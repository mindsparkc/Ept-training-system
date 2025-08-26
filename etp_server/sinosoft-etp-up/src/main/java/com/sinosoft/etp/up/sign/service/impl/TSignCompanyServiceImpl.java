package com.sinosoft.etp.up.sign.service.impl;

import com.sinosoft.etp.up.sign.domain.TSignCompany;
import com.sinosoft.etp.up.sign.mapper.TSignCompanyMapper;
import com.sinosoft.etp.up.sign.service.ITSignCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动报名与企业关联表Service业务层处理
 *
 *
 * @date 2022-02-10
 */
@Service
public class TSignCompanyServiceImpl implements ITSignCompanyService
{
    @Autowired
    private TSignCompanyMapper tSignCompanyMapper;

    /**
     * 查询活动报名与企业关联表
     *
     * @param id 活动报名与企业关联表ID
     * @return 活动报名与企业关联表
     */
    @Override
    public TSignCompany selectTSignCompanyById(Long id)
    {
        return tSignCompanyMapper.selectTSignCompanyById(id);
    }

    /**
     * 查询活动报名与企业关联表列表
     *
     * @param tSignCompany 活动报名与企业关联表
     * @return 活动报名与企业关联表
     */
    @Override
    public List<TSignCompany> selectTSignCompanyList(TSignCompany tSignCompany)
    {
        return tSignCompanyMapper.selectTSignCompanyList(tSignCompany);
    }

    /**
     * 新增活动报名与企业关联表
     *
     * @param tSignCompany 活动报名与企业关联表
     * @return 结果
     */
    @Override
    public int insertTSignCompany(TSignCompany tSignCompany)
    {
        return tSignCompanyMapper.insertTSignCompany(tSignCompany);
    }



    /**
     * 批量删除活动报名与企业关联表
     *
     * @param ids 需要删除的活动报名与企业关联表ID
     * @return 结果
     */
    @Override
    public int deleteTSignCompanyByIds(Long[] ids)
    {
        return tSignCompanyMapper.deleteTSignCompanyByIds(ids);
    }

    /**
     * 删除活动报名与企业关联表信息
     *
     * @param id 活动报名与企业关联表ID
     * @return 结果
     */
    @Override
    public int deleteTSignCompanyById(Long id)
    {
        return tSignCompanyMapper.deleteTSignCompanyById(id);
    }
}
