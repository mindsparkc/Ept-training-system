package com.sinosoft.etp.cp.questionnaire.mapper;

import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireWithCp;

import java.util.List;

/**
 * 问卷调查Mapper接口
 *
 *
 * @date 2022-02-14
 */
public interface TQuestionnaireWithCpMapper
{
    /**
     * 查询问卷调查
     *
     * @param id 问卷调查ID
     * @return 问卷调查
     */
    public TQuestionnaireWithCp selectTQuestionnaireById(Long id);

    /**
     * 查询问卷调查列表
     *
     * @param tQuestionnaire 问卷调查
     * @return 问卷调查集合
     */
    public List<TQuestionnaireWithCp> selectTQuestionnaireList(TQuestionnaireWithCp tQuestionnaire);

    /**
     * 新增问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    public Long insertTQuestionnaire(TQuestionnaireWithCp tQuestionnaire);

    /**
     * 修改问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    public int updateTQuestionnaire(TQuestionnaireWithCp tQuestionnaire);

    /**
     * 删除问卷调查
     *
     * @param id 问卷调查ID
     * @return 结果
     */
    public int deleteTQuestionnaireById(Long id);

    /**
     * 批量删除问卷调查
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTQuestionnaireByIds(Long[] ids);

    /**
     * 撤回问卷调查
     * @param id
     * @return
     */
    int back(String id);

    public List<TQuestionnaireWithCp> selectTQuestionnaireStudentList(TQuestionnaireWithCp tQuestionnaire);
}
