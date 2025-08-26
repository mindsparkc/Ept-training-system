package com.sinosoft.etp.cp.banner.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 轮播图对象 t_banner
 *
 * 
 * @date 2022-03-09
 */
public class TBannerWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 图片地址 */
    @Excel(name = "图片地址")
    @PreProcessField
    private String imageurl;

    /** 点击跳转的目标地址 */
    @Excel(name = "点击跳转的目标地址")
    private String openurl;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 排序 */
    @Excel(name = "排序")
    private String sort;

    /** 标记移动端还是PC端 【0移动、1PC端】 */
    @Excel(name = "标记移动端还是PC端 【0移动、1PC端】")
    private String source;

    /** 生效状态:0-未生效，1-已生效 */
    @Excel(name = "生效状态:0-未生效，1-已生效")
    private String status;

    /** 删除标志 */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setImageurl(String imageurl)
    {
        this.imageurl = imageurl;
    }

    public String getImageurl()
    {
        return imageurl;
    }
    public void setOpenurl(String openurl)
    {
        this.openurl = openurl;
    }

    public String getOpenurl()
    {
        return openurl;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getSort()
    {
        return sort;
    }
    public void setSource(String source)
    {
        this.source = source;
    }

    public String getSource()
    {
        return source;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("imageurl", getImageurl())
                .append("openurl", getOpenurl())
                .append("title", getTitle())
                .append("sort", getSort())
                .append("source", getSource())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
