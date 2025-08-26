package com.sinosoft.user.exam.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于查询用户考试记录表
 *
 * @author wanglijie
 */
public class UserExamRecordReqQuery {
    private static final long serialVersionUID = 1L;

    private String name;
    private Long userId;
    private Long examId;

    private String status;

    private Date rangStartTime;
    private Date rageEndTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRangStartTime() {
        return rangStartTime;
    }

    public void setRangStartTime(Date rangStartTime) {
        this.rangStartTime = rangStartTime;
    }

    public Date getRageEndTime() {
        return rageEndTime;
    }

    public void setRageEndTime(Date rageEndTime) {
        this.rageEndTime = rageEndTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }


    // 数据权限相关 lyd 20250517  create_user_id  tenant_id department_id  pub_flag
    private Map<String, Object> params;
    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }
    //dept_id 不能用，而用 department_id，是因为有的表已经有这个字段，而且public TOpinionWithCp setDeptId(Long deptId)  返回的不是void
    //user_id 不能用，而用create_user_id 原因同上
    public Long createUserId;
    public Long getCreateUserId() {
        return createUserId;
    }
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
    public Long departmentId;
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
    public String tenantId;
    public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    private String pubFlag;
    public String getPubFlag() {
        return pubFlag;
    }
    public void setPubFlag(String pubFlag) {
        this.pubFlag = pubFlag;
    }
}
