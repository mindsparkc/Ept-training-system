package com.sinosoft.etp.cp.questionnaire.service;

import com.sinosoft.etp.cp.questionnaire.domain.QuestionnaireTask;
import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireWithCp;

import java.util.List;

/**
 * 问卷调查Service接口
 *
 *
 * @date 2022-02-14
 */
public interface ITQuestionnaireWithCpService
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
     * 批量删除问卷调查
     *
     * @param ids 需要删除的问卷调查ID
     * @return 结果
     */
    public int deleteTQuestionnaireByIds(Long[] ids);

    /**
     * 删除问卷调查信息
     *
     * @param id 问卷调查ID
     * @return 结果
     */
    public int deleteTQuestionnaireById(Long id);

    /**
     * 撤回问卷调查
     * @param id
     * @return
     */
    int back(String id);

    public int getTotalByWjid(Long wjid);

    List<TQuestionnaireWithCp> selectTQuestionnaireStudentList(TQuestionnaireWithCp tQuestionnaire);

    /**
     * 问卷各个选择题题目分析
     * @return
     */
    List<QuestionnaireTask>getChoseAnalysis(Long wjid);
    /**
     * 问卷问答题 题目分析
     * @return
     */
    List<QuestionnaireTask>getWdAnalysis(Long wjid,String tmid);
}


