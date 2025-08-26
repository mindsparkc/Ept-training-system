package com.sinosoft.admin.knowledge.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识点分类树对象 knowledge_category
 *
 *
 * @date 2022-08-10
 */
public class KnowledgeCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private Long catId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String name;

    /** 分类父ID */
    @Excel(name = "分类父ID")
    private Long parentId;

    /** 分类祖级列表 */
    @Excel(name = "分类祖级列表")
    private String ancestors;

    /** 排序字段 */
    @Excel(name = "排序字段")
    private Long sort;

    /** 删除标记（0有效 2删除） */
    private String delFlag;

    /** 子分类 */
    private List<KnowledgeCategory> children = new ArrayList<KnowledgeCategory>();


    public List<KnowledgeCategory> getChildren() {
        return children;
    }

    public void setChildren(List<KnowledgeCategory> children) {
        this.children = children;
    }

    public void setCatId(Long catId)
    {
        this.catId = catId;
    }

    public Long getCatId()
    {
        return catId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }
    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public String getAncestors()
    {
        return ancestors;
    }
    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort()
    {
        return sort;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("catId", getCatId())
            .append("name", getName())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("sort", getSort())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
