package com.sinosoft.system.utils;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.core.domain.model.LoginUser;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.spring.SpringUtils;
import com.sinosoft.system.service.ISysConfigService;

/**
 * @Description: 数据权限工具类
 * @Date  2025/5/19
 * @Author soft.l
*/
public class EtpDataScopeUtils {
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 仅本部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "30";

    /**
     * 本部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "31";  //40

    /**
     * 本部门及所有上级部门数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_PARENT = "32";

    /**
     * 本部门及所有上级和下级部门数据权限 （即仅和本部部门有关的部门，无权限查看其他同级部门及其下属部门，以及上级部门的同级部门和其下属部门资源）
     * 含0租户
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD_PARENT = "33";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 租户数据权限  不含0租户数据
     */
    public static final String DATA_SCOPE_TENANT = "60";
    /**
     * 本租户以及0租户数据权限  0租户其实就是对全部开放（由不属于任何部门的用户以及属于顶级部门的用户发布的资源）
     */
    public static final String DATA_SCOPE_TENANT_AND_0TENANT = "61";

    /**
     * 数据权限过滤关键字
     */
    public static final String ETP_DATA_SCOPE = "etpDataScope";

    /**
     * 设置数据权限字段
     *
     * @param entity 实体类
     */
    public static void setDataScope(BaseEntity entity) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        entity.setDepartmentId(user.getDeptId() == null ? 0L : user.getDeptId());
        entity.setTenantId(user.getTenantId() == null ? "0" : user.getTenantId());
        entity.setCreateUserId(user.getUserId() == null ? 0L : user.getUserId());
        if (entity.getPubFlag() == null) entity.setPubFlag("0");
    }

    // 根据规则生成权限sql   最后结果 要么为空 要么 and开头
    // 业务表中必须有  department_id  tenant_id  create_user_id  pub_flag
    public static String getDataScopeSql(String alias) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser)) return null;
        ISysConfigService configService = SpringUtils.getBean(ISysConfigService.class);
        String dataScope = configService.selectConfigByKey("etp.datascope.mode");
        if (alias == null) alias = "";
        // 数据权限和角色无关，后续可以定义系统参数，来决定逻辑h
        StringBuilder sqlString = new StringBuilder();

        SysUser user = loginUser.getUser();
        String separtor = StringUtils.isNotBlank(alias) ? "." : "";

        String tenantId = user.getTenantId() == null ? "0" : user.getTenantId(); // 即二级部门的id
        if ("0".equals(tenantId)) return "";

        Long deptId = user.getDeptId() == null ? 0L : user.getDeptId(); // 所属部门
        if (deptId==0L) return "";

        String ancestors = user.getDept() == null ? "" : user.getDept().getAncestors();
        if (StringUtils.isEmpty(ancestors)) return "";

        String topDeptId = "1";// 目前是1，顶级部门是系统自带的    parentDeptIds[0]=0 parentDeptIds[1]=顶级部门id
        String[] parentDeptIds = ancestors.split(",");
        if (parentDeptIds.length < 2) return "";
        topDeptId = parentDeptIds[1];


        switch (dataScope) {
            case DATA_SCOPE_ALL://全部 1
                //sqlString = new StringBuilder();
                break;
            case DATA_SCOPE_CUSTOM://自定义 2  还未实现，需要额外定义如何自定义
                /*sqlString.append(StringUtils.format(
                        " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", deptAlias,
                        role.getRoleId()));*/
                break;
            case DATA_SCOPE_DEPT:// 仅本部门  30
                sqlString.append(StringUtils.format(" {}{}department_id = {} ", alias, separtor, deptId));
                break;
            case DATA_SCOPE_DEPT_AND_CHILD://本部门及所有子部门 31
                //SELECT * FROM sys_dept WHERE dept_id=2 or   ancestors like '0,1,2%'
                sqlString.append(StringUtils.format(
                        "{}{}department_id IN (SELECT '{}' union SELECT dept_id FROM sys_dept WHERE ancestors like '{},{}%')",
                        alias, separtor, deptId, ancestors, deptId));
                break;
            case DATA_SCOPE_DEPT_AND_PARENT://本部门及所有上级部门 32
                //SELECT * FROM sys_dept WHERE '0,1,2,5' like CONCAT(ancestors,',',dept_id,'%')
                sqlString.append(StringUtils.format(
                        "{}{}department_id IN (SELECT dept_id FROM sys_dept WHERE '{},{}' like CONCAT(ancestors,',',dept_id,'%'))",
                        alias, separtor, ancestors, deptId));
                break;
            case DATA_SCOPE_DEPT_AND_CHILD_PARENT://本部门及所有上级部门和下级 33 即不看同级其他部门及他们的下级部门的
                //SELECT * FROM sys_dept WHERE '0,1,2,5' like CONCAT(ancestors,',',dept_id,'%')
                sqlString.append(StringUtils.format(
                        "{}{}department_id IN (SELECT dept_id FROM sys_dept WHERE '{},{}' like CONCAT(ancestors,',',dept_id,'%') or '{},{}' like CONCAT(ancestors,',',dept_id,'%'))",
                        alias, separtor, ancestors, deptId, ancestors, deptId));
                break;
            case DATA_SCOPE_SELF://仅本人 5
                sqlString.append(StringUtils.format("{}{}create_user_id = {} ", alias, separtor, user.getUserId()));
                break;
            case DATA_SCOPE_TENANT://仅本租户 60 不含0租户  避免语句中 null变成字符串
                sqlString.append(StringUtils.format("{}{}tenant_id = '{}' ", alias, separtor, tenantId));
                break;
            case DATA_SCOPE_TENANT_AND_0TENANT: //61 本租户及0租户
                sqlString.append(StringUtils.format("{}{}tenant_id in('0','{}')", alias, separtor, tenantId));
                break;
        }
        return sqlString.length() > 0 ? " AND (" + sqlString.toString() + ")" : "";
    }


    // 通过部门id 来控制数据权限   限于本租户
    public static String getDeptIdDataScopeSqlByTenant(String alias) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String separtor = StringUtils.isNotBlank(alias) ? "." : "";
        String tenantId = user.getTenantId() == null ? "0" : user.getTenantId(); // 即二级部门的id
        if("0".equals(tenantId))return "";

        String ancestors = user.getDept() == null ? "" : user.getDept().getAncestors();
        if (StringUtils.isEmpty(ancestors)) return "";

        String topDeptId = "1";// 目前是1，顶级部门是系统自带的    parentDeptIds[0]=0 parentDeptIds[1]=顶级部门id
        String[] parentDeptIds = ancestors.split(",");
        if (parentDeptIds.length < 2) return "";
        topDeptId = parentDeptIds[1];
        return StringUtils.format(
                "AND {}{}dept_id IN (SELECT '{}' union SELECT dept_id FROM sys_dept WHERE ancestors like '0,{},{}%')",
                alias, separtor, tenantId, topDeptId, tenantId);
    }

    /**
     * @param alias 业务表别名(dept字段的表别名)
     * @return java.lang.String
     * @Description: 通过部门id 来控制数据权限   限于本部门及以下部门
     * @Date 2025/5/28 10:36
     * @Author soft.l
     */
    public static String getDeptIdDataScopeSqlByDept(String alias) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String separtor = StringUtils.isNotBlank(alias) ? "." : "";
        Long deptId = user.getDeptId() == null ? 0L : user.getDeptId(); // 所属部门
        if(deptId==0L)return "";
        String ancestors = user.getDept() == null ? "" : user.getDept().getAncestors();
        if (StringUtils.isEmpty(ancestors)) return "";
        return StringUtils.format(
                "AND {}{}dept_id IN (SELECT '{}' union SELECT dept_id FROM sys_dept WHERE ancestors like '{},{}%')",
                alias, separtor, deptId, ancestors, deptId); //注意要不要,问题
    }
}
