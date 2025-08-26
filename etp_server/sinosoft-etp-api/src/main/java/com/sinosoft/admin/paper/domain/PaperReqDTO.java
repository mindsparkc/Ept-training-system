package com.sinosoft.admin.paper.domain;

import com.sinosoft.core.domain.Paper;
import com.sinosoft.core.domain.PaperUnit;

import java.util.List;

/**
 * 管理端组织试卷，前端提交的请求
 *
 * @author wanglijie
 */
public class PaperReqDTO extends Paper {
    private static final long serialVersionUID = 1L;

    /** 试卷组成单元 */
    private List<PaperUnit> paperUnit;

    /** 试卷关联分类 */
    List<List<Long>> categoryIds;

    public boolean hasCategoryIds() {
        return categoryIds!=null && categoryIds.size()>0;
    }

    public List<PaperUnit> getPaperUnit() {
        return paperUnit;
    }

    public void setPaperUnit(List<PaperUnit> paperUnit) {
        this.paperUnit = paperUnit;
    }

    public List<List<Long>> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<List<Long>> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
