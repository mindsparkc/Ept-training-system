package com.sinosoft.system.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.domain.SysUserGroup;
import com.sinosoft.system.mapper.SysUserGroupCompanyMapper;
import com.sinosoft.system.mapper.SysUserGroupMapper;
import com.sinosoft.system.service.ISysUserGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 用户组Service业务层处理
 *
 *
 * @date 2022-02-08
 */
@Service
public class SysUserGroupServiceImpl implements ISysUserGroupService
{
    @Resource
    private SysUserGroupMapper sysUserGroupMapper;

    @Resource
    private SysUserGroupCompanyMapper sysUserGroupCompanyMapper;

    /**
     * 查询用户组
     *
     * @param groupId 用户组ID
     * @return 用户组
     */
    @Override
    public SysUserGroup selectSysUserGroupById(Long groupId)
    {
        return sysUserGroupMapper.selectSysUserGroupById(groupId);
    }

    /**
     * 查询用户组列表
     *
     * @param sysUserGroup 用户组
     * @return 用户组
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysUserGroup> selectSysUserGroupList(SysUserGroup sysUserGroup)
    {
        return sysUserGroupMapper.selectSysUserGroupList(sysUserGroup);
    }

    /**
     * 新增用户组
     *
     * @param sysUserGroup 用户组
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysUserGroup(SysUserGroup sysUserGroup)
    {
        sysUserGroup.setCreateTime(DateUtils.getNowDate());
        sysUserGroup.setDelFlag("0");
        sysUserGroup.setCreateBy(SecurityUtils.getUsername());
        int res = sysUserGroupMapper.insertSysUserGroup(sysUserGroup);
        // todo 操作 sys_user_group_company
        //SysUserGroupCompany sysUserGroupCompany = new SysUserGroupCompany();
        //sysUserGroupCompany.setGroupId(sysUserGroup.getGroupId());
        //sysUserGroupCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //res += sysUserGroupCompanyMapper.insertSysUserGroupCompany(sysUserGroupCompany);
        return res;
    }

    /**
     * 修改用户组
     *
     * @param sysUserGroup 用户组
     * @return 结果
     */
    @Override
    public int updateSysUserGroup(SysUserGroup sysUserGroup)
    {
        sysUserGroup.setUpdateTime(DateUtils.getNowDate());
        sysUserGroup.setUpdateBy(SecurityUtils.getUsername());
        return sysUserGroupMapper.updateSysUserGroup(sysUserGroup);
    }

    /**
     * 批量删除用户组
     *
     * @param groupIds 需要删除的用户组ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysUserGroupByIds(Long[] groupIds)
    {
        // todo 判断该组下是否有用户，没有删除，有不允许删除
        for (Long groupId : groupIds) {
            Map map = sysUserGroupMapper.selectByGroupId(groupId);
            if (!map.isEmpty()) {
                return -1;
            }
        }
        int res = sysUserGroupMapper.deleteSysUserGroupByIds(groupIds);
        //res += sysUserGroupCompanyMapper.deleteSysUserGroupCompanyByGroupIds(groupIds);
        return res;
    }

    /**
     * 删除用户组信息
     *
     * @param groupId 用户组ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysUserGroupById(Long groupId)
    {
        // todo 判断该组下是否有用户，没有删除，有不允许删除
        Map map = sysUserGroupMapper.selectByGroupId(groupId);
        if (map.isEmpty()){
            // 删除关联表
            //int res = sysUserGroupCompanyMapper.deleteSysUserGroupCompanyByGroupId(groupId);
            int res = sysUserGroupMapper.deleteSysUserGroupById(groupId);
            return res;
        }
        return -1;
    }
}
