package com.sinosoft.etp.up.banner.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 轮播图对象 t_banner
 *
 *
 * @date 2022-02-22
 */
public class TBanner extends BaseEntity
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("imageurl", getImageurl())
            .append("openurl", getOpenurl())
            .append("title", getTitle())
            .append("sort", getSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
