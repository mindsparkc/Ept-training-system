package com.sinosoft.system.service;

import com.sinosoft.system.domain.SysNotice;
import com.sinosoft.system.domain.SysTenant;

import java.util.List;

/**
 * 租户注册信息 服务层
 *
 *
 */
public interface ISysTenantService
{
    /**
     * 查询信息
     *
     * @param id  ID
     * @return  信息
     */
    public SysTenant selectTenantById(Long id);

    /**
     * 查询 列表
     *
     * @param tenant 信息
     * @return  集合
     */
    public List<SysTenant> selectTenantList(SysTenant tenant);

    /**
     * 新增
     *
     * @param tenant  信息
     * @return 结果
     */
    public int insertTenant(SysTenant tenant);

    /**
     * 修改
     *
     * @param tenant  信息
     * @return 结果
     */
    public int updateTenant(SysTenant tenant);

    /**
     * 删除 信息
     *
     * @param id  ID
     * @return 结果
     */
    public int deleteTenantById(Long id);

    /**
     * 批量删除 信息
     *
     * @param ids 需要删除的 ID
     * @return 结果
     */
    public int deleteTenantByIds(Long[] ids);

    /**
     * 校验租户名称是否唯一
     *
     * @param tenantName 租户名称
     * @return 结果
     */
    public int checkTenantNameUnique(String tenantName);

    /**
     * 创建客户
     * @author congpeitong
     * @date 2025-05-29 10:00:20
     * @param sysTenant 租户信息
     * @return int
     */
    int addCustomer(SysTenant sysTenant);

    /**
     * 修改客户
     * @author congpeitong
     * @date 2025-05-29 13:10:15
     * @param sysTenant 客户/租户/公司
     * @return int
     */
    int updateCustomer(SysTenant sysTenant);

    /**
     * 删除客户
     * @author congpeitong
     * @date 2025-05-30 13:51:38
     * @param tenantId 客户id
     * @return int
     */
    int deleteCustomer(Long tenantId);
}
