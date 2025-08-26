package com.sinosoft.user.exam.domain;


import cn.jkingtools.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试列表查询
 *
 * @author wanglijie
 */
public class UserExamReqQuery {
    private static final long serialVersionUID = 1L;

    /** 考试名称 */
    private String name;

    /** 考试描述 */
    private String content;

    /** 排序字段 '字段名,desc;字段名,asc' */
    private String orders;

    /** 考试限范围 */
    private String openType;

    /** 用户ID */
    private Long userId;

    /** 用户岗位 */
    private Long[] postIds;

    /** 用户部门 ID */
    private Long[] deptIds;
    private Long deptId;

    private Long examId;

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    /**
     * 校验 Query 是否有效
     * @return false 为无效，true 为有效
     */
    public boolean validated() {
        if (StringUtils.isBlank(orders)) return true;

        // orders 必须是以 ‘,’ 分割的，且第二位只能是 desc 或 asc
        // 多个排序规则以 ; 分割
        // 该逻辑过滤掉所有合规的，如果不合格的 size != 0 则肯定 Query 无效
        List<String> orderList = Arrays.asList(orders.split(";"));
        return orderList.stream().filter(order -> {
            String[] split = order.toLowerCase().split(",");
            if (split.length != 2) return true;
            if (split[1].equals("desc") || split[1].equals("asc")) return false;
            return true;
        }).collect(Collectors.toList()).size() == 0;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

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

    public String getOrders() {
        if (StringUtils.isBlank(orders)) return null;
        return orders.replace(",", " ").replace(";", ",");
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public Long[] getPostIds() {
        return postIds;
    }

    public void setPostIds(Long[] postIds) {
        this.postIds = postIds;
    }

    public Long[] getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(Long[] deptIds) {
        this.deptIds = deptIds;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
