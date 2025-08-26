package com.sinosoft.system.service.impl;

import com.sinosoft.system.domain.SysUserGroupCompany;
import com.sinosoft.system.mapper.SysUserGroupCompanyMapper;
import com.sinosoft.system.service.ISysUserGroupCompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户组关联表Service业务层处理
 *
 *
 * @date 2022-02-08
 */
@Service
public class SysUserGroupCompanyServiceImpl implements ISysUserGroupCompanyService
{
    @Resource
    private SysUserGroupCompanyMapper sysUserGroupCompanyMapper;

    /**
     * 查询用户组关联表
     *
     * @param id 用户组关联表ID
     * @return 用户组关联表
     */
    @Override
    public SysUserGroupCompany selectSysUserGroupCompanyById(Long id)
    {
        return sysUserGroupCompanyMapper.selectSysUserGroupCompanyById(id);
    }

    /**
     * 查询用户组关联表列表
     *
     * @param sysUserGroupCompany 用户组关联表
     * @return 用户组关联表
     */
    @Override
    public List<SysUserGroupCompany> selectSysUserGroupCompanyList(SysUserGroupCompany sysUserGroupCompany)
    {
        return sysUserGroupCompanyMapper.selectSysUserGroupCompanyList(sysUserGroupCompany);
    }

    /**
     * 新增用户组关联表
     *
     * @param sysUserGroupCompany 用户组关联表
     * @return 结果
     */
    @Override
    public int insertSysUserGroupCompany(SysUserGroupCompany sysUserGroupCompany)
    {
        return sysUserGroupCompanyMapper.insertSysUserGroupCompany(sysUserGroupCompany);
    }

    /**
     * 修改用户组关联表
     *
     * @param sysUserGroupCompany 用户组关联表
     * @return 结果
     */
    @Override
    public int updateSysUserGroupCompany(SysUserGroupCompany sysUserGroupCompany)
    {
        return sysUserGroupCompanyMapper.updateSysUserGroupCompany(sysUserGroupCompany);
    }

    /**
     * 批量删除用户组关联表
     *
     * @param ids 需要删除的用户组关联表ID
     * @return 结果
     */
    @Override
    public int deleteSysUserGroupCompanyByIds(Long[] ids)
    {
        return sysUserGroupCompanyMapper.deleteSysUserGroupCompanyByGroupIds(ids);
    }

    /**
     * 删除用户组关联表信息
     *
     * @param id 用户组关联表ID
     * @return 结果
     */
    @Override
    public int deleteSysUserGroupCompanyById(Long id)
    {
        return sysUserGroupCompanyMapper.deleteSysUserGroupCompanyByGroupId(id);
    }
}
