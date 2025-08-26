package com.sinosoft.system.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.constant.UserConstants;
import com.sinosoft.common.core.domain.entity.SysDept;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.core.domain.model.LoginUser;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.domain.SysTenant;
import com.sinosoft.system.mapper.SysTenantMapper;
import com.sinosoft.system.service.ISysConfigService;
import com.sinosoft.system.service.ISysDeptService;
import com.sinosoft.system.service.ISysTenantService;
import com.sinosoft.system.service.ISysUserService;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 公告 服务层实现
 *
 *
 */
@Service
public class SysTenantServiceImpl implements ISysTenantService
{
    @Resource
    private SysTenantMapper tenantMapper;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysConfigService configService;

    /**
     * 查询信息
     *
     * @param id ID
     * @return 信息
     */
    @Override
    public SysTenant selectTenantById(Long id)
    {
        return tenantMapper.selectTenantById(id);
    }

    /**
     * 查询列表
     *
     * @param notice 信息
     * @return 集合
     */
    @Override
    @EtpDataScope(alias = "t")
    public List<SysTenant> selectTenantList(SysTenant tenant)
    {
        return tenantMapper.selectTenantList(tenant);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertTenant(SysTenant tenant){
        return tenantMapper.insertTenant(tenant);
    }

    /**
     * 修改
     *
     * @param tenant  信息
     * @return 结果
     */
    @Override
    public int updateTenant(SysTenant tenant)
    {
        tenant.setUpdateBy(SecurityUtils.getUsername());
        tenant.setUpdateTime(new Date());
        return tenantMapper.updateTenant(tenant);
    }

    /**
     * 删除 对象
     *
     * @param id  ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTenantById(Long id)
    {
        return tenantMapper.deleteTenantById(id);
    }

    /**
     * 批量删除信息
     *
     * @param ids 需要删除的ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTenantByIds(Long[]ids)
    {
        return tenantMapper.deleteTenantByIds(ids);
    }

    /**
     * 校验租户名称是否唯一
     *
     * @param tenantName 租户名称
     * @return 结果
     */
    @Override
    public int checkTenantNameUnique(String tenantName){
        return tenantMapper.checkTenantNameUnique(tenantName);
    }

    /**
     * 创建客户
     * @author congpeitong
     * @date 2025-05-29 10:01:00
     * @param sysTenant 租户信息
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addCustomer(SysTenant sysTenant) {
        SysDept topDept = this.deptService.queryTopDept(); // 查询顶级部门
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(sysTenant, sysDept);
        sysDept.setDeptName( sysTenant.getTenantName());
        sysDept.setLeader(sysTenant.getLinkman());
        sysDept.setPhone(sysTenant.getPhonenumber());
        sysDept.setParentId(topDept.getDeptId());
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysTenant, sysUser);
        if (!org.springframework.util.StringUtils.hasText(sysDept.getDeptName()))
        {
            throw new IllegalArgumentException("企业名称不能为空");
        }
        if (UserConstants.NOT_UNIQUE.equals(this.deptService.checkDeptNameUnique(sysDept)))
        {
            throw new IllegalArgumentException("该企业已存在");
        }
        if (!org.springframework.util.StringUtils.hasText(sysUser.getUserName())) {
            throw new IllegalArgumentException("管理员账号不允许为空");
        }
        if (sysUser.getUserName().length() < UserConstants.USERNAME_MIN_LENGTH
                || sysUser.getUserName().length() > UserConstants.USERNAME_MAX_LENGTH)  {
            throw new IllegalArgumentException("登录账户长度必须在2到20个字符之间");
        }
        if (!org.springframework.util.StringUtils.hasText(sysUser.getPassword())) {
            throw new IllegalArgumentException("管理员账号的密码不允许为空");
        }
        if (sysUser.getPassword().length() < UserConstants.PASSWORD_MIN_LENGTH
                || sysUser.getPassword().length() > UserConstants.PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("登录密码长度必须在5到20个字符之间");
        }

        if (UserConstants.NOT_UNIQUE.equals(this.userService.checkUserNameUnique(sysUser.getUserName())))
        {
            throw new IllegalArgumentException("登录账号已存在");
        }
        if (!org.springframework.util.StringUtils.hasText(sysTenant.getVipGrade())) {
            throw new IllegalArgumentException("未设置vip等级");
        }
        LoginUser loginUser = null;
        try {
            loginUser = SecurityUtils.getLoginUser();
        }catch (Exception ex){
        }

        sysDept.setCreateBy(loginUser==null ? null : loginUser.getUsername());

        // 用户昵称，没传值就用单位名称
        sysUser.setNickName(org.springframework.util.StringUtils.hasText(sysUser.getNickName()) ? sysUser.getNickName() : sysTenant.getTenantName());
        sysUser.setPassword(SecurityUtils.encryptPassword(sysUser.getPassword())); // 密码
        sysUser.setCreateBy(loginUser==null ? null : loginUser.getUsername());
        // 设置租户管理员角色
        String roleIdStr = configService.selectConfigByKey("sys.tenant.adminRole");
        if (!org.springframework.util.StringUtils.hasText(roleIdStr)) {
            throw new IllegalArgumentException("租户管理员角色未配置");
        }
        sysUser.setRoleIds(new Long[] {Long.parseLong(roleIdStr)});
        // 3. 新增租户的信息
        //保存租户昵称信息
        sysTenant.setNickName(org.springframework.util.StringUtils.hasText(sysTenant.getNickName()) ? sysTenant.getNickName() : sysTenant.getTenantName() );
        // 密码加密处理
        sysTenant.setPassword(SecurityUtils.encryptPassword(sysTenant.getPassword()));
        //部门信息
        sysTenant.setDeptCreateFlag("1");
        sysTenant.setDeptCreateTime(DateUtils.getNowDate());
        //用户信息
        sysTenant.setUserCreateFlag("1");
        sysTenant.setUserCreateTime(DateUtils.getNowDate());
        //vip属性
        sysTenant.setRegisterTime(DateUtils.getNowDate());
        // sysTenant.setVipGrade("0");
        sysTenant.setVipStartTime(DateUtils.getNowDate());
        sysTenant.setVipEndTime(DateUtils.getNyearsAfter(99));  //99年

        try {
            this.deptService.insertDept(sysDept);
            sysUser.setDeptId(sysDept.getDeptId()); // 部门id
            userService.insertUser(sysUser);
            sysTenant.setDeptId(sysDept.getDeptId());
            sysTenant.setUserId(sysUser.getUserId());
            this.insertTenant(sysTenant);
        }catch (Exception ex) {
            throw new IllegalArgumentException("客户信息保存失败");
        }
        return 1;
    }

    /**
     * 修改客户， 修改只是修改组织
     * @author congpeitong
     * @date 2025-05-29 13:10:43
     * @param sysTenant
     * @return int
     */
    @Override
    public int updateCustomer(SysTenant sysTenant) {
        Long id = sysTenant.getId();
        if (id == null) {
            throw new IllegalArgumentException("未设置需要修改的企业");
        }
        SysTenant sysTenantOld = this.selectTenantById(id);
        if (sysTenantOld == null) {
            throw new IllegalArgumentException("未查询到公司无法修改");
        }
        Long deptId = sysTenantOld.getDeptId();
        Long userId = sysTenantOld.getUserId();
        // 更新部门
        SysDept sysDeptUpdate = new SysDept();
        sysDeptUpdate.setDeptId(deptId);
        sysDeptUpdate.setDeptName(sysTenant.getTenantName());
        // 校验企业唯一性
        if (UserConstants.NOT_UNIQUE.equals(this.deptService.checkDeptNameUnique(sysDeptUpdate))) {
            throw new RuntimeException("企业已存在");
        }

        sysDeptUpdate.setPhone(sysTenant.getPhonenumber());
        sysDeptUpdate.setEmail(sysTenant.getEmail());
        sysDeptUpdate.setLeader(sysTenant.getLinkman());
        this.deptService.updateDept(sysDeptUpdate);
        // 更新租户
        this.updateTenant(sysTenant);
        return 1;
    }

    /**
     * 删除客户
     * @author congpeitong
     * @date 2025-05-30 13:51:58
     * @param tenantId 客户id
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCustomer(Long tenantId) {
        SysTenant sysTenant = this.selectTenantById(tenantId);
        if (sysTenant == null) throw new RuntimeException("未查询到删除的客户");
        Long deptId = sysTenant.getDeptId();
        Long userId = sysTenant.getUserId();
        // 1. 删除客户
        this.deleteTenantById(tenantId);
        // 2. 删除客户对应的二级部门
        this.deptService.deleteDeptById(deptId);
        // 3. 删除客户注册时候的管理员账号
        this.userService.deleteUserById(userId);
        return 1;
    }


}
