package com.sinosoft.etp.up.curriculum.service;

import com.sinosoft.etp.up.curriculum.domain.TCurriculum;
import com.sinosoft.etp.up.curriculum.domain.TCurriculumContent;

import java.util.List;
import java.util.Map;

/**
 * curriculumService接口
 *
 *
 * @date 2021-11-15
 */
public interface ITCurriculumService
{
    /**
     * 查询curriculum
     *
     * @param id curriculumID
     * @return curriculum
     */
    public TCurriculum selectTCurriculumById(Long id);

    /**
     * 查询curriculum列表
     *
     * @param tCurriculum curriculum
     * @return curriculum集合
     */
    public List<TCurriculum> selectTCurriculumList(TCurriculum tCurriculum);
    public List<TCurriculum> selectTjCurriculumList(TCurriculum tCurriculum);



    /**
     * 2021-11-26 修改获取课程内容项
     * @param id
     * @return
     */
    List<TCurriculumContent> getTCurriculumContentDetailByCid(Long id);

    List<Map<String,Object>> getCurriculumTags();

    /**
     * 知识库信息
     * @return
     */
    List<TCurriculum> getKnowledgeInfo(TCurriculum tCurriculum);

    /**
     * 修改课程学习状态
     * @param
     * @return
     */
    public Double updateStatus(Long cid,Long kjid,String contentIds);

    /**
     * 根据kcid和xyid获取课程信息及进度
     */
    public TCurriculum selectTCurriculumByIds(Long id,Long rwid);

    public Long showInitContent(Long kcid);
}
