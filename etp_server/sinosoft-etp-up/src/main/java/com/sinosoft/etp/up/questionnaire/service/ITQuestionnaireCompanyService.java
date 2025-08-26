package com.sinosoft.etp.up.questionnaire.service;

import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaireCompany;

import java.util.List;

/**
 * 问卷调查和企业关联表Service接口
 *
 *
 * @date 2022-02-16
 */
public interface ITQuestionnaireCompanyService
{
    /**
     * 查询问卷调查和企业关联表
     *
     * @param id 问卷调查和企业关联表ID
     * @return 问卷调查和企业关联表
     */
    public TQuestionnaireCompany selectTQuestionnaireCompanyById(Long id);

    /**
     * 查询问卷调查和企业关联表列表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 问卷调查和企业关联表集合
     */
    public List<TQuestionnaireCompany> selectTQuestionnaireCompanyList(TQuestionnaireCompany tQuestionnaireCompany);

    /**
     * 新增问卷调查和企业关联表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 结果
     */
    public int insertTQuestionnaireCompany(TQuestionnaireCompany tQuestionnaireCompany);

    /**
     * 修改问卷调查和企业关联表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 结果
     */
    public int updateTQuestionnaireCompany(TQuestionnaireCompany tQuestionnaireCompany);

    /**
     * 批量删除问卷调查和企业关联表
     *
     * @param ids 需要删除的问卷调查和企业关联表ID
     * @return 结果
     */
    public int deleteTQuestionnaireCompanyByIds(Long[] ids);

    /**
     * 删除问卷调查和企业关联表信息
     *
     * @param id 问卷调查和企业关联表ID
     * @return 结果
     */
    public int deleteTQuestionnaireCompanyById(Long id);
}
