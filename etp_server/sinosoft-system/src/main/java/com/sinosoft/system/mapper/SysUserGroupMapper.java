package com.sinosoft.system.mapper;

import com.sinosoft.system.domain.SysUserGroup;

import java.util.List;
import java.util.Map;

/**
 * 用户组Mapper接口
 *
 *
 * @date 2022-02-08
 */
public interface SysUserGroupMapper
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
     * 删除用户组
     *
     * @param groupId 用户组ID
     * @return 结果
     */
    public int deleteSysUserGroupById(Long groupId);

    /**
     * 批量删除用户组
     *
     * @param groupIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserGroupByIds(Long[] groupIds);


    public Map selectByGroupId(Long groupId);

}
