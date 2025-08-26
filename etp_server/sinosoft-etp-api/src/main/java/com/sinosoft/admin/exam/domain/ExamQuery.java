package com.sinosoft.admin.exam.domain;

import java.util.HashMap;
import java.util.Map;

public class ExamQuery {
    /** 考试名称 */
    private String name;

    /** 考试描述 */
    private String content;

    /** 开放策略（1公开 2部门 3群组 4限定成员） */
    private String openType;

    /** 考试状态（1暂存 2发布 3正常结束 4强制结束），允许管理员强制结束已经开始的考试，学员只能搜索到已发布的考试 */
    private Long status;

    /** 考试关联分类 */
    Long categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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
