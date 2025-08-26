package com.sinosoft.etp.cp.curriculum.mapper;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumStudentWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TCurriculumStudentWithCpMapper {

    /**
     * 插入课程与学员关联关系
     * @param sysUsers 学员
     */
    Long insert(@Param("list") List<TCurriculumStudentWithCp> sysUsers);

    /**
     * 根据课程ID查询用户
     * @param kcid 课程ID
     * @return  List<TCurriculumStudent>
     */
    List<TCurriculumStudentWithCp> selectByKcId(@Param("kcid") Long kcid);


    /**
     * 根据课程ID删除任务与学员记录表记录
     * @param kcid 课程ID
     */
    void delRealtionByKcId(@Param("kcid") Long kcid);

    List<TCurriculumStudentWithCp> selectStudnetByKcIdAndXYid(@Param("kcid") Long kcid, @Param("xyid") Long xyid);


    /*************统计分析********************/
    /**
     * 课程完成总的用户数
     * @param kcid
     * @return
     */
    Integer getAllCompletedKcUser(@Param("kcid") Long kcid);

    /**
     * 近七日课程完成人数
     * @param kcid
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> getCompletedKcUserByDate(@Param("kcid") Long kcid,@Param("startTime")Long startTime,@Param("endTime")Long endTime);

    /**
     * 课件总计已完成
     * @param kcid
     * @return
     */
    Integer getCompletedCourse(@Param("kcid") Long kcid);

    /**
     * 已领取课程用户数
     * @param kcid
     * @return
     */
    Integer getReceivedKcUser(@Param("kcid") Long kcid);

    /**
     * 昨日学习课程用户数
     * @param kcid
     * @return
     */
    Integer getYesterdayStudyKc(@Param("kcid") Long kcid);

    /**
     * 课程总计包含课件数
     * @param kcid
     * @return
     */
    Integer getCourseNum(@Param("kcid") Long kcid);

    /**
     * 课程各课件完成情况
     * @param kcid
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> getCompletedKjByDate(@Param("kcid") Long kcid,
                                                  @Param("startTime")String startTime,
                                                  @Param("endTime")String endTime,
                                                  @Param("dates")String[] dates);


    /**
     * 统计分析 -- 各课程课件完成情况
     * @param
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String,Object>> getCompletedKcKjDate(@Param("startTime")String startTime,
                                                  @Param("endTime")String endTime,
                                                  @Param("dates")String[] dates);


    /**
     * 已发布课程数
     * @return
     */
    Integer getFbCurriculum();
}
