package com.sinosoft.etp.cp.questionnaire.mapper;

import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireCompanyWithCp;

import java.util.List;

/**
 * 问卷调查和企业关联表Mapper接口
 *
 *
 * @date 2022-02-15
 */
public interface TQuestionnaireCompanyWithCpMapper
{
    /**
     * 查询问卷调查和企业关联表
     *
     * @param id 问卷调查和企业关联表ID
     * @return 问卷调查和企业关联表
     */
    public TQuestionnaireCompanyWithCp selectTQuestionnaireCompanyById(Long id);

    /**
     * 查询问卷调查和企业关联表列表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 问卷调查和企业关联表集合
     */
    public List<TQuestionnaireCompanyWithCp> selectTQuestionnaireCompanyList(TQuestionnaireCompanyWithCp tQuestionnaireCompany);

    /**
     * 新增问卷调查和企业关联表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 结果
     */
    public int insertTQuestionnaireCompany(TQuestionnaireCompanyWithCp tQuestionnaireCompany);

    /**
     * 修改问卷调查和企业关联表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 结果
     */
    public int updateTQuestionnaireCompany(TQuestionnaireCompanyWithCp tQuestionnaireCompany);

    /**
     * 删除问卷调查和企业关联表
     *
     * @param id 问卷调查和企业关联表ID
     * @return 结果
     */
    public int deleteTQuestionnaireCompanyById(Long id);

    /**
     * 批量删除问卷调查和企业关联表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTQuestionnaireCompanyByIds(Long[] ids);
}
