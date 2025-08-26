package com.sinosoft.etp.cp.testpaper.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.alibaba.fastjson.JSON;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.testpaper.domain.TTestpaperSubjectWithCp;
import com.sinosoft.etp.cp.testpaper.domain.TTestpaperWithCp;
import com.sinosoft.etp.cp.testpaper.mapper.TTestpaperSubjectWithCpMapper;
import com.sinosoft.etp.cp.testpaper.mapper.TTestpaperWithCpMapper;
import com.sinosoft.etp.cp.testpaper.service.ITTestpaperSubjectWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 试卷关联题目Service业务层处理
 *
 *
 * @date 2021-12-17
 */
@Service
public class TTestpaperSubjectWithCpServiceImpl implements ITTestpaperSubjectWithCpService
{
    @Autowired
    private TTestpaperSubjectWithCpMapper tTestpaperSubjectMapper;
    @Autowired
    private TTestpaperWithCpMapper tTestpaperMapper;
    /**
     * 查询试卷关联题目
     *
     * @param id 试卷关联题目ID
     * @return 试卷关联题目
     */
    @Override
    public TTestpaperSubjectWithCp selectTTestpaperSubjectById(Long id)
    {
        return tTestpaperSubjectMapper.selectTTestpaperSubjectById(id);
    }

    /**
     * 查询试卷关联题目列表
     *
     * @param tTestpaperSubject 试卷关联题目
     * @return 试卷关联题目
     */
    @Override
    public List<TTestpaperSubjectWithCp> selectTTestpaperSubjectList(TTestpaperSubjectWithCp tTestpaperSubject)
    {
        return tTestpaperSubjectMapper.selectTTestpaperSubjectList(tTestpaperSubject);
    }

    /**
     * 新增试卷关联题目
     *
     * @param tTestpaperSubject 试卷关联题目
     * @return 结果
     */
    @Override
    public int insertTTestpaperSubject(TTestpaperSubjectWithCp tTestpaperSubject)
    {
        tTestpaperSubject.setCreateTime(DateUtils.getNowDate());
        return tTestpaperSubjectMapper.insertTTestpaperSubject(tTestpaperSubject);
    }
    /**
     * 批量新增试卷关联题目
     *
     * @param list 试卷关联题目
     * @return 结果
     */
    @Override
    public int insertTTestpaperSubjects(List<TTestpaperSubjectWithCp> list)
    {
        return tTestpaperSubjectMapper.insertTTestpaperSubjects(list);
    }

    /**
     * 修改试卷关联题目
     *
     * @param tTestpaperSubject 试卷关联题目
     * @return 结果
     */
    @Override
    public int updateTTestpaperSubject(TTestpaperSubjectWithCp tTestpaperSubject)
    {
        tTestpaperSubject.setUpdateTime(DateUtils.getNowDate());
        return tTestpaperSubjectMapper.updateTTestpaperSubject(tTestpaperSubject);
    }

    /**
     * 批量删除试卷关联题目
     *
     * @param ids 需要删除的试卷关联题目ID
     * @return 结果
     */
    @Override
    public int deleteTTestpaperSubjectByIds(Long[] ids)
    {
        return tTestpaperSubjectMapper.deleteTTestpaperSubjectByIds(ids);
    }

    /**
     * 删除试卷关联题目信息
     *
     * @param id 试卷关联题目ID
     * @return 结果
     */
    @Override
    public int deleteTTestpaperSubjectById(Long id)
    {
        return tTestpaperSubjectMapper.deleteTTestpaperSubjectById(id);
    }
    @Override
    public int deleteTestpaperSubjectBySjid(Long sjid)
    {
        return tTestpaperSubjectMapper.deleteTestpaperSubjectBySjid(sjid);
    }
    @Override
    public List<Map<String,Object>> getSubjectByPaper(Long sjid)
    {
        return tTestpaperSubjectMapper.getSubjectByPaper(sjid);
    }
    @Override
    public List<Map<String,Object>> getPaperSubjectTags(Long sjid)
    {
        return tTestpaperSubjectMapper.getPaperSubjectTags(sjid);
    }
    @Override
    @Transactional
    public int updateScore(Long sjid,String type,String score,String fb)
    {
        if(null!=score){
            tTestpaperSubjectMapper.updateScore(sjid,type,score);
        }
        int i = updatePaper(sjid,fb);
        return i;
    }

    //更新试卷发布状态信息
    public int updatePaper(Long sjid,String fb){
        TTestpaperWithCp testpaper = new TTestpaperWithCp();
        testpaper.setId(sjid);
        testpaper.setFb(fb);
        testpaper.setUpdateBy(SecurityUtils.getUsername());
        testpaper.setUpdateTime(new Date());
        return tTestpaperMapper.updateTTestpaper(testpaper);
    }
    @Override
    public List<Map<String,Object>> selectScoreByType(Long sjid)
    {
        return tTestpaperSubjectMapper.selectScoreByType(sjid);
    }

    @Override
    public int updateDa(Map<String,Object> params) {
        int res = 0;
        String id = params.get("id").toString();
        Map<String,Object>tkPf = new HashMap<>();
        Map<String,Object>zgPf = new HashMap<>();
        tkPf = (Map) params.get("tkpf");
        int tkScore = 0;
        for(Map.Entry<String,Object>entry : tkPf.entrySet()){
            String colName = "logicalScore";
            String val = entry.getValue().toString();
            String key = entry.getKey();
            String first = "$.sj.\"";
            String center = key+"\"";
            String end = "."+colName+"";
            String url = first+center+end;
            System.out.println("==============url = "+url);
            tTestpaperSubjectMapper.updateDa(id,url,val);
            tkScore+=Integer.parseInt(val);
        }

        zgPf = (Map) params.get("zgpf");
        int zgScore = 0;
        for(Map.Entry<String,Object>entry : zgPf.entrySet()){
            String colName = "logicalScore";
            String val = entry.getValue().toString();
            String key = entry.getKey();
            String first = "$.sj.\"";
            String center = key+"\"";
            String end = "."+colName+"";
            String url = first+center+end;
            System.out.println("==============url = "+url);
            tTestpaperSubjectMapper.updateDa(id,url,val);
            zgScore+=Integer.parseInt(val);
        }
        //获取当前学员得分，把主观题得分加上
        int score = zgScore + tkScore;
        String xyScore = JSON.parse(tTestpaperSubjectMapper.getScore(id)).toString();
        if(!"0".equals(xyScore)){
            score = score + Integer.parseInt(xyScore);
        }
        String scoreUrl = "$.detail.\"score\"";
        System.out.println("==================  scoreUrl = "+ scoreUrl);
        tTestpaperSubjectMapper.updateDaScore(id,scoreUrl, String.valueOf(score));

        //pf.forEach((k, value) -> {
        //    System.out.println("------------- key="+k+"------value="+value);
        //    String colName = "logicalScore";
        //    String val = value.toString();
        //    String key = k.toString();
        //    String first = "$.sj.\"";
        //    String center = key+"\"";
        //    String end = "."+colName+"";
        //    String url = first+center+end;
        //    System.out.println("==============url = "+url);
        //    tTestpaperSubjectMapper.updateDa(id,url,val);
        //
        //});

        Map py = (Map)params.get("py");
        py.forEach((j, values) -> {
            System.out.println("------------- key="+j+"------value="+values);
            String colName = "py";
            String val = values.toString();
            String key = j.toString();
            String first = "$.sj.\"";
            String center = key+"\"";
            String end = "."+colName+"";
            String url = first+center+end;
            System.out.println("==============url = "+url);
            tTestpaperSubjectMapper.updateDa(id,url,val);
        });
        String userName = SecurityUtils.getLoginUser().getUser().getUserName();
        res = tTestpaperSubjectMapper.updatePjzt(id,userName);
        return res;
    }

}
