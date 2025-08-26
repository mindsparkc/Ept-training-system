package com.sinosoft.etp.cp.sign.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * sign对象 t_sign
 *
 *
 * @date 2022-02-09
 */
public class TSignWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识 */
    private Long id;

    /** 活动标题 */
    @Excel(name = "活动标题")
    private String hdmc;

    /** 活动副标题 */
    @Excel(name = "活动副标题")
    private String hdfmc;

    /** 活动封面 */
    @Excel(name = "活动封面")
    @PreProcessField
    private String hdfm;

    /** 活动内容 */
    @Excel(name = "活动内容")
    private String hdnr;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date kssj;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date jssj;

    /** 开放程度 */
    @Excel(name = "开放程度")
    private String kfcd;

    /** 删除标志 */
    private String delFlag;

    private String status;

    private String tags;

    /** @type update   限定用户*/
    private List<TSignStudentWithCp> sysUsers;

    public List<TSignStudentWithCp> getSysUsers() {
        return sysUsers;
    }

    public TSignWithCp setSysUsers(List<TSignStudentWithCp> sysUsers) {
        this.sysUsers = sysUsers;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public TSignWithCp setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public TSignWithCp setStatus(String status) {
        this.status = status;
        return this;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setHdmc(String hdmc)
    {
        this.hdmc = hdmc;
    }

    public String getHdmc()
    {
        return hdmc;
    }
    public void setHdfmc(String hdfmc)
    {
        this.hdfmc = hdfmc;
    }

    public String getHdfmc()
    {
        return hdfmc;
    }
    public void setHdfm(String hdfm)
    {
        this.hdfm = hdfm;
    }

    public String getHdfm()
    {
        return hdfm;
    }
    public void setHdnr(String hdnr)
    {
        this.hdnr = hdnr;
    }

    public String getHdnr()
    {
        return hdnr;
    }
    public void setKssj(Date kssj)
    {
        this.kssj = kssj;
    }

    public Date getKssj()
    {
        return kssj;
    }
    public void setJssj(Date jssj)
    {
        this.jssj = jssj;
    }

    public Date getJssj()
    {
        return jssj;
    }
    public void setKfcd(String kfcd)
    {
        this.kfcd = kfcd;
    }

    public String getKfcd()
    {
        return kfcd;
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
                .append("hdmc", getHdmc())
                .append("hdfmc", getHdfmc())
                .append("hdfm", getHdfm())
                .append("hdnr", getHdnr())
                .append("kssj", getKssj())
                .append("jssj", getJssj())
                .append("kfcd", getKfcd())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
