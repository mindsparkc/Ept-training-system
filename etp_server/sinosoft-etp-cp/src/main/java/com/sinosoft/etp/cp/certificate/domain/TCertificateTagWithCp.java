package com.sinosoft.etp.cp.certificate.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 管理证书标签关联对象 t_certificate_tag
 *
 *
 * @date 2022-02-08
 */
public class TCertificateTagWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 证书id */
    @Excel(name = "证书id")
    private Long zsid;

    /** 标签id */
    @Excel(name = "标签id")
    private Long bqid;

    /** 企业id */
    @Excel(name = "企业id")
    private Long qyid;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setZsid(Long zsid)
    {
        this.zsid = zsid;
    }

    public Long getZsid()
    {
        return zsid;
    }
    public void setBqid(Long bqid)
    {
        this.bqid = bqid;
    }

    public Long getBqid()
    {
        return bqid;
    }
    public void setQyid(Long qyid)
    {
        this.qyid = qyid;
    }

    public Long getQyid()
    {
        return qyid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("zsid", getZsid())
            .append("bqid", getBqid())
            .append("qyid", getQyid())
            .toString();
    }
}
