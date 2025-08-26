package com.sinosoft.etp.cp.analysis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.analysis.domain.DeptAndExaminationWithCp;
import com.sinosoft.etp.cp.analysis.domain.SubjectAndExaminationWithCp;
import com.sinosoft.etp.cp.analysis.domain.TExaminationScoreAnalysisWithCp;
import com.sinosoft.etp.cp.analysis.mapper.TExaminationScoreAnalysisWithCpMapper;
import com.sinosoft.etp.cp.analysis.service.ITExaminationScoreAnaltsisService;
import com.sinosoft.etp.cp.examination.mapper.TExaminationStudentWithCpMapper;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import com.sinosoft.etp.cp.testpaper.domain.TTestpaperStudentWithCp;
import com.sinosoft.etp.cp.testpaper.mapper.TTestpaperWithCpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 考试Service业务层处理
 *
 *
 * @date 2021-12-24
 */
@Service
public class TExaminationScoreAnalysisServiceImpl implements ITExaminationScoreAnaltsisService
{
    @Resource
    private TExaminationScoreAnalysisWithCpMapper tExaminationScoreAnalysisMapper;
    @Autowired
    private ITTagWithCpService iTTagService;


    /** @type update 2021-12-31 考试限定用户相关 */
    @Resource
    private TExaminationStudentWithCpMapper tExaminationStudentMapper;
    @Resource
    private TTestpaperWithCpMapper tTestpaperWithCpMapper;


    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    @Override
    @DataScope(deptAlias = "d")
    public TExaminationScoreAnalysisWithCp selectTExaminationById(Long id)
    {
        //获取当前用户的部门id
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        //查询考试信息
        TExaminationScoreAnalysisWithCp tExamination = tExaminationScoreAnalysisMapper.selectTExaminationScoreAnalysisById(id);
        //根据考试开始结束时间判断考试状态
        Date now = new Date();
        if(tExamination.getKskssj().after(now)){
            tExamination.setExamStatus("0");
        }else if(tExamination.getKskssj().before(now)&&tExamination.getKsjssj().after(now)){
            tExamination.setExamStatus("1");
        }else if(tExamination.getKsjssj().before(now)){
            tExamination.setExamStatus("2");
        }
        //查询考试各项分数
        List list = new ArrayList();
        Integer countScore = 0;
        List<TTestpaperStudentWithCp>tTestpaperStudentWithCps = tTestpaperWithCpMapper.getExamOfStudentInfo(id,deptId);
        for (TTestpaperStudentWithCp tTestpaperStudentWithCp :tTestpaperStudentWithCps) {
            list.add(tTestpaperStudentWithCp.getScore());
            countScore+=tTestpaperStudentWithCp.getScore();
        }
        if(list.size()>0){
            //最高得分
            Integer maxScore = (Integer) Collections.max(list);
            tExamination.setTopScore(maxScore);
            //最低得分
            Integer minScore = (Integer) Collections.min(list);
            tExamination.setLastScore(minScore);
            //平均分数
            BigDecimal averageScore = BigDecimal.valueOf(countScore).divide(BigDecimal.valueOf(tTestpaperStudentWithCps.size()),1,BigDecimal.ROUND_HALF_UP);
            tExamination.setAverageScore(averageScore.doubleValue());
        }else{
            //最高得分
            tExamination.setTopScore(0);
            //最低得分
            tExamination.setLastScore(0);
            //平均分数
            tExamination.setAverageScore(0.0);
        }

        //查询考试人数
        int peopleNum = tExaminationScoreAnalysisMapper.selectExamNum(id,deptId);
        tExamination.setPeopleNum(peopleNum);
        tExamination.setSysUsers(tExaminationStudentMapper.selectByKsId(id));
        return tExamination;
    }

    /**
     * 查询考试列表
     *
     * @param tExamination 考试
     * @return 考试
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TExaminationScoreAnalysisWithCp> selectTExaminationList(TExaminationScoreAnalysisWithCp tExamination)
    {
        return tExaminationScoreAnalysisMapper.selectTExaminationScoreAnalysisList(tExamination);
    }

    /**
     * 查询考试部门关联列表
     * @param deptAndExaminationWithCp 考试部门
     * @return
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<DeptAndExaminationWithCp> selectTExaminationByDeptList(DeptAndExaminationWithCp deptAndExaminationWithCp) {
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        //获取当前部门的考试分析信息
        deptAndExaminationWithCp.setDeptId(deptId);
        List<DeptAndExaminationWithCp> deptAndExaminationWithCps = getDeptInfo(deptAndExaminationWithCp);
        //获取当前部门的子部门考试分析信息
        deptAndExaminationWithCp.setParentId(deptId);
        deptAndExaminationWithCp.setDeptId(null);
        List<DeptAndExaminationWithCp> deptAndExaminationWithCps1 = getDeptByChildInfo(deptAndExaminationWithCp);
        //把当前部门与子部门合为一个集合
        List<DeptAndExaminationWithCp> deptAndExaminationWithCps2 = new ArrayList<>();
        deptAndExaminationWithCps2.add(deptAndExaminationWithCps.get(0));
        for(int i=0;i<deptAndExaminationWithCps1.size();i++){
            deptAndExaminationWithCps2.add(deptAndExaminationWithCps1.get(i));
        }
        //System.out.println(deptAndExaminationWithCps2);
        return deptAndExaminationWithCps2;
    }

    @Override
    public List<SubjectAndExaminationWithCp> getSubjectInfo(Long sjid) {
        List<SubjectAndExaminationWithCp> subjectData = new ArrayList<>();
        //根据试卷id获取所有题目的id
        List<Long> subjectIds = tExaminationScoreAnalysisMapper.getTkid(sjid);
        for (int i = 0; i < subjectIds.size(); i++) {
            Long tkid = subjectIds.get(i);
            //根据题库id获取对应的试题信息
            SubjectAndExaminationWithCp subjectAndExaminationWithCps = tExaminationScoreAnalysisMapper.getSubjectInfo(tkid);
            if(subjectAndExaminationWithCps!=null){
                subjectData.add(subjectAndExaminationWithCps);
            }
        }
        return subjectData;
    }

    @Override
    public SubjectAndExaminationWithCp getSubjectAnalysis(Long id, Long ksid,String lx) {
        SubjectAndExaminationWithCp subjectAndExaminationWithCp = new SubjectAndExaminationWithCp();
        //根据题目id和考试id获取题目选项
        HashMap dx = new HashMap();
        HashMap pd = new HashMap();
        HashMap fx = new HashMap();
        String daPath = "$.sj.\"" + id + "\".answered";
        List<String> yhDa = tExaminationScoreAnalysisMapper.getYhda(daPath, ksid);
        if(!"3".equals(lx)){
            String xxPath = "$.sj.\"" + id + "\".xx";
            List<String> xxData = tExaminationScoreAnalysisMapper.getYhda(xxPath, ksid);
            String xxValue = JSON.parse(xxData.get(0)).toString();
            JSONArray xx = JSONArray.parseArray(xxValue);
            int i;
            int j;
            String key = null;
            for (i = 0; i < xx.size(); i++) {
                int dxNum = 0;
                int fxNum = 0;
                //System.out.println("JSON遍历值 =====" + xx.get(i));
                key = getKey(xx.get(i).toString());
                if (yhDa.size() > 0) {
                    for (j = 0; j < yhDa.size(); j++) {
                        if (yhDa.get(j) != null) {
                            String d = yhDa.get(j);
                            String data = d.substring(1, d.length()-1);
                            if (key.equals(data) && "1".equals(lx)) {
                                dxNum++;
                            } else if ("2".equals(lx)) {
                                String fxDa = yhDa.get(j).substring(1, yhDa.size());
                                String[] yhDas = fxDa.split(",");
                                for (int k = 0; k < yhDas.length; k++) {
                                    String val = yhDas[k];
                                    if (key.equals(val)) {
                                        fxNum++;
                                    }
                                }
                            }
                        }
                    }
                    if ("1".equals(lx)) {
                        dx.put(key, String.valueOf(dxNum));
                    } else {
                        fx.put(key, String.valueOf(fxNum));
                    }
                }
            }
        }else{
            int isTrue = 0;
            int isFalse = 0;
            if(yhDa.size()>0){
                for (int i = 0; i < yhDa.size(); i++) {
                    if(pd.size()>0){
                        String data = yhDa.get(i);
                        String value = data==null?"":data.substring(1,data.length()-1);
                        if("0".equals(value)){
                            isFalse++;
                            pd.put("0",String.valueOf(isFalse));
                        }else if("1".equals(value)){
                            isTrue++;
                            pd.put("1",String.valueOf(isTrue));
                        }
                    }else{
                        pd.put("0",String.valueOf(isFalse));
                        pd.put("1",String.valueOf(isTrue));
                    }
                }
            }else{
               pd.put("0",isFalse);
               pd.put("1",isTrue);
            }
        }
        if("1".equals(lx)){
            subjectAndExaminationWithCp.setDxArr(dx);
        }else if("2".equals(lx)){
            subjectAndExaminationWithCp.setFxArr(fx);
        }else{
            subjectAndExaminationWithCp.setPdArr(pd);
        }
        subjectAndExaminationWithCp.setLx(lx);
        return subjectAndExaminationWithCp;
    }

    /**
     * 根据考试id获取当前部门的考试分析数据
     * @param deptAndExaminationWithCp
     */
    public List<DeptAndExaminationWithCp> getDeptInfo(DeptAndExaminationWithCp deptAndExaminationWithCp){
        Long deptId = deptAndExaminationWithCp.getDeptId();
        Long ksid = deptAndExaminationWithCp.getKsid();
        List<DeptAndExaminationWithCp> deptAndExaminationWithCps1 = tExaminationScoreAnalysisMapper.selectDeptAndExaminationWithCpList(deptAndExaminationWithCp);
        //定义分数数组，用于排序获取成绩最高分和最低分
        List list = new ArrayList();
        Integer countScore = 0;
        Long sunTime = 0L;
        Map map = new HashMap();
        List<TTestpaperStudentWithCp>tTestpaperStudentWithCps = tTestpaperWithCpMapper.getExamOfStudentInfo(ksid,deptId);
        if(tTestpaperStudentWithCps.size()>0){
            for (TTestpaperStudentWithCp tTestpaperStudentWithCp :tTestpaperStudentWithCps) {
                list.add(tTestpaperStudentWithCp.getScore());
                countScore+=tTestpaperStudentWithCp.getScore();
                Long startTime = tTestpaperStudentWithCp.getCreateTime().getTime();
                Long endTime = tTestpaperStudentWithCp.getHandTime().getTime();
                sunTime += (endTime-startTime);
                //获取考试分数等级并转换为json格式
                String fsdj = tTestpaperStudentWithCp.getFsdj();
                JSONArray json = JSONArray.parseArray(fsdj);
                int score = tTestpaperStudentWithCp.getScore();
                //解析以json形式存储的考试等级
                for (int i = 0; i < json.size(); i++) {
                    JSONObject js = JSONObject.parseObject(json.getString(i));
                    List ls = Collections.singletonList(js.get("value"));
                    List ls2 = (List) ls.get(0);
                    //System.out.println(ls2);
                    String key = js.get("key").toString();
                    int firstVal = Integer.parseInt(ls2.get(0).toString());
                    int lastVal = Integer.parseInt(ls2.get(1).toString());
                    //判断逻辑：定义一个map，把考试等级遍历出来的等级名称存入map中key，value值默认为0，
                    // 当学员考试分数符合考试等级时，把map中对应的考试等级的key值对应的value值加1
                    if (map.size() > 0) {
                        for (Object mapK : map.keySet()) {
                            //判断这个循环中获取的考试等级在map中有没有存在、学员考试分数是否符合此等级，如果存在且符合则对应的value加1，
                            //不存在但等级符合则新增一个对应等级的key-value，value的值为1，
                            //如果不存在且不符合当前等级，则新增一个对应等级的key-value，value值为0
                            if(map.containsKey(key)){
                                if (key.equals(mapK.toString()) && score >= firstVal && score <= lastVal) {
                                    int val = Integer.parseInt(map.get(mapK).toString());
                                    val+=1;
                                    map.put(mapK, val);
                                    continue;
                                }
                            }else{
                                if(score >= firstVal && score <= lastVal){
                                    map.put(key,1);
                                }else{
                                    map.put(key,0);
                                }
                            }
                        }
                    }else if (score >= firstVal && score <= lastVal) {
                        map.put(key,1);
                    }else{
                        map.put(key,0);
                    }
                }
                //deptAndExaminationWithCps1.get(0).setParams(map);
            }
            deptAndExaminationWithCps1.get(0).setParams(map);
            //最高得分
            Integer maxScore = (Integer) Collections.max(list);
            deptAndExaminationWithCps1.get(0).setTopScore(maxScore);
            //最低得分
            Integer minScore = (Integer) Collections.min(list);
            deptAndExaminationWithCps1.get(0).setLastScore(minScore);
            //平均分数
            BigDecimal averageScore = BigDecimal.valueOf(countScore).divide(BigDecimal.valueOf(tTestpaperStudentWithCps.size()),2,BigDecimal.ROUND_HALF_UP);
            deptAndExaminationWithCps1.get(0).setAverageScore(averageScore.doubleValue());
            //应考人数
            int peopleNum = tExaminationScoreAnalysisMapper.selectExamNum(ksid,deptId);
            deptAndExaminationWithCps1.get(0).setPeopleNum(peopleNum);
            //实考人数
            int practiceNum = tExaminationScoreAnalysisMapper.selectPracticeNum(ksid,deptId);
            deptAndExaminationWithCps1.get(0).setPracticeNum(practiceNum);
            //参考率
            BigDecimal ratio = BigDecimal.valueOf(practiceNum).divide(BigDecimal.valueOf(peopleNum==0?1:peopleNum),2,BigDecimal.ROUND_HALF_UP);
            deptAndExaminationWithCps1.get(0).setRatio(ratio.doubleValue());
            //平均时长
            BigDecimal averageTime = BigDecimal.valueOf(sunTime).divide(BigDecimal.valueOf(tTestpaperStudentWithCps.size()),0,BigDecimal.ROUND_HALF_UP);
            deptAndExaminationWithCps1.get(0).setAverageTime(String.valueOf((averageTime.longValue()/1000)/60));
            //判断分数等级并返回
        }
        return deptAndExaminationWithCps1;
    }

    /**
     * 根据考试id获取当前部门的子部门考试分析数据
     * @param deptAndExaminationWithCp
     */
    public List<DeptAndExaminationWithCp> getDeptByChildInfo(DeptAndExaminationWithCp deptAndExaminationWithCp){
        Long deptId = SecurityUtils.getLoginUser().getUser().getDeptId();
        Long ksid = deptAndExaminationWithCp.getKsid();
        List<DeptAndExaminationWithCp> deptAndExaminationWithCps2 = tExaminationScoreAnalysisMapper.selectDeptAndExaminationWithCpList(deptAndExaminationWithCp);
        List list = new ArrayList();
        Integer countScore = 0;
        Long sunTime = 0L;
        Map map = new HashMap();
        if(deptAndExaminationWithCps2.size()>0){
            for (DeptAndExaminationWithCp d: deptAndExaminationWithCps2) {
                Long childDpetId = d.getDeptId();
                List<TTestpaperStudentWithCp>tTestpaperStudentWithCps2 = tTestpaperWithCpMapper.getExamOfStudentInfo(ksid,childDpetId);
                if(tTestpaperStudentWithCps2.size()>0) {
                    for (TTestpaperStudentWithCp tTestpaperStudentWithCp2 : tTestpaperStudentWithCps2) {
                        list.add(tTestpaperStudentWithCp2.getScore());
                        countScore += tTestpaperStudentWithCp2.getScore();
                        Long startTime = tTestpaperStudentWithCp2.getCreateTime().getTime();
                        Long endTime = tTestpaperStudentWithCp2.getHandTime().getTime();
                        sunTime += (endTime - startTime);
                        //获取考试分数等级并转换为json格式
                        String fsdj = tTestpaperStudentWithCp2.getFsdj();
                        JSONArray json = JSONArray.parseArray(fsdj);
                        int score = tTestpaperStudentWithCp2.getScore();
                        //解析以json形式存储的考试等级
                        for (int i = 0; i < json.size(); i++) {
                            JSONObject js = JSONObject.parseObject(json.getString(i));
                            List ls = Collections.singletonList(js.get("value"));
                            List ls2 = (List) ls.get(0);
                            //System.out.println(ls2);
                            String key = js.get("key").toString();
                            int firstVal = Integer.parseInt(ls2.get(0).toString());
                            int lastVal = Integer.parseInt(ls2.get(1).toString());
                            //判断逻辑：定义一个map，把考试等级遍历出来的等级名称存入map中key，value值默认为0，
                            // 当学员考试分数符合考试等级时，把map中对应的考试等级的key值对应的value值加1
                            if (map.size() > 0) {
                                for (Object mapK : map.keySet()) {
                                    //判断这个循环中获取的考试等级在map中有没有存在、学员考试分数是否符合此等级，如果存在且符合则对应的value加1，
                                    //不存在但等级符合则新增一个对应等级的key-value，value的值为1，
                                    //如果不存在且不符合当前等级，则新增一个对应等级的key-value，value值为0
                                    if(map.containsKey(key)){
                                        if (key.equals(mapK.toString()) && score >= firstVal && score <= lastVal) {
                                            int val = Integer.parseInt(map.get(mapK).toString());
                                            val+=1;
                                            map.put(mapK, val);
                                            continue;
                                        }
                                    }else{
                                        if(score >= firstVal && score <= lastVal){
                                            map.put(key,1);
                                        }else{
                                            map.put(key,0);
                                        }
                                    }
                                }
                            }else if (score >= firstVal && score <= lastVal) {
                                map.put(key,1);
                            }else{
                                map.put(key,0);
                            }
                        }
                        d.setParams(map);
                        //最高得分
                        Integer maxScore = (Integer) Collections.max(list);
                        d.setTopScore(maxScore);
                        //最低得分
                        Integer minScore = (Integer) Collections.min(list);
                        d.setLastScore(minScore);
                        //平均分数
                        BigDecimal averageScore = BigDecimal.valueOf(countScore).divide(BigDecimal.valueOf(tTestpaperStudentWithCps2.size()), 2, BigDecimal.ROUND_HALF_UP);
                        d.setAverageScore(averageScore.doubleValue());
                        //应考人数
                        int peopleNum = tExaminationScoreAnalysisMapper.selectExamNum(ksid, childDpetId);
                        d.setPeopleNum(peopleNum);
                        //实考人数
                        int practiceNum = tExaminationScoreAnalysisMapper.selectPracticeNum(ksid, childDpetId);
                        d.setPracticeNum(practiceNum);
                        //参考率
                        BigDecimal ratio = BigDecimal.valueOf(practiceNum).divide(BigDecimal.valueOf(peopleNum==0?1:peopleNum), 2, BigDecimal.ROUND_HALF_UP);
                        d.setRatio(ratio.doubleValue());
                        //平均时长
                        BigDecimal averageTime = BigDecimal.valueOf(sunTime).divide(BigDecimal.valueOf(tTestpaperStudentWithCps2.size()), 0, BigDecimal.ROUND_HALF_UP);
                        d.setAverageTime(String.valueOf(averageTime.longValue() / 1000 / 60));
                    }
                }
            }
        }
        return deptAndExaminationWithCps2;
    }

    public String getKey(String xxValue){
        String key = null;
        JSONObject obj = JSONObject.parseObject(xxValue);
        for (Map.Entry<String,Object> entry:obj.entrySet() ) {
                if("key".equals(entry.getKey())){
                    key = entry.getValue().toString();
                    System.out.println("取值的key为==========="+key);
                }
        }
        return key;
    }
}
