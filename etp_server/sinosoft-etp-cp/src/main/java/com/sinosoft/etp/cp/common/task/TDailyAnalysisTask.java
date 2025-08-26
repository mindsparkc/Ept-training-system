package com.sinosoft.etp.cp.common.task;

import com.sinosoft.etp.common.domain.TEventLog;
import com.sinosoft.etp.cp.common.domain.TDailyAnalysisWithCp;
import com.sinosoft.etp.cp.common.mapper.TDailyAnalysisWithCpMapper;
import com.sinosoft.etp.cp.common.mapper.TEventLogMapper;
import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import com.sinosoft.etp.cp.courseware.mapper.TCoursewareWithCpMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时任务：统计每天的事件
 * @author 86151
 */
@Component("dailyAnalysisTask")
public class TDailyAnalysisTask {

    @Resource
    private TEventLogMapper tEventLogMapper;

    @Resource
    private TDailyAnalysisWithCpMapper tDailyAnalysisWithCpMapper;

    @Resource
    private TCoursewareWithCpMapper tCoursewareWithCpMapper;

    /**
     * 获取需要统计的数据并处理
     */
    public void getStatistics(){
        //获取前一天的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,-1);
        //c.add(Calendar.DAY_OF_YEAR,-1);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.HOUR_OF_DAY,0);
        Date day = c.getTime();
        String date = sdf.format(day);
        //获取每日课件点击次数统计，新增至统计表
        List<TEventLog> kjList = tEventLogMapper.selectByKj(date,"CLI_COUR");
        for (int i = 0; i < kjList.size(); i++) {
            System.out.println("遍历list = "+ kjList.get(i));
            TDailyAnalysisWithCp tDailyAnalysisWithCp = new TDailyAnalysisWithCp();
            tDailyAnalysisWithCp.setKjid(kjList.get(i).getKjid());
            tDailyAnalysisWithCp.setDailyCoursewareClicks(kjList.get(i).getCounts());
            tDailyAnalysisWithCp.setCreateTime(day);
            tDailyAnalysisWithCp.setSpecificDate(date);
            tDailyAnalysisWithCpMapper.insertTDailyAnalysis(tDailyAnalysisWithCp);
        }
        //获取每日课程点击次数统计，新增至统计表
        List<TEventLog> kcList = tEventLogMapper.selectByKc(date,"CLI_CURR");
        for (int i = 0; i < kcList.size(); i++) {
            TDailyAnalysisWithCp tDailyAnalysisWithCp = new TDailyAnalysisWithCp();
            tDailyAnalysisWithCp.setKcid(kcList.get(i).getKcid());
            tDailyAnalysisWithCp.setDailyCurriculumClicks(kcList.get(i).getCounts());
            tDailyAnalysisWithCp.setCreateTime(day);
            tDailyAnalysisWithCp.setSpecificDate(date);
            tDailyAnalysisWithCpMapper.insertTDailyAnalysis(tDailyAnalysisWithCp);
        }
        //获取每日任务点击次数统计，新增至统计表
        List<TEventLog> rwList = tEventLogMapper.selectByKc(date,"CLI_CURR");
        for (int i = 0; i < rwList.size(); i++) {
            TDailyAnalysisWithCp tDailyAnalysisWithCp = new TDailyAnalysisWithCp();
            tDailyAnalysisWithCp.setRwid(rwList.get(i).getRwid());
            tDailyAnalysisWithCp.setDailyCurriculumClicks(rwList.get(i).getCounts());
            tDailyAnalysisWithCp.setCreateTime(day);
            tDailyAnalysisWithCp.setSpecificDate(date);
            tDailyAnalysisWithCpMapper.insertTDailyAnalysis(tDailyAnalysisWithCp);
        }
        //获取每日学员登录次数统计，新增至统计表
        //获取不同课件类型的学习时长
        List<TEventLog> studyList = tEventLogMapper.selectStudyByKj(date,"ING_STUDY");
        for (TEventLog tEventLog : studyList) {
            //根据课件id查询课件类型
            Long kjid = tEventLog.getKjid();
            TCoursewareWithCp tCoursewareWithCp = tCoursewareWithCpMapper.selectTCoursewareById(kjid);
            if("1".equals(tCoursewareWithCp.getXs())){
                TDailyAnalysisWithCp tDailyAnalysisWithCp = new TDailyAnalysisWithCp();
                tDailyAnalysisWithCp.setUserId(tEventLog.getUserId());
                tDailyAnalysisWithCp.setKjid(tEventLog.getKjid());
                tDailyAnalysisWithCp.setDailyFileStudyTime(tEventLog.getStudyDuration());
                tDailyAnalysisWithCp.setCreateTime(day);
                tDailyAnalysisWithCp.setSpecificDate(date);
                tDailyAnalysisWithCpMapper.insertTDailyAnalysis(tDailyAnalysisWithCp);
            }else if("2".equals(tCoursewareWithCp.getXs())){
                TDailyAnalysisWithCp tDailyAnalysisWithCp = new TDailyAnalysisWithCp();
                tDailyAnalysisWithCp.setUserId(tEventLog.getUserId());
                tDailyAnalysisWithCp.setKjid(tEventLog.getKjid());
                tDailyAnalysisWithCp.setDailyFileStudyTime(tEventLog.getStudyDuration());
                tDailyAnalysisWithCp.setCreateTime(day);
                tDailyAnalysisWithCp.setSpecificDate(date);
                tDailyAnalysisWithCpMapper.insertTDailyAnalysis(tDailyAnalysisWithCp);
            }else if("3".equals(tCoursewareWithCp.getXs())){
                TDailyAnalysisWithCp tDailyAnalysisWithCp = new TDailyAnalysisWithCp();
                tDailyAnalysisWithCp.setUserId(tEventLog.getUserId());
                tDailyAnalysisWithCp.setKjid(tEventLog.getKjid());
                tDailyAnalysisWithCp.setDailyFileStudyTime(tEventLog.getStudyDuration());
                tDailyAnalysisWithCp.setCreateTime(day);
                tDailyAnalysisWithCp.setSpecificDate(date);
                tDailyAnalysisWithCpMapper.insertTDailyAnalysis(tDailyAnalysisWithCp);
            }
        }
    }

}
