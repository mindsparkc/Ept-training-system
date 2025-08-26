package com.sinosoft.etp.up.testpaper.service.impl;

import cn.jkingtools.utils.BeanUtils;
import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.common.constant.HandType;
import com.sinosoft.etp.common.constant.SubjectType;
import com.sinosoft.etp.up.errorbook.domain.TErrorbook;
import com.sinosoft.etp.up.errorbook.mapper.TErrorbookMapper;
import com.sinosoft.etp.up.examination.domain.TExamination;
import com.sinosoft.etp.up.examination.mapper.TExaminationMapper;
import com.sinosoft.etp.up.testpaper.domain.TTestPaperSnapshotJson;
import com.sinosoft.etp.up.testpaper.domain.TTestpaperSnapshot;
import com.sinosoft.etp.up.testpaper.domain.TTestpaperStudent;
import com.sinosoft.etp.up.testpaper.dto.PaperSnapshotDTO;
import com.sinosoft.etp.up.testpaper.dto.TSubjectAnsweredReview;
import com.sinosoft.etp.up.testpaper.dto.TestPaperReviewDetail;
import com.sinosoft.etp.up.testpaper.mapper.TTestpaperSnapshotMapper;
import com.sinosoft.etp.up.testpaper.mapper.TTestpaperStudentMapper;
import com.sinosoft.etp.up.testpaper.mapper.TTestpaperSubjectMapper;
import com.sinosoft.etp.up.testpaper.service.ITTestpaperSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 试卷关联题目Service业务层处理
 *
 *
 * @date 2021-12-17
 */
@Service
public class TTestpaperSubjectServiceImpl implements ITTestpaperSubjectService {
    private final static Object syncLock = new Object();

    @Autowired
    private TTestpaperSubjectMapper tTestpaperSubjectMapper;

    @Autowired
    TTestpaperStudentMapper tTestpaperStudentMapper;

    @Autowired
    TTestpaperSnapshotMapper tTestpaperSnapshotMapper;

    @Autowired
    TExaminationMapper tExaminationMapper;

    @Autowired
    TErrorbookMapper tErrorbookMapper;

    /**
     * 试卷关联题目信息
     *
     * @param sjid 试卷关联题目ID
     * @return 结果
     */
    @Override
    public List<Map<String, Object>> getSubjectByPaper(Long sjid) {
        return tTestpaperSubjectMapper.getSubjectByPaper(sjid);
    }

    @Override
    public PaperSnapshotDTO start(Long ksid){
        PaperSnapshotDTO paperSnapshotDTO;
        // 生成学员的考试信息
        TTestpaperStudent testpaperStudent = new TTestpaperStudent();
        testpaperStudent.setKsid(ksid);
        testpaperStudent.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());

        // TODO: 需添加以下逻辑
        // TODO: 查询是否有已参加未结束的考试（T_TESTPAPER_STUDENT）,则加载快照继续开始考试，就不走获取试卷快照的流程了
        //           -- 添加了字段 hand_type 标记交卷状态，0-未交卷；1-人工交卷；2-强制交卷
        // TODO: 直接根据考试 ID 和学员ID去查询就可以
        // TODO: snapshot 结构为 PaperSnapshotDTO，其中包含

        // TODO: 需要加载 da，将 da 解析，授予快照中的字段 - da 字段做修改，添加判卷信息

        /**
         * 不存在已开始的考试，走以下流程，生成快照并建立与学员的关联
         */
        paperSnapshotDTO = buildPaperSnashot(ksid);
        TTestpaperStudent testpaperStudents = tTestpaperStudentMapper.selectIsSubmit(ksid);
        System.out.println(testpaperStudents);
        testpaperStudent.setSnapshot(paperSnapshotDTO.getSnapshot());
        testpaperStudent.setSjid(paperSnapshotDTO.getExamination().getSjid());
        // TODO: wlj 开始考试，da 字段保存为考试快照
        testpaperStudent.setDa(JSON.toJSONString(paperSnapshotDTO));
        testpaperStudent.setCreateBy(SecurityUtils.getUsername());

        if(testpaperStudents == null || testpaperStudents.getHandTime()!=null){
            tTestpaperStudentMapper.insertTTestpaperStudent(testpaperStudent);
            paperSnapshotDTO.setId(testpaperStudent.getId());
        }else if(testpaperStudents != null && testpaperStudents.getHandTime() == null){
            paperSnapshotDTO.setId(testpaperStudents.getId());
        }
        return paperSnapshotDTO;
    }
    /**
     * 阅卷
     *
     * @param tTestPaperSnapshotJson
     */
    @Override
    public void markThePapers(TTestPaperSnapshotJson tTestPaperSnapshotJson) {
        autoReviewPaper(tTestPaperSnapshotJson);
    }

    /**
     * 生成试卷快照
     * 生成快照前会尝试先判断快照是否已存在，若已存在则直接读取快照，若快照不存在，则会重新生成快照，并将快照写入到数据库中
     *
     * @param ksid
     * @return
     */
    private PaperSnapshotDTO buildPaperSnashot(Long ksid) {

        // tTestpaperStudentMapper.selectTTestpaperStudentInfoByIds()

        // 获取考试基本信息
        TExamination tExamination = tExaminationMapper.selectTExaminationByKSID(ksid);
        // 尝试获取试卷快照
        TTestpaperSnapshot snapshot = tTestpaperSnapshotMapper.selectTTestpaperSnapshotByInfo(ksid, tExamination.getSjid());
        if (snapshot == null) {
            synchronized (syncLock) {
                snapshot = tTestpaperSnapshotMapper.selectTTestpaperSnapshotByInfo(ksid, tExamination.getSjid());
                if (snapshot == null) {
                    snapshot = new TTestpaperSnapshot();
                    snapshot.setSnapshot(JSON.toJSONString(buildPaperSnashot0(ksid)));
                    snapshot.setKsid(ksid);
                    snapshot.setSjid(tExamination.getSjid());
                    tTestpaperSnapshotMapper.insertTTestpaperSnapshot(snapshot);
                }
            }
        }
        snapshot.setSnapshot(JSON.toJSONString(buildPaperSnashot0(ksid)));
        PaperSnapshotDTO paperSnapshotDTO = JSONObject.parseObject(snapshot.getSnapshot(), PaperSnapshotDTO.class);
        paperSnapshotDTO.setSnapshot(snapshot.getId());
        return paperSnapshotDTO;
    }

    /**
     * 生成试卷快照
     *
     * @param ksid
     * @return
     */
    private PaperSnapshotDTO buildPaperSnashot0(Long ksid) {
        // 获取考试基本信息
        TExamination tExamination = tExaminationMapper.selectTExaminationByKSID(ksid);
        // 获取考试题目信息
        List<Map<String, Object>> subjectByPaper = tTestpaperSubjectMapper.getSubjectByPaper(tExamination.getSjid());
        PaperSnapshotDTO paperSnapshotDTO = new PaperSnapshotDTO();
        paperSnapshotDTO.setExamination(tExamination);
        Double totalScore = .0D;
        Integer totalNum = 0;
        Map<String, TestPaperReviewDetail> detailMap = new LinkedHashMap<>();
        for (Map<String, Object> subject : subjectByPaper) {
            paperSnapshotDTO.addQuestions(BeanUtils.map(subject, TSubjectAnsweredReview.class));
            totalScore += (Double) subject.get("score");
            totalNum++;

            TestPaperReviewDetail lxDetail = detailMap.get((String) subject.get("lx"));
            if (lxDetail == null) {
                lxDetail = new TestPaperReviewDetail();
                detailMap.put((String) subject.get("lx"), lxDetail);
                lxDetail.setType(Integer.valueOf((String) subject.get("lx")));
                lxDetail.setTypeName((String) subject.get("lxbq"));
            }

            lxDetail.addTotalNum(1);
            lxDetail.addTotalScore(((Double) subject.get("score")).longValue());
        }
        // 在快照总保存试卷总分数
        TestPaperReviewDetail paperReviewDetail = new TestPaperReviewDetail();
        paperReviewDetail.setTotalScore(totalScore.longValue()); // 试卷总分数
        paperReviewDetail.setTotalNum(totalNum); // 试卷总题目数
        paperReviewDetail.setDetails(detailMap); // 各类型题目的总条数和总分数

        paperSnapshotDTO.setDetail(paperReviewDetail);
        return paperSnapshotDTO;
    }

    /**
     * 阅卷
     * 自动阅卷，处理单选题、多选题、判断题、填空题
     *
     * @author WANGLIJIE
     */
    private Double autoReviewPaper(TTestPaperSnapshotJson testPaperSnapshotJson) {
        // 读取试卷快照，数据库中的试卷快照包括题目的答案
            TTestpaperSnapshot testpaperSnapshot
                    = tTestpaperSnapshotMapper.selectTTestpaperSnapshotById(testPaperSnapshotJson.getSnapshot());
        TTestpaperStudent tTestpaperStudent1 = tTestpaperStudentMapper.selectTTestpaperStudentById(testPaperSnapshotJson.getId());
        // answered 是学员的答案
        List<Map<String, Object>> answereds = testPaperSnapshotJson.getSj();
        // snapshotAnswered 是加载的快照，快照中包含答案和解析
        PaperSnapshotDTO paperSnapshotDTO = JSONObject.parseObject(tTestpaperStudent1.getDa()==null?testpaperSnapshot.getSnapshot():tTestpaperStudent1.getDa(), PaperSnapshotDTO.class);
        // 记分
        AtomicReference<Double> totalScore = new AtomicReference<>(0.0D);
        //创建错题本对象，记录错题信息
        TErrorbook tErrorbook = new TErrorbook();
        Long xyid = SecurityUtils.getLoginUser().getUser().getUserId();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String date = sdf.format(now);
        // 遍历学员答案，与加载的试卷快照答案做比对
        for (Map<String, Object> answered : answereds) {
            paperSnapshotDTO.getSj().forEach((key, item) -> {
                // 对比题目 ID，在快照中命中题目信息
                if(item.getId().intValue() == (Integer)answered.get("id")
                ) {
                    Long id = tErrorbookMapper.selectTErrorbookByKs(testPaperSnapshotJson.getExamination().getId(),null,item.getTkid().longValue(),xyid);
                    /* 将学员答案赋予快照，做保存 */
                    if ((SubjectType.SINGLE_CHOICE.equals(item.getLx()) || SubjectType.TRUE_FALSE_QUESTION.equals(item.getLx()))
                      && StringUtils.isNotBlank((String)answered.get("da"))) {
                        // 单选题 和 判断题
                        item.setAnswered((String) answered.get("da"));
                        if (item.getDa().equals(answered.get("da"))) {
                            totalScore.updateAndGet(v -> v + item.getScore());
                            item.setIsRight(true);
                            item.setLogicalScore(item.getScore());
                            paperSnapshotDTO.getDetail().addScore(item.getScore().longValue());
                            paperSnapshotDTO.getDetail().addCorrectNum(1);
                            paperSnapshotDTO.getDetail().getDetails().get(item.getLx()).addScore(item.getScore().longValue());
                            paperSnapshotDTO.getDetail().getDetails().get(item.getLx()).addCorrectNum(1);
                        }else{
                            //存储错题
                            tErrorbook.setAnswered(answered.get("da").toString());
                            tErrorbook.setDa(item.getDa());
                            tErrorbook.setCtlx("1");
                            tErrorbook.setJx(item.getJx());
                            tErrorbook.setKsid(testPaperSnapshotJson.getExamination().getId());
                            tErrorbook.setRemake("在"+date+"的\""+ testPaperSnapshotJson.getExamination().getMc() +"\"考试中答错");
                            tErrorbook.setStlx(item.getLx());
                            tErrorbook.setStid(item.getTkid().longValue());
                            tErrorbook.setXyid(xyid);
                            tErrorbook.setXx(item.getXx()==null?null:item.getXx());
                            tErrorbook.setTg(item.getTg());
                            if(id==null||"".equals(id)){
                                tErrorbook.setId(null);
                                tErrorbook.setCreateTime(DateUtils.getNowDate());
                                tErrorbookMapper.insertTErrorbook(tErrorbook);
                            }else{
                                tErrorbook.setId(id);
                                tErrorbook.setUpdateTime(DateUtils.getNowDate());
                                tErrorbookMapper.updateTErrorbook(tErrorbook);
                            }

                        }
                    }else if (SubjectType.MULTIPE_CHOICE.equals(item.getLx())
                    && StringUtils.isNotBlank((String)answered.get("da"))) {
                        // 多选题
                        List<String> s = Arrays.asList(((String)answered.get("da")).split(",").clone())
                                .stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                        item.setAnswered(JSON.toJSONString(s));
                        String[] strings = item.getDa().split(",");
                        List<String> r = Arrays.asList(strings);
                        if (s.size() == strings.length) {
                            for (int i=0; i<s.size(); i++) {
                                int indexOf = r.indexOf(s.get(i));
                                if (indexOf == -1) {
                                    item.setIsRight(false);
                                    break;
                                }
                                item.setIsRight(true);
                                item.setLogicalScore(item.getScore());
                            }
                            if (item.getIsRight()) {
                                totalScore.updateAndGet(v -> v + item.getScore());
                                paperSnapshotDTO.getDetail().addScore(item.getScore().longValue());
                                paperSnapshotDTO.getDetail().addCorrectNum(1);
                                paperSnapshotDTO.getDetail().getDetails().get(item.getLx()).addScore(item.getScore().longValue());
                                paperSnapshotDTO.getDetail().getDetails().get(item.getLx()).addCorrectNum(1);
                            }else{
                                //存储错题
                                tErrorbook.setAnswered(answered.get("da").toString());
                                tErrorbook.setDa(item.getDa());
                                tErrorbook.setCtlx("1");
                                tErrorbook.setJx(item.getJx());
                                tErrorbook.setKsid(testPaperSnapshotJson.getExamination().getId());
                                tErrorbook.setRemake("在"+date+"的\""+ testPaperSnapshotJson.getExamination().getMc() +"\"考试中答错");
                                tErrorbook.setStlx(item.getLx());
                                tErrorbook.setStid(item.getTkid().longValue());
                                tErrorbook.setXyid(xyid);
                                tErrorbook.setXx(item.getXx());
                                tErrorbook.setTg(item.getTg());
                                if(id==null||"".equals(id)){
                                    tErrorbook.setId(null);
                                    tErrorbook.setCreateTime(DateUtils.getNowDate());
                                    tErrorbookMapper.insertTErrorbook(tErrorbook);
                                }else{
                                    tErrorbook.setId(id);
                                    tErrorbook.setUpdateTime(DateUtils.getNowDate());
                                    tErrorbookMapper.updateTErrorbook(tErrorbook);
                                }
                            }
                        }
                    }else if (SubjectType.FILL_IN_THE_BLANKS.equals(item.getLx())
                    && answered.get("da") != null) {
                        // 填空题 [["问题","但","散"],["你","有","无梦"]]
                        item.setAnswered(JSONObject.toJSONString(answered.get("da")).replace(" ", ""));
                        List<String> s = (List<String>) answered.get("da");
                        List<List<String>> r = JSONObject.parseObject(item.getDa().replace(" ",""), List.class);
                        for (int i=0; i<s.size(); i++) {
                            int indexOf = r.get(i).indexOf(s.get(i).trim());
                            if (indexOf == -1) {
                                item.setIsRight(false);
                                break;
                            }
                            item.setIsRight(true);
                            item.setLogicalScore(item.getScore());
                        }

                        if (item.getIsRight()) {
                            totalScore.updateAndGet(v -> v + item.getScore());
                            paperSnapshotDTO.getDetail().addScore(item.getScore().longValue());
                            paperSnapshotDTO.getDetail().addCorrectNum(1);
                            paperSnapshotDTO.getDetail().getDetails().get(item.getLx()).addScore(item.getScore().longValue());
                            paperSnapshotDTO.getDetail().getDetails().get(item.getLx()).addCorrectNum(1);
                        }else{
                            //存储错题
                            tErrorbook.setAnswered(item.getAnswered());
                            tErrorbook.setDa(item.getDa());
                            tErrorbook.setCtlx("1");
                            tErrorbook.setJx(item.getJx());
                            tErrorbook.setKsid(testPaperSnapshotJson.getExamination().getId());
                            tErrorbook.setRemake("在"+date+"的\""+ testPaperSnapshotJson.getExamination().getMc() +"\"考试中答错");
                            tErrorbook.setStlx(item.getLx());
                            tErrorbook.setStid(item.getTkid().longValue());
                            tErrorbook.setXyid(xyid);
                            tErrorbook.setTg(item.getTg());
                            if(id==null||"".equals(id)){
                                tErrorbook.setId(null);
                                tErrorbook.setCreateTime(DateUtils.getNowDate());
                                tErrorbookMapper.insertTErrorbook(tErrorbook);
                            }else{
                                tErrorbook.setId(id);
                                tErrorbook.setUpdateTime(DateUtils.getNowDate());
                                tErrorbookMapper.updateTErrorbook(tErrorbook);
                            }
                        }
                    }else if (SubjectType.Q_A.equals(item.getLx())
                            && answered.get("da") != null) {
                        item.setAnswered((String) answered.get("da"));
                    }
                }
            });
        }

        //更新分数和答案
        TTestpaperStudent tTestpaperStudent = new TTestpaperStudent();
        tTestpaperStudent.setId(testPaperSnapshotJson.getId());
        // TODO: 这里有问题 Double 转 Long
        tTestpaperStudent.setScore(totalScore.get().longValue());
        tTestpaperStudent.setHandType(HandType.PERSON_HAND_TYPE);
        tTestpaperStudent.setHandTime(new Date());
        // 需要加试卷正确率
        tTestpaperStudent.setRate((long) (paperSnapshotDTO.getDetail().getCorrectNum()/paperSnapshotDTO.getDetail().getTotalNum()));
        tTestpaperStudent.setDa(JSON.toJSONString(paperSnapshotDTO));
        tTestpaperStudent.setUpdateTime(new Date());
        System.out.println("_________________________________");
        System.out.println(JSON.toJSONString(paperSnapshotDTO));
        System.out.println(tTestpaperStudent.getId());
        tTestpaperStudentMapper.updateTTestpaperStudent(tTestpaperStudent);
        return totalScore.get();
    }
}
