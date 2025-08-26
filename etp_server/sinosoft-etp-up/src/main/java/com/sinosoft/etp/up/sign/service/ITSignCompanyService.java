package com.sinosoft.etp.up.sign.service;

import com.sinosoft.etp.up.sign.domain.TSignCompany;

import java.util.List;

/**
 * 活动报名与企业关联表Service接口
 *
 *
 * @date 2022-02-10
 */
public interface ITSignCompanyService
{
    /**
     * 查询活动报名与企业关联表
     *
     * @param id 活动报名与企业关联表ID
     * @return 活动报名与企业关联表
     */
    public TSignCompany selectTSignCompanyById(Long id);

    /**
     * 查询活动报名与企业关联表列表
     *
     * @param tSignCompany 活动报名与企业关联表
     * @return 活动报名与企业关联表集合
     */
    public List<TSignCompany> selectTSignCompanyList(TSignCompany tSignCompany);

    /**
     * 新增活动报名与企业关联表
     *
     * @param tSignCompany 活动报名与企业关联表
     * @return 结果
     */
    public int insertTSignCompany(TSignCompany tSignCompany);


    /**
     * 批量删除活动报名与企业关联表
     *
     * @param ids 需要删除的活动报名与企业关联表ID
     * @return 结果
     */
    public int deleteTSignCompanyByIds(Long[] ids);

    /**
     * 删除活动报名与企业关联表信息
     *
     * @param id 活动报名与企业关联表ID
     * @return 结果
     */
    public int deleteTSignCompanyById(Long id);
}
