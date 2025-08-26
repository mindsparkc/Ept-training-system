package com.sinosoft.etp.up.questionnaire.service.impl;

import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaireCompany;
import com.sinosoft.etp.up.questionnaire.mapper.TQuestionnaireCompanyMapper;
import com.sinosoft.etp.up.questionnaire.service.ITQuestionnaireCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问卷调查和企业关联表Service业务层处理
 *
 * 
 * @date 2022-02-16
 */
@Service
public class TQuestionnaireCompanyServiceImpl implements ITQuestionnaireCompanyService
{
    @Autowired
    private TQuestionnaireCompanyMapper tQuestionnaireCompanyMapper;

    /**
     * 查询问卷调查和企业关联表
     *
     * @param id 问卷调查和企业关联表ID
     * @return 问卷调查和企业关联表
     */
    @Override
    public TQuestionnaireCompany selectTQuestionnaireCompanyById(Long id)
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
    public List<TQuestionnaireCompany> selectTQuestionnaireCompanyList(TQuestionnaireCompany tQuestionnaireCompany)
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
    public int insertTQuestionnaireCompany(TQuestionnaireCompany tQuestionnaireCompany)
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
    public int updateTQuestionnaireCompany(TQuestionnaireCompany tQuestionnaireCompany)
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
