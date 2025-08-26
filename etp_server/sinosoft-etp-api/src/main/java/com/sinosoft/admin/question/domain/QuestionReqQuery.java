package com.sinosoft.admin.question.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionReqQuery {
    private static final long serialVersionUID = 1L;

    /** 题干内容 */
    private String content;

    /** 题目类型：1-单选题；2-复选题；3-判断题；4-填空题；5-问答题；6-组合题。取字典 question_type */
    private List<String> type;

    /** 难度等级，取字典 question_level */
    private List<String> level;

    /** 是否练习（1是 0否） */
    private Integer tarinFlag;

    /** 是否考试（1是 0否） */
    private Integer examFlag;

    /** 有效性（0有效 1手动废弃） */
    private String status;

    /** 一个分类 */
    private Long categoryId;

    /** 题目关联分类，暂不支持该参数 */
    private List<String> categoryIds;

    /** 随机取 N 道题 */
    private Integer n;
    /** 题目id，在调用 list 查询时，该字段标记要排除的题目 ID */
    List<Long> quIds;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getLevel() {
        return level;
    }

    public void setLevel(List<String> level) {
        this.level = level;
    }

    public Integer getTarinFlag() {
        return tarinFlag;
    }

    public void setTarinFlag(Integer tarinFlag) {
        this.tarinFlag = tarinFlag;
    }

    public Integer getExamFlag() {
        return examFlag;
    }

    public void setExamFlag(Integer examFlag) {
        this.examFlag = examFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public List<Long> getQuIds() {
        return quIds;
    }

    public void setQuIds(List<Long> quIds) {
        this.quIds = quIds;
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
