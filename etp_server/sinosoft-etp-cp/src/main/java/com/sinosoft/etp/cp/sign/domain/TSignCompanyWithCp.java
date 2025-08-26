package com.sinosoft.etp.cp.sign.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 活动报名与企业关联表对象 t_sign_company
 *
 * 
 * @date 2022-02-10
 */
public class TSignCompanyWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 活动报名ID */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long signId;

    /** 企业ID */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long qyid;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setSignId(Long signId)
    {
        this.signId = signId;
    }

    public Long getSignId()
    {
        return signId;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("signId", getSignId())
                .append("qyid", getQyid())
                .toString();
    }
}
