package com.sinosoft.system.service;

import com.sinosoft.system.domain.SysUserGroup;

import java.util.List;

/**
 * 用户组Service接口
 *
 * 
 * @date 2022-02-08
 */
public interface ISysUserGroupService
{
    /**
     * 查询用户组
     *
     * @param groupId 用户组ID
     * @return 用户组
     */
    public SysUserGroup selectSysUserGroupById(Long groupId);

    /**
     * 查询用户组列表
     *
     * @param sysUserGroup 用户组
     * @return 用户组集合
     */
    public List<SysUserGroup> selectSysUserGroupList(SysUserGroup sysUserGroup);

    /**
     * 新增用户组
     *
     * @param sysUserGroup 用户组
     * @return 结果
     */
    public int insertSysUserGroup(SysUserGroup sysUserGroup);

    /**
     * 修改用户组
     *
     * @param sysUserGroup 用户组
     * @return 结果
     */
    public int updateSysUserGroup(SysUserGroup sysUserGroup);

    /**
     * 批量删除用户组
     *
     * @param groupIds 需要删除的用户组ID
     * @return 结果
     */
    public int deleteSysUserGroupByIds(Long[] groupIds);

    /**
     * 删除用户组信息
     *
     * @param groupId 用户组ID
     * @return 结果
     */
    public int deleteSysUserGroupById(Long groupId);
}
