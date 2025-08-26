package com.sinosoft.system.mapper;

import com.sinosoft.system.domain.SysUserGroupCompany;

import java.util.List;

/**
 * 用户组关联表Mapper接口
 *
 *
 * @date 2022-02-08
 */
public interface SysUserGroupCompanyMapper
{
    /**
     * 查询用户组关联表
     *
     * @param id 用户组关联表ID
     * @return 用户组关联表
     */
    public SysUserGroupCompany selectSysUserGroupCompanyById(Long id);

    /**
     * 查询用户组关联表列表
     *
     * @param sysUserGroupCompany 用户组关联表
     * @return 用户组关联表集合
     */
    public List<SysUserGroupCompany> selectSysUserGroupCompanyList(SysUserGroupCompany sysUserGroupCompany);

    /**
     * 新增用户组关联表
     *
     * @param sysUserGroupCompany 用户组关联表
     * @return 结果
     */
    public int insertSysUserGroupCompany(SysUserGroupCompany sysUserGroupCompany);

    /**
     * 修改用户组关联表
     *
     * @param sysUserGroupCompany 用户组关联表
     * @return 结果
     */
    public int updateSysUserGroupCompany(SysUserGroupCompany sysUserGroupCompany);

    /**
     * 删除用户组关联表
     *
     * @param groupId 用户组关联表ID
     * @return 结果
     */
    public int deleteSysUserGroupCompanyByGroupId(Long groupId);

    /**
     * 批量删除用户组关联表
     *
     * @param groupids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserGroupCompanyByGroupIds(Long[] groupids);
}
