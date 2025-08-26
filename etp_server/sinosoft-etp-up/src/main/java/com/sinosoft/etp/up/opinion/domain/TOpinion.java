package com.sinosoft.etp.up.opinion.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 意见反馈对象 t_opinion
 *
 *
 * @date 2022-02-18
 */
public class TOpinion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 意见描述 */
    @Excel(name = "意见描述")
    private String yjms;

    /** 附件地址 */
    @Excel(name = "附件地址")
    @PreProcessField
    private String fj;
    private String fjname;
    /** 回复内容 */
    @Excel(name = "回复内容")
    private String hf;

    /** 学员ID */
    private Long xyid;

    /** 删除标志 */
    private String delFlag;

    /** 回复状态 0 未回复 1已回复 */
    @Excel(name = "回复状态 0 未回复 1已回复")
    private String hfStatus;
    private String hfStatusName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setYjms(String yjms)
    {
        this.yjms = yjms;
    }

    public String getYjms()
    {
        return yjms;
    }
    public void setFj(String fj)
    {
        this.fj = fj;
    }

    public String getFj()
    {
        return fj;
    }
    public void setHf(String hf)
    {
        this.hf = hf;
    }

    public String getHf()
    {
        return hf;
    }
    public void setXyid(Long xyid)
    {
        this.xyid = xyid;
    }

    public Long getXyid()
    {
        return xyid;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }
    public void setHfStatus(String hfStatus)
    {
        this.hfStatus = hfStatus;
    }

    public String getHfStatus()
    {
        return hfStatus;
    }

    public String getHfStatusName() {
        return hfStatusName;
    }

    public void setHfStatusName(String hfStatusName) {
        this.hfStatusName = hfStatusName;
    }

    public String getFjname() {
        return fjname;
    }

    public void setFjname(String fjname) {
        this.fjname = fjname;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("yjms", getYjms())
            .append("fj", getFj())
            .append("hf", getHf())
            .append("xyid", getXyid())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("hfStatus", getHfStatus())
            .toString();
    }
}
