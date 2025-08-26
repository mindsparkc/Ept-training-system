package com.sinosoft.etp.cp.certificate.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 证书管理对象 t_certificate
 *
 *
 * @date 2022-02-07
 */
public class TCertificateWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 证书中文名 */
    @Excel(name = "证书中文名")
    private String mc;

    /** 证书英文名 */
    @Excel(name = "证书英文名")
    private String enmc;

    /** 证书描述 */
    @Excel(name = "证书描述")
    private String ms;

    /** 证书底图 */
    @Excel(name = "证书底图")
    @PreProcessField
    private String dt;

    /** 证书类型 */
    @Excel(name = "证书类型")
    private String lx;

    /** 删除标志 */
    private String delFlag;

    /** 关联标签 */
    private String tags;

    public String getTags(){
        return tags;
    }

    public void setTags(String tags){
        this.tags = tags;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMc(String mc)
    {
        this.mc = mc;
    }

    public String getMc()
    {
        return mc;
    }
    public void setEnmc(String enmc)
    {
        this.enmc = enmc;
    }

    public String getEnmc()
    {
        return enmc;
    }
    public void setMs(String ms)
    {
        this.ms = ms;
    }

    public String getMs()
    {
        return ms;
    }
    public void setDt(String dt)
    {
        this.dt = dt;
    }

    public String getDt()
    {
        return dt;
    }
    public void setLx(String lx)
    {
        this.lx = lx;
    }

    public String getLx()
    {
        return lx;
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
            .append("id", getId())
            .append("mc", getMc())
            .append("enmc", getEnmc())
            .append("ms", getMs())
            .append("dt", getDt())
            .append("lx", getLx())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
