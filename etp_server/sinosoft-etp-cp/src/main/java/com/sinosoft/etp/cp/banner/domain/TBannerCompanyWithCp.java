package com.sinosoft.etp.cp.banner.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * banner与企业关联表对象 t_banner_company
 *
 *
 * @date 2022-03-09
 */
public class TBannerCompanyWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** bannerid */
    @Excel(name = "bannerid")
    private Long bid;

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
    public void setBid(Long bid)
    {
        this.bid = bid;
    }

    public Long getBid()
    {
        return bid;
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
                .append("bid", getBid())
                .append("qyid", getQyid())
                .toString();
    }
}
