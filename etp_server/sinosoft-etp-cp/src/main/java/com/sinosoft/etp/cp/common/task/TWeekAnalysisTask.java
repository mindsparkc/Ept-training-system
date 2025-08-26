package com.sinosoft.etp.cp.common.task;

import com.sinosoft.etp.cp.common.domain.TAnalysisCountWithCp;
import com.sinosoft.etp.cp.common.domain.TDailyAnalysisWithCp;
import com.sinosoft.etp.cp.common.mapper.TAnalysisCountWithCpMapper;
import com.sinosoft.etp.cp.common.mapper.TDailyAnalysisWithCpMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 定时任务：统计近7天的数据
 * @author 86151
 */
@Component("weekAnalysisTask")
public class TWeekAnalysisTask {
    @Resource
    private TDailyAnalysisWithCpMapper tDailyAnalysisWithCpMapper;
    @Resource
    private TAnalysisCountWithCpMapper tAnalysisCountWithCpMapper;


    public void weekCount(){
        //获取统计范围时间
        Calendar c = Calendar.getInstance();
        Date lastTime = c.getTime();
        c.add(Calendar.DAY_OF_YEAR,-7);
        Date fristTime = c.getTime();
        Map map = new HashMap<String,Object>();
        map.put("fristTime",fristTime);
        map.put("lastTime",lastTime);
        int kj = insertCourCount(map,lastTime);
        int kc = insertCurrCount(map,lastTime);
        int rw = insertTaskCount(map,lastTime);
        int stydy = insertStudyTimeCount(map,lastTime);
        int video = insertVideTimeCount(map,lastTime);
        int text = insertTextTimeCount(map,lastTime);
        int file = insertFileTimeCount(map,lastTime);
        System.out.println("共统计课件："+kj+"条，课程："+kc+"条，任务："+rw+"条；用户学习时长共："+stydy+
                "条，视频类学习时长："+video+"条，富文本类学习时长："+text+"条，文件类学习时长："+file+"条");
    }

    /**
     * 查询课件近七天点击次数并保存
     * @param map
     */
    public int insertCourCount(Map map,Date timeStamp){
        int i = 0;
        TAnalysisCountWithCp tAnalysisCountWithCp = new TAnalysisCountWithCp();
        List<TDailyAnalysisWithCp> tDailyAnalysisWithCps = tDailyAnalysisWithCpMapper.selectWeekByKj(map);
        for (TDailyAnalysisWithCp tDailyAnalysisWithCp : tDailyAnalysisWithCps) {
            tAnalysisCountWithCp.setKjid(tDailyAnalysisWithCp.getKjid());
            tAnalysisCountWithCp.setCoursewareClickCount(tDailyAnalysisWithCp.getDailyCoursewareClicks().longValue());
            tAnalysisCountWithCp.setCreateTime(timeStamp);
            i += tAnalysisCountWithCpMapper.insertTAnalysisCount(tAnalysisCountWithCp);
        }
        return i;
    }

    /**
     * 查询课程近七天点击次数并保存
     * @param map
     */
    public int insertCurrCount(Map map,Date timeStamp){
        int i = 0;
        TAnalysisCountWithCp tAnalysisCountWithCp = new TAnalysisCountWithCp();
        List<TDailyAnalysisWithCp> tDailyAnalysisWithCps = tDailyAnalysisWithCpMapper.selectWeekByKc(map);
        for (TDailyAnalysisWithCp tDailyAnalysisWithCp:tDailyAnalysisWithCps) {
            tAnalysisCountWithCp.setKcid(tDailyAnalysisWithCp.getKcid());
            tAnalysisCountWithCp.setCurriculumClickCount(tDailyAnalysisWithCp.getDailyCurriculumClicks().longValue());
            tAnalysisCountWithCp.setCreateTime(timeStamp);
            i += tAnalysisCountWithCpMapper.insertTAnalysisCount(tAnalysisCountWithCp);
        }
        return i;
    }

    /**
     * 查询任务近七天点击次数并保存
     * @param map
     */
    public int insertTaskCount(Map map,Date timeStamp){
        int i = 0;
        TAnalysisCountWithCp tAnalysisCountWithCp = new TAnalysisCountWithCp();
        List<TDailyAnalysisWithCp> tDailyAnalysisWithCps = tDailyAnalysisWithCpMapper.selectWeekByRw(map);
        for(TDailyAnalysisWithCp tDailyAnalysisWithCp : tDailyAnalysisWithCps){
            tAnalysisCountWithCp.setRwid(tDailyAnalysisWithCp.getRwid());
            tAnalysisCountWithCp.setTaskClickCount(tDailyAnalysisWithCp.getDailyTaskClicks().longValue());
            tAnalysisCountWithCp.setCreateTime(timeStamp);
            i += tAnalysisCountWithCpMapper.insertTAnalysisCount(tAnalysisCountWithCp);
        }
        return i;
    }

    /**
     * 获取近七天用户的学习时长
     * @param map
     * @param timeStamp
     * @return
     */
    public int insertStudyTimeCount(Map map,Date timeStamp){
        int i = 0;
        TAnalysisCountWithCp tAnalysisCountWithCp = new TAnalysisCountWithCp();
        List<TDailyAnalysisWithCp> tDailyAnalysisWithCps = tDailyAnalysisWithCpMapper.selectWeekByStudy(map);
        for (TDailyAnalysisWithCp tDailyAnalysisWithCp:tDailyAnalysisWithCps) {
            tAnalysisCountWithCp.setUserId(tDailyAnalysisWithCp.getUserId());
            tAnalysisCountWithCp.setStudyTimeCount(Long.parseLong(tDailyAnalysisWithCp.getDailyUserStudyTime()));
            tAnalysisCountWithCp.setCreateTime(timeStamp);
            tAnalysisCountWithCpMapper.insertTAnalysisCount(tAnalysisCountWithCp);
        }
        return i;
    }

    /**
     * 获取近七天的视频类课件的学习时长（粒度到用户）
     * @param map
     * @param timeStamp
     * @return
     */
    public int insertVideTimeCount(Map map,Date timeStamp){
        int i = 0;
        TAnalysisCountWithCp tAnalysisCountWithCp = new TAnalysisCountWithCp();
        List<TDailyAnalysisWithCp> tDailyAnalysisWithCps = tDailyAnalysisWithCpMapper.selectWeekByStudy(map);
        for (TDailyAnalysisWithCp tDailyAnalysisWithCp:tDailyAnalysisWithCps) {
            tAnalysisCountWithCp.setUserId(tDailyAnalysisWithCp.getUserId());
            tAnalysisCountWithCp.setVideoTimeCount(Long.parseLong(tDailyAnalysisWithCp.getDailyVideoStudyTime()));
            tAnalysisCountWithCp.setCreateTime(timeStamp);
            tAnalysisCountWithCpMapper.insertTAnalysisCount(tAnalysisCountWithCp);
        }
        return i;
    }

    /**
     * 获取近七天的富文本类的课件学习时长（粒度到用户）
     * @param map
     * @param timeStamp
     * @return
     */
    public int insertTextTimeCount(Map map,Date timeStamp){
        int i = 0;
        TAnalysisCountWithCp tAnalysisCountWithCp = new TAnalysisCountWithCp();
        List<TDailyAnalysisWithCp> tDailyAnalysisWithCps = tDailyAnalysisWithCpMapper.selectWeekByStudy(map);
        for (TDailyAnalysisWithCp tDailyAnalysisWithCp:tDailyAnalysisWithCps) {
            tAnalysisCountWithCp.setUserId(tDailyAnalysisWithCp.getUserId());
            tAnalysisCountWithCp.setTextTimeCount(Long.parseLong(tDailyAnalysisWithCp.getDailyRichtextStudyTime()));
            tAnalysisCountWithCp.setCreateTime(timeStamp);
            tAnalysisCountWithCpMapper.insertTAnalysisCount(tAnalysisCountWithCp);
        }
        return i;
    }

    /**
     * 获取近七天的文件类课件学习时长（粒度到用户）
     * @param map
     * @param timeStamp
     * @return
     */
    public int insertFileTimeCount(Map map,Date timeStamp){
        int i = 0;
        TAnalysisCountWithCp tAnalysisCountWithCp = new TAnalysisCountWithCp();
        List<TDailyAnalysisWithCp> tDailyAnalysisWithCps = tDailyAnalysisWithCpMapper.selectWeekByStudy(map);
        for (TDailyAnalysisWithCp tDailyAnalysisWithCp:tDailyAnalysisWithCps) {
            tAnalysisCountWithCp.setUserId(tDailyAnalysisWithCp.getUserId());
            tAnalysisCountWithCp.setFileTimeCount(Long.parseLong(tDailyAnalysisWithCp.getDailyFileStudyTime()));
            tAnalysisCountWithCp.setCreateTime(timeStamp);
            tAnalysisCountWithCpMapper.insertTAnalysisCount(tAnalysisCountWithCp);
        }
        return i;
    }

}
