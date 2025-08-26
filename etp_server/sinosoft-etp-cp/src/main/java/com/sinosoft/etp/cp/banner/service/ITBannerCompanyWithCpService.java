package com.sinosoft.etp.cp.banner.service;

import com.sinosoft.etp.cp.banner.domain.TBannerCompanyWithCp;

import java.util.List;

/**
 * banner与企业关联表Service接口
 *
 * 
 * @date 2022-03-09
 */
public interface ITBannerCompanyWithCpService
{
    /**
     * 查询banner与企业关联表
     *
     * @param id banner与企业关联表ID
     * @return banner与企业关联表
     */
    public TBannerCompanyWithCp selectTBannerCompanyById(Long id);

    /**
     * 查询banner与企业关联表列表
     *
     * @param tBannerCompany banner与企业关联表
     * @return banner与企业关联表集合
     */
    public List<TBannerCompanyWithCp> selectTBannerCompanyList(TBannerCompanyWithCp tBannerCompany);

    /**
     * 新增banner与企业关联表
     *
     * @param tBannerCompany banner与企业关联表
     * @return 结果
     */
    public int insertTBannerCompany(TBannerCompanyWithCp tBannerCompany);

    /**
     * 修改banner与企业关联表
     *
     * @param tBannerCompany banner与企业关联表
     * @return 结果
     */
    public int updateTBannerCompany(TBannerCompanyWithCp tBannerCompany);

    /**
     * 批量删除banner与企业关联表
     *
     * @param ids 需要删除的banner与企业关联表ID
     * @return 结果
     */
    public int deleteTBannerCompanyByIds(Long[] ids);

    /**
     * 删除banner与企业关联表信息
     *
     * @param id banner与企业关联表ID
     * @return 结果
     */
    public int deleteTBannerCompanyById(Long id);
}
