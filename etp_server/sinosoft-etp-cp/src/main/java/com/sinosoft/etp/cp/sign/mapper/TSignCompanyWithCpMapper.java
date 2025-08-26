package com.sinosoft.etp.cp.sign.mapper;

import com.sinosoft.etp.cp.sign.domain.TSignCompanyWithCp;

import java.util.List;

/**
 * 活动报名与企业关联表Mapper接口
 *
 *
 * @date 2022-02-10
 */
public interface TSignCompanyWithCpMapper
{
    /**
     * 查询活动报名与企业关联表
     *
     * @param id 活动报名与企业关联表ID
     * @return 活动报名与企业关联表
     */
    public TSignCompanyWithCp selectTSignCompanyById(Long id);

    /**
     * 查询活动报名与企业关联表列表
     *
     * @param tSignCompany 活动报名与企业关联表
     * @return 活动报名与企业关联表集合
     */
    public List<TSignCompanyWithCp> selectTSignCompanyList(TSignCompanyWithCp tSignCompany);

    /**
     * 新增活动报名与企业关联表
     *
     * @param tSignCompany 活动报名与企业关联表
     * @return 结果
     */
    public int insertTSignCompany(TSignCompanyWithCp tSignCompany);


    /**
     * 删除活动报名与企业关联表
     *
     * @param signid 活动报名与企业关联表ID
     * @return 结果
     */
    public int deleteTSignCompanyById(Long signid);

    /**
     * 批量删除活动报名与企业关联表
     *
     * @param signids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTSignCompanyByIds(Long[] signids);
}
