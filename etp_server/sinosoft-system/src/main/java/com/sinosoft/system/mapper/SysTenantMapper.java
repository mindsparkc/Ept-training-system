package com.sinosoft.system.mapper;

import com.sinosoft.system.domain.SysNotice;
import com.sinosoft.system.domain.SysTenant;

import java.util.List;

/**
 * 租户 数据层
 *
 *
 */
public interface SysTenantMapper
{
    /**
     * 查询租户注册信息
     *
     * @param id 租户id
     * @return 租户信息
     */
    public SysTenant selectTenantById(Long id);

    /**
     * 查询公告列表
     *
     * @param tenant 租户信息
     * @return 公告集合
     */
    public List<SysTenant> selectTenantList(SysTenant tenant);

    /**
     * 新增租户 注册
     *
     * @param tenant 租户信息
     * @return 结果
     */
    public int insertTenant(SysTenant tenant);

    /**
     * 修改
     *
     * @param tenant 租户
     * @return 结果
     */
    public int updateTenant(SysTenant tenant);

    /**
     * 删除
     *
     * @param id ID
     * @return 结果
     */
    public int deleteTenantById(Long id);

    /**
     * 批量删除公告信息
     *
     * @param ids 需要删除的ID
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
}
