package com.sinosoft.common.enums;

import cn.jkingtools.core.text.Convert;
import cn.jkingtools.utils.BeanUtils;
import cn.jkingtools.utils.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * VIP资源控制类型
 *
 *
 * max_dept          INT             DEFAULT 0                  COMMENT '允许最大部门数量',
 * 	max_user          INT             DEFAULT 0                  COMMENT '允许最大人员数量',
 * 	max_courseware    INT             DEFAULT 0                  COMMENT '允许最大课件数量',
 * 	max_curriculum    INT             DEFAULT 0                  COMMENT '允许最大课程数量',
 * 	max_exam          INT             DEFAULT 0                  COMMENT '允许最大考试数量',
 * 	max_file          INT             DEFAULT 0                  COMMENT '允许最大文件数量', -- 此模块还未做
 * 	max_file_size     INT             DEFAULT 0                  COMMENT '允许最大文件大小(G)', -- 此模块还未做
 *
 */
public enum VipResource {
    DEPT("dept", "部门", "maxDept", (String tenantId, Long topDeptId) -> {
        return StringUtils.format("select count(0) from sys_dept where del_flag='0' and ancestors like '0,{},{}%' ", topDeptId, tenantId);
    }),
    USER("user", "用户", "maxUser", (String tenantId, Long topDeptId) -> {
        return StringUtils.format("select count(0) from sys_user where del_flag='0' and dept_id in(select '{}' union select dept_id from sys_dept where del_flag='0' and ancestors like '0,{},{}%' )", tenantId, topDeptId, tenantId);
    }),
    COURSEWARE("courseware", "课件", "maxCourseware", (String tenantId, Long topDeptId) -> {
        return StringUtils.format("select count(0) from t_courseware where del_flag='0' and tenant_id='{}'", tenantId);
    }),
    CURRICULUM("curriculum", "课程", "maxCurriculum", (String tenantId, Long topDeptId) -> {
        return StringUtils.format("select count(0) from t_curriculum where del_flag='0' and tenant_id='{}'", tenantId);
    }),
    EXAM("exam", "考试", "maxExam", (String tenantId, Long topDeptId) -> {
        return StringUtils.format("select count(0) from exam where del_flag='0' and tenant_id='{}'", tenantId);
    }),
    FILE_NUM("file_num", "文件数量", "maxFile", (String tenantId, Long topDeptId) -> {
        return "";
    }),
    FILE_SIZE("file_size", "文件大小", "maxFileSize", (String tenantId, Long topDeptId) -> {
        return "";
    });
    private final String code;
    private final String info;
    private final String key;  // vip控制中的key
    private BiFunction<String, Long, String> sqlFunc = null;

    VipResource(String code, String info, String key, BiFunction<String, Long, String> sqlFunc) {
        this.code = code;
        this.info = info;
        this.key = key;
        this.sqlFunc = sqlFunc;
    }

    public static VipResource getVipResourceByCode(String code) {
        for (VipResource item : values()) {
            if (code.equals(item.code))
                return item;
        }
        return VipResource.USER;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public Boolean check(String tenantId, Long topDeptId, Map vip, Function<String, Integer> dataFunc) {
        Object val = vip.get(this.key);
        if (val == null) return true;
        Integer max = Convert.toInt(val);
        if (max == 0) return true;

        if (this.sqlFunc == null) return true;

        String sql = this.sqlFunc.apply(tenantId, topDeptId);
        if (StringUtils.isEmpty(sql)) return true;
        Integer count = dataFunc.apply(sql);

        if (max > count) return true;

        return false;
    }
}
