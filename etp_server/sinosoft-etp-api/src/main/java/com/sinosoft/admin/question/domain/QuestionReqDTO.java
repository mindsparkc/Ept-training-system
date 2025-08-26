package com.sinosoft.admin.question.domain;

import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.core.domain.Question;

import java.util.List;

/**
 * 题目 RequestBody 请求
 * 应用于添加题目和更新题目接口
 *
 * @author wanglijie
 */
public class QuestionReqDTO extends Question {
    private static final long serialVersionUID = 1L;

    /** 题目关联分类 */
    List<List<Long>> categoryIds;

    @PreProcessField
    private String content;

    @PreProcessField
    private String analysis;

    public boolean hasCategoryIds() {
        return categoryIds!=null && categoryIds.size()>0;
    }

    public List<List<Long>> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<List<Long>> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
