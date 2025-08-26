package com.sinosoft.etp.cp.certificate.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 证书企业关联对象 t_certificate_company
 *
 *
 * @date 2022-02-08
 */
public class TCertificateCompanyWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 证书id */
    @Excel(name = "证书id")
    private Long zsid;

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
            .append("qyid", getQyid())
            .toString();
    }
}
