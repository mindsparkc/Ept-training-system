package com.sinosoft.etp.cp.common.service.impl;

import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.etp.cp.common.mapper.CommonWithCpMapper;
import com.sinosoft.etp.cp.common.service.CommonServiceWithCp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommonServiceWithCpImpl implements CommonServiceWithCp {

    @Resource
    private CommonWithCpMapper commonMapper;


    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<SysUser> getCurrentCompanyUsers(SysUser sysUser) {
        List<SysUser> sysUsers = commonMapper.getCurrentCompanyUsers(sysUser);
        return sysUsers.stream().filter(user -> !user.isAdmin()).collect(Collectors.toList());
    }
}
