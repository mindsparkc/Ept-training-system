package com.sinosoft.admin.exam.domain;

import com.sinosoft.core.domain.Exam;

import java.util.List;

/**
 * 管理端组织考试，前端提交的请求
 *
 * @author wanglijie
 */
public class ExamReqDTO extends Exam {
    private static final long serialVersionUID = 1L;

    /** 考试关联分类 */
    List<List<Long>> categoryIds;

    /** 考试开放规则 */
    List<Long> openRules;

    public boolean hasCategoryIds() {
        return categoryIds!=null && categoryIds.size()>0;
    }

    public List<List<Long>> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<List<Long>> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Long> getOpenRules() {
        return openRules;
    }

    public void setOpenRules(List<Long> openRules) {
        this.openRules = openRules;
    }
}
