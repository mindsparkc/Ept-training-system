package com.sinosoft.etp.cp.banner.service.impl;

import com.sinosoft.etp.cp.banner.domain.TBannerCompanyWithCp;
import com.sinosoft.etp.cp.banner.mapper.TBannerCompanyWithCpMapper;
import com.sinosoft.etp.cp.banner.service.ITBannerCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * banner与企业关联表Service业务层处理
 *
 *
 * @date 2022-03-09
 */
@Service
public class TBannerCompanyServiceWithCpImpl implements ITBannerCompanyWithCpService
{
    @Autowired
    private TBannerCompanyWithCpMapper tBannerCompanyMapper;

    /**
     * 查询banner与企业关联表
     *
     * @param id banner与企业关联表ID
     * @return banner与企业关联表
     */
    @Override
    public TBannerCompanyWithCp selectTBannerCompanyById(Long id)
    {
        return tBannerCompanyMapper.selectTBannerCompanyById(id);
    }

    /**
     * 查询banner与企业关联表列表
     *
     * @param tBannerCompany banner与企业关联表
     * @return banner与企业关联表
     */
    @Override
    public List<TBannerCompanyWithCp> selectTBannerCompanyList(TBannerCompanyWithCp tBannerCompany)
    {
        return tBannerCompanyMapper.selectTBannerCompanyList(tBannerCompany);
    }

    /**
     * 新增banner与企业关联表
     *
     * @param tBannerCompany banner与企业关联表
     * @return 结果
     */
    @Override
    public int insertTBannerCompany(TBannerCompanyWithCp tBannerCompany)
    {
        return tBannerCompanyMapper.insertTBannerCompany(tBannerCompany);
    }

    /**
     * 修改banner与企业关联表
     *
     * @param tBannerCompany banner与企业关联表
     * @return 结果
     */
    @Override
    public int updateTBannerCompany(TBannerCompanyWithCp tBannerCompany)
    {
        return tBannerCompanyMapper.updateTBannerCompany(tBannerCompany);
    }

    /**
     * 批量删除banner与企业关联表
     *
     * @param ids 需要删除的banner与企业关联表ID
     * @return 结果
     */
    @Override
    public int deleteTBannerCompanyByIds(Long[] ids)
    {
        return tBannerCompanyMapper.deleteTBannerCompanyByIds(ids);
    }

    /**
     * 删除banner与企业关联表信息
     *
     * @param id banner与企业关联表ID
     * @return 结果
     */
    @Override
    public int deleteTBannerCompanyById(Long id)
    {
        return tBannerCompanyMapper.deleteTBannerCompanyById(id);
    }
}
