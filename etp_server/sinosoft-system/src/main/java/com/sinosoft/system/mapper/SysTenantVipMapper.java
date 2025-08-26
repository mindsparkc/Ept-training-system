package com.sinosoft.system.mapper;

import java.util.List;
import com.sinosoft.system.domain.SysTenantVip;

/**
 * 租户vip资源控制Mapper接口
 *
 * @author sinosoft
 * @date 2025-05-29
 */
public interface SysTenantVipMapper
{
    /**
     * 查询租户vip资源控制
     *
     * @param id 租户vip资源控制ID
     * @return 租户vip资源控制
     */
    public SysTenantVip selectSysTenantVipById(Long id);

    /**
     * 查询租户vip资源控制
     *
     * @param deptId 部门id 也是租户id
     * @return 租户vip资源控制
     */
    public SysTenantVip selectSysTenantVipByCode(String vipCode);

    /**
     * 查询租户vip资源控制列表
     *
     * @param sysTenantVip 租户vip资源控制
     * @return 租户vip资源控制集合
     */
    public List<SysTenantVip> selectSysTenantVipList(SysTenantVip sysTenantVip);

    /**
     * 批量查询租户vip资源控制列表
     *
     * @param ids 需要查询的租户vip资源控制ID
     * @return 租户vip资源控制集合
     */
    public List<SysTenantVip> selectSysTenantVipByIds(Long[] ids);

    /**
     * 新增租户vip资源控制
     *
     * @param sysTenantVip 租户vip资源控制
     * @return 结果
     */
    public int insertSysTenantVip(SysTenantVip sysTenantVip);

    /**
     * 修改租户vip资源控制
     *
     * @param sysTenantVip 租户vip资源控制
     * @return 结果
     */
    public int updateSysTenantVip(SysTenantVip sysTenantVip);

    /**
     * 删除租户vip资源控制
     *
     * @param id 租户vip资源控制ID
     * @return 结果
     */
    public int deleteSysTenantVipById(Long id);

    /**
     * 批量删除租户vip资源控制
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysTenantVipByIds(Long[] ids);


    /**
     * 获取资源数量
     * @param sql
     * @return
     */
    public Integer getCount(String sql);
}
