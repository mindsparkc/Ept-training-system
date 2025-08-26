package com.sinosoft.etp.up.questionnaire.service;

import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaireTag;

import java.util.List;

/**
 * 问卷调查和标签关联表Service接口
 *
 * 
 * @date 2022-02-16
 */
public interface ITQuestionnaireTagService
{
    /**
     * 查询问卷调查和标签关联表
     *
     * @param id 问卷调查和标签关联表ID
     * @return 问卷调查和标签关联表
     */
    public TQuestionnaireTag selectTQuestionnaireTagById(Long id);

    /**
     * 查询问卷调查和标签关联表列表
     *
     * @param tQuestionnaireTag 问卷调查和标签关联表
     * @return 问卷调查和标签关联表集合
     */
    public List<TQuestionnaireTag> selectTQuestionnaireTagList(TQuestionnaireTag tQuestionnaireTag);

    /**
     * 新增问卷调查和标签关联表
     *
     * @param tQuestionnaireTag 问卷调查和标签关联表
     * @return 结果
     */
    public int insertTQuestionnaireTag(TQuestionnaireTag tQuestionnaireTag);

    /**
     * 修改问卷调查和标签关联表
     *
     * @param tQuestionnaireTag 问卷调查和标签关联表
     * @return 结果
     */
    public int updateTQuestionnaireTag(TQuestionnaireTag tQuestionnaireTag);

    /**
     * 批量删除问卷调查和标签关联表
     *
     * @param ids 需要删除的问卷调查和标签关联表ID
     * @return 结果
     */
    public int deleteTQuestionnaireTagByIds(Long[] ids);

    /**
     * 删除问卷调查和标签关联表信息
     *
     * @param id 问卷调查和标签关联表ID
     * @return 结果
     */
    public int deleteTQuestionnaireTagById(Long id);
}
