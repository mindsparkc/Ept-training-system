package com.sinosoft.etp.cp.questionnaire.service.impl;

import com.sinosoft.etp.cp.questionnaire.domain.TQuestionnaireCompanyWithCp;
import com.sinosoft.etp.cp.questionnaire.mapper.TQuestionnaireCompanyWithCpMapper;
import com.sinosoft.etp.cp.questionnaire.service.ITQuestionnaireCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问卷调查和企业关联表Service业务层处理
 *
 *
 * @date 2022-02-15
 */
@Service
public class TQuestionnaireCompanyWithCpServiceImpl implements ITQuestionnaireCompanyWithCpService
{
    @Autowired
    private TQuestionnaireCompanyWithCpMapper tQuestionnaireCompanyMapper;

    /**
     * 查询问卷调查和企业关联表
     *
     * @param id 问卷调查和企业关联表ID
     * @return 问卷调查和企业关联表
     */
    @Override
    public TQuestionnaireCompanyWithCp selectTQuestionnaireCompanyById(Long id)
    {
        return tQuestionnaireCompanyMapper.selectTQuestionnaireCompanyById(id);
    }

    /**
     * 查询问卷调查和企业关联表列表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 问卷调查和企业关联表
     */
    @Override
    public List<TQuestionnaireCompanyWithCp> selectTQuestionnaireCompanyList(TQuestionnaireCompanyWithCp tQuestionnaireCompany)
    {
        return tQuestionnaireCompanyMapper.selectTQuestionnaireCompanyList(tQuestionnaireCompany);
    }

    /**
     * 新增问卷调查和企业关联表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 结果
     */
    @Override
    public int insertTQuestionnaireCompany(TQuestionnaireCompanyWithCp tQuestionnaireCompany)
    {
        return tQuestionnaireCompanyMapper.insertTQuestionnaireCompany(tQuestionnaireCompany);
    }

    /**
     * 修改问卷调查和企业关联表
     *
     * @param tQuestionnaireCompany 问卷调查和企业关联表
     * @return 结果
     */
    @Override
    public int updateTQuestionnaireCompany(TQuestionnaireCompanyWithCp tQuestionnaireCompany)
    {
        return tQuestionnaireCompanyMapper.updateTQuestionnaireCompany(tQuestionnaireCompany);
    }

    /**
     * 批量删除问卷调查和企业关联表
     *
     * @param ids 需要删除的问卷调查和企业关联表ID
     * @return 结果
     */
    @Override
    public int deleteTQuestionnaireCompanyByIds(Long[] ids)
    {
        return tQuestionnaireCompanyMapper.deleteTQuestionnaireCompanyByIds(ids);
    }

    /**
     * 删除问卷调查和企业关联表信息
     *
     * @param id 问卷调查和企业关联表ID
     * @return 结果
     */
    @Override
    public int deleteTQuestionnaireCompanyById(Long id)
    {
        return tQuestionnaireCompanyMapper.deleteTQuestionnaireCompanyById(id);
    }
}
