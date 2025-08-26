package com.sinosoft.etp.up.questionnaire.service;

import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaire;

import java.util.List;
import java.util.Map;

/**
 * 问卷调查Service接口
 *
 *
 * @date 2022-02-16
 */
public interface ITQuestionnaireService
{
    /**
     * 查询问卷调查
     *
     * @param id 问卷调查ID
     * @return 问卷调查
     */
    public TQuestionnaire selectTQuestionnaireById(Long id);

    /**
     * 查询问卷调查列表
     *
     * @param tQuestionnaire 问卷调查
     * @return 问卷调查集合
     */
    public List<TQuestionnaire> selectTQuestionnaireList(TQuestionnaire tQuestionnaire);

    /**
     * 新增问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    public int insertTQuestionnaire(TQuestionnaire tQuestionnaire);

    /**
     * 修改问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    public int updateTQuestionnaire(TQuestionnaire tQuestionnaire);

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
     * 获取首页问卷
     * @return
     */
    public Map<String,Object> getHomeQuestionnaire();
}
