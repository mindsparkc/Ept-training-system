package com.sinosoft.admin.paper.domain;

import java.util.HashMap;
import java.util.Map;

public class PaperQuery {
    private static final long serialVersionUID = 1L;

    /** 试卷名称 */
    private String name;

    /** 考试描述 */
    private String content;

    /** 试卷状态（0暂存 99发布） */
    private Long status;

    /** 一个分类 */
    private Long categoryId;

    private String flag;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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
