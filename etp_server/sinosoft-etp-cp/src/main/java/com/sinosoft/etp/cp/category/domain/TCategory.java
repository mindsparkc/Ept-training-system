package com.sinosoft.etp.cp.category.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类树对象 t_category
 *
 *
 * @date 2022-05-11
 */
public class TCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 父id */
    @Excel(name = "父id")
    private Long parentId;
    //private Long qyid;

    /** 祖级列表 */
    @Excel(name = "祖级列表")
    private String ancestors;

    /** 标签名称 */
    @Excel(name = "标签名称")
    private String name;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /** 首页是否显示*/
    private String sfxs;
    /** 子分类 */
    private List<TCategory> children = new ArrayList<TCategory>();

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
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
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    //public Long getQyid() {
    //    return qyid;
    //}
    //
    //public void setQyid(Long qyid) {
    //    this.qyid = qyid;
    //}

    public List<TCategory> getChildren() {
        return children;
    }

    public void setChildren(List<TCategory> children) {
        this.children = children;
    }

    public String getSfxs() {
        return sfxs;
    }

    public void setSfxs(String sfxs) {
        this.sfxs = sfxs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("name", getName())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
