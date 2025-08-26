package com.sinosoft.etp.cp.curriculum.service;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumContentWithCp;
import com.sinosoft.etp.cp.curriculum.domain.TCurriculumWithCp;

import java.util.List;
import java.util.Map;

/**
 * curriculumService接口
 *
 *
 * @date 2021-11-15
 */
public interface ITCurriculumWithCpService
{
    /**
     * 查询curriculum
     *
     * @param id curriculumID
     * @return curriculum
     */
    public TCurriculumWithCp selectTCurriculumById(Long id);

    /**
     * 查询curriculum列表
     *
     * @param tCurriculum curriculum
     * @return curriculum集合
     */
    public List<TCurriculumWithCp> selectTCurriculumList(TCurriculumWithCp tCurriculum);

    /**
     * 新增curriculum
     *
     * @param tCurriculum curriculum
     * @return 结果
     */
    public Long insertTCurriculum(TCurriculumWithCp tCurriculum);

    /**
     * 修改curriculum
     *
     * @param tCurriculum curriculum
     * @return 结果
     */
    public int updateTCurriculum(TCurriculumWithCp tCurriculum);

    public int updateTCurriculumTjzt(TCurriculumWithCp tCurriculum);

    /**
     * 批量删除curriculum
     *
     * @param ids 需要删除的curriculumID
     * @return 结果
     */
    public int deleteTCurriculumByIds(Long[] ids);

    /**
     * 删除curriculum信息
     *
     * @param id curriculumID
     * @return 结果
     */
    public int deleteTCurriculumById(Long id);

    /**
     * 撤回已发布的课程
     * @param id 课程ID
     */
    void pushBack(Long id);

    /**
     * 发布课程
     * @param id 课程ID
     */
    void push(Long id);

    /**
     * 分步骤设置课程
     * @param tCurriculum
     * @return
     */
    Long completeTCurriculum(TCurriculumWithCp tCurriculum);

    /**
     * 2021-11-26 修改获取课程内容项
     * @param id
     * @return
     */
    List<TCurriculumContentWithCp> getTCurriculumContentDetailByCid(Long id);



    /***************************统计分析************************/

    /**
     * 课程完成总的用户数
     * @param kcid
     * @return
     */
    public Integer getAllCompletedKcUser(Long kcid);

    /**
     * 近七日课程完成人数
     * @param kcid
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String,Object>> getCompletedKcUserByDate(Long kcid, Long startTime, Long endTime);

    /**
     * 课件总计已完成
     * @param kcid
     * @return
     */
    public Integer getCompletedCourse(Long kcid);

    /**
     * 已领取课程用户数
     * @param kcid
     * @return
     */
    public Integer getReceivedKcUser(Long kcid);

    /**
     * 昨日学习课程用户数
     * @param kcid
     * @return
     */
    public Integer getYesterdayStudyKc(Long kcid);

    /**
     * 课程总计包含课件数
     * @param kcid
     * @return
     */
    public Integer getCourseNum(Long kcid);

    /**
     * 课程各课件完成情况
     * @param kcid
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String,Object>> getCompletedKjByDate(Long kcid,String startTime,String endTime,String[] dates);

    /**
     * 各课程课件完成情况
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Map<String,Object>> getCompletedKcKjDate(String startTime,String endTime,String[] dates);

    /**
     * 已发布课程数
     * @return
     */
    public Integer getFbCurriculum();

    public boolean checkExistKC(TCurriculumWithCp curriculum);

    //***************统计分析**************************

    public int getKcLearnTime(Long kcid);

    public List<Map<String,Object>> getKcListByKjTime(Long kcid);
    public List<Map<String,Object>> getEveKcLearnTime();
}
