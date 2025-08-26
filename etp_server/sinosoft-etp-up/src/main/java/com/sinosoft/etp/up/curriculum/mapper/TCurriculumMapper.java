package com.sinosoft.etp.up.curriculum.mapper;

import com.sinosoft.etp.up.curriculum.domain.TCurriculum;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * curriculumMapper接口
 *
 *
 * @date 2021-11-15
 */
public interface TCurriculumMapper
{
    /**
     * 查询curriculum
     *
     * @param id curriculumID
     * @return curriculum
     */
    public TCurriculum selectTCurriculumById(@Param("id")Long id,@Param("xyid")Long xyid);

    /**
     * 查询curriculum列表
     *
     * @param tCurriculum curriculum
     * @return curriculum集合
     */
    public List<TCurriculum> selectTCurriculumList(TCurriculum tCurriculum);

    /**
     * 查询用户最近领取课程
     * @param tCurriculum
     * @return tCurriculum集合
     */
    public List<TCurriculum> selectTCurriculumByGet(TCurriculum tCurriculum);


    /**
     * 查询课程标签
     * @param qyid
     * @return
     */
    public List<Map<String,Object>> getCurriculumTags();

    /**
     * 首页获取课程
     * @param xyid
     * @return
     */
    public List<Map<String,Object>> getHomeCurriculum(@Param("xyid")Long xyid,@Param("num")Long num);

    /**
     * 知识库信息
     * @param tCurriculum
     * @return
     */
    public List<TCurriculum> getKnowledgeInfo(TCurriculum tCurriculum);

    /**
     * 根据id修改课程学习状态
     * @param map
     * @return
     */
    public int updateStatus(Map map);

    /**
     * 根据kcid和学员id获取课程信息及进度
     */
    public TCurriculum selectTCurriculumByIds(Map map);

    public List<Map<String,Object>> getHomeHotKc();


    /**
     * 推荐课程
     * @param tCurriculum
     * @return
     */
    public List<TCurriculum> selectTjCurriculumList(TCurriculum tCurriculum);

    /**
     * 获取课程内容
     *  @param xyid
     *  @param kcids
     * @return
     */
    public List<Map<String,Object>> selectKcContent(@Param("xyid") Long xyid , @Param("kcids") Long[] kcids);

    /**
     * 获取未完成的课程个数
     * @param xyid 学员id
     * @return
     */
    public int getKcNotCompletedNum(@Param("xyid") Long xyid);

    /**
     * 已领取课程个数
     * @param xyid 学员id
     * @return
     */
    public int getKcYlqNum(@Param("xyid") Long xyid);

    /**
     * 最近学习的课程
     * @param xyid
     * @return
     */
    public TCurriculum getLastStudy(@Param("xyid")Long xyid);
}
