package com.sinosoft.admin.knowledge.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 知识点对象 knowledge
 *
 *
 * @date 2022-08-10
 */
public class Knowledge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 知识点ID */
    private Long knoledgeId;

    /** 知识点描述 */
    @Excel(name = "知识点描述")
    private String content;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long catId;

    /** 数据归属用户 */
    @Excel(name = "数据归属用户")
    private Long userId;

    /** 数据归属部门 */
    @Excel(name = "数据归属部门")
    private Long deptId;

    /** 删除标记（0有效 2删除） */
    private String delFlag;

    /** 纯文本 */
    private String contentDisplay;

    /** 父类Id */
    private Long parentId;

    /** 是否存在子类状态 */
    private Boolean hasChildren;


    /** 试卷关联分类 */
    List<List<Long>> categoryIds;
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getContentDisplay() {
        return contentDisplay;
    }

    public void setContentDisplay(String contentDisplay) {
        this.contentDisplay = contentDisplay;
    }

    public void setKnoledgeId(Long knoledgeId)
    {
        this.knoledgeId = knoledgeId;
    }

    public Long getKnoledgeId()
    {
        return knoledgeId;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setCatId(Long catId)
    {
        this.catId = catId;
    }

    public Long getCatId()
    {
        return catId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public boolean hasCategoryIds() {
        return categoryIds!=null && categoryIds.size()>0;
    }

    public List<List<Long>> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<List<Long>> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("knoledgeId", getKnoledgeId())
            .append("content", getContent())
            .append("catId", getCatId())
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
