package com.sinosoft.etp.up.testpaper.service.impl;

import cn.jkingtools.utils.DateUtils;
import cn.jkingtools.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.examination.mapper.TExaminationMapper;
import com.sinosoft.etp.up.testpaper.domain.TTestPaperSnapshotJson;
import com.sinosoft.etp.up.testpaper.domain.TTestpaperStudent;
import com.sinosoft.etp.up.testpaper.dto.PaperSnapshotDTO;
import com.sinosoft.etp.up.testpaper.dto.TSubjectAnsweredReview;
import com.sinosoft.etp.up.testpaper.dto.TestPaperReviewDetail;
import com.sinosoft.etp.up.testpaper.mapper.TTestpaperSnapshotMapper;
import com.sinosoft.etp.up.testpaper.mapper.TTestpaperStudentMapper;
import com.sinosoft.etp.up.testpaper.service.ITTestpaperStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 试卷学员关联Service业务层处理
 *
 *
 * @date 2022-02-28
 */
@Service
public class TTestpaperStudentServiceImpl implements ITTestpaperStudentService
{
    @Autowired
    private TTestpaperStudentMapper tTestpaperStudentMapper;

    @Autowired
    private TExaminationMapper tExaminationMapper;

    @Autowired
    private TTestpaperSnapshotMapper tTestpaperSnapshotMapper;

    /**
     * 查询试卷学员关联
     *
     * @param id 试卷学员关联ID
     * @return 试卷学员关联
     */
    @Override
    public TTestpaperStudent selectTTestpaperStudentById(Integer id)
    {
        return tTestpaperStudentMapper.selectTTestpaperStudentById(id);
    }

    /**
     * 查询试卷学员关联列表
     *
     * @param tTestpaperStudent 试卷学员关联
     * @return 试卷学员关联
     */
    @Override
    public List<TTestpaperStudent> selectTTestpaperStudentList(TTestpaperStudent tTestpaperStudent)
    {
        return tTestpaperStudentMapper.selectTTestpaperStudentList(tTestpaperStudent);
    }

    /**
     * 新增试卷学员关联
     *
     * @param tTestpaperStudent 试卷学员关联
     * @return 结果
     */
    @Override
    public int insertTTestpaperStudent(TTestpaperStudent tTestpaperStudent)
    {
        tTestpaperStudent.setCreateTime(DateUtils.getNowDate());
        return tTestpaperStudentMapper.insertTTestpaperStudent(tTestpaperStudent);
    }

    /**
     * 修改试卷学员关联
     *
     * @param tTestpaperStudent 试卷学员关联
     * @return 结果
     */
    @Override
    public int updateTTestpaperStudent(TTestpaperStudent tTestpaperStudent)
    {
        tTestpaperStudent.setUpdateTime(DateUtils.getNowDate());
        return tTestpaperStudentMapper.updateTTestpaperStudent(tTestpaperStudent);
    }

    /**
     * 批量删除试卷学员关联
     *
     * @param ids 需要删除的试卷学员关联ID
     * @return 结果
     */
    @Override
    public int deleteTTestpaperStudentByIds(String[] ids)
    {
        return tTestpaperStudentMapper.deleteTTestpaperStudentByIds(ids);
    }

    /**
     * 删除试卷学员关联信息
     *
     * @param id 试卷学员关联ID
     * @return 结果
     */
    @Override
    public int deleteTTestpaperStudentById(String id)
    {
        return tTestpaperStudentMapper.deleteTTestpaperStudentById(id);
    }

    /**
     * 成绩查询接口
     * @param id
     * @return
     */
    @Override
    public TestPaperReviewDetail getScore(String id) {
        TTestpaperStudent tTestpaperStudent = tTestpaperStudentMapper.selectTTestpaperStudentById(Integer.parseInt(id));
        PaperSnapshotDTO tTestPaperSnapshotJson = JSON.parseObject(tTestpaperStudent.getDa(), PaperSnapshotDTO.class);
        Map<String, TSubjectAnsweredReview> sj = tTestPaperSnapshotJson.getSj();

        TestPaperReviewDetail testPaperReviewDetail = new TestPaperReviewDetail();
//        sj.forEach((k, v)->{
//            testPaperReviewDetail.addScore(v.isRight()?v.getLogicalScore().longValue():0);
//            testPaperReviewDetail.addCorrectNum(v.isRight()?1:0);
//            Map<String, TestPaperReviewDetail> details = tTestPaperSnapshotJson.getDetail();
//            if (details == null) details = new HashMap<>();
//            TestPaperReviewDetail detail = details.get(String.valueOf(v.getLx()));
//            if (detail == null ) {
//                detail = new TestPaperReviewDetail();
//                details.put(String.valueOf(v.getLx()), detail);
//            }
//            detail.addScore(v.isRight()?v.getLogicalScore().longValue():0);
//            detail.addCorrectNum(v.isRight()?1:0);
//        });

        testPaperReviewDetail = tTestPaperSnapshotJson.getDetail();
        // 设置分数等级
        String fsdj = tTestPaperSnapshotJson.getExamination().getFsdj();
        String fsdj1 = getFSDJ(fsdj, testPaperReviewDetail.getScore());
        testPaperReviewDetail.setLevel(fsdj1);

        // 正确率
//        tTestPaperSnapshotJson.getRate();

        return testPaperReviewDetail;
    }

    private String getFSDJ(String fsdj,Long score){
        List<Map<String, Object>> maps = null;
        try {
            maps = JSONObject.parseObject(fsdj, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        AtomicReference<String> fsdjC = new AtomicReference<>("");
        if (maps != null){
            try {
                maps.forEach(stringListMap -> {
                    List<Integer> value = (List<Integer>)stringListMap.get("value");
                    String key = (String) stringListMap.get("key");
                    long start = Long.parseLong(value.get(0) + "");
                    long end = Long.parseLong(value.get(1) + "");
                    if (score != null){
                        if (score >= start && score < end){
                            fsdjC.set(key);
                        }
                    }else {
                        fsdjC.set("不及格");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fsdjC.get();
    }

    /**
     * 查询历史考试
     * @param tTestpaperStudent
     * @return
     */
    @Override
    public List<TTestpaperStudent> getHistoryExam(TTestpaperStudent tTestpaperStudent) {
        tTestpaperStudent.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        List<TTestpaperStudent> list = tTestpaperStudentMapper.selectTTestpaperStudentHistoryExamList(tTestpaperStudent);
        list.forEach(student -> {
            String fsdj = student.getFsdj();
            Long score = student.getScore();
            String fsdj1 = getFSDJ(fsdj, score);
            student.setFsdj(fsdj1);
        });
        return list;
    }

    @Override
    public  TTestPaperSnapshotJson selectAnswerInfo(Long id) {
        List<Map<String, Object>> list = tTestpaperStudentMapper.selectAnswerInfo(id);
        if(null != list && list.size()>0) {
            String snapshot = list.get(0).get("snapshot").toString();
            Map<String,Object> daMap = JSONObject.parseObject(String.valueOf(list.get(0).get("da")));
            TTestPaperSnapshotJson snapshotObject = snapshotDataProcessing(snapshot,
                    Integer.parseInt(list.get(0).get("id").toString()),
                    Integer.parseInt(list.get(0).get("snapshotId").toString()),daMap);
            snapshotObject.getExamination().setPjzt(list.get(0).get("pjzt")==null?null:list.get(0).get("pjzt").toString());
            snapshotObject.getExamination().setPjTeacher(list.get(0).get("pj_teacher")==null?null:list.get(0).get("pj_teacher").toString());
            snapshotObject.getExamination().setPjTime(list.get(0).get("pj_time")==null?null:
                    Date.from(((LocalDateTime) list.get(0).get("pj_time")).atZone(ZoneId.systemDefault()).toInstant()));
            return snapshotObject;
        }
        return new TTestPaperSnapshotJson() ;
    }

    /**
     * 快照数据处理
     */
    private TTestPaperSnapshotJson snapshotDataProcessing(String data, Integer id, Integer snapshotId, Map<String,Object> daMap) {
        if (StringUtils.isNotEmpty(data)) {
            TTestPaperSnapshotJson tTestPaperSnapshotJson = JSONObject.parseObject(data, TTestPaperSnapshotJson.class);
            tTestPaperSnapshotJson.setId(id);
            tTestPaperSnapshotJson.setSnapshot(snapshotId);
            tTestPaperSnapshotJson.setDa(daMap);
            return tTestPaperSnapshotJson;
        }
        return new TTestPaperSnapshotJson();
    }
}
