package com.sinosoft.etp.cp.common.mapper;

import com.sinosoft.common.core.domain.entity.SysUser;

import java.util.List;

public interface CommonWithCpMapper {

    /**
     * 获取当前企业下的所有用户
     * @return List<SysUser>
     */
    List<SysUser> getCurrentCompanyUsers(SysUser sysUser);
}
