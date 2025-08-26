package com.sinosoft.etp.cp.questionnaire.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 问卷调查和企业关联表对象 t_questionnaire_company
 *
 *
 * @date 2022-02-15
 */
public class TQuestionnaireCompanyWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 问卷ID */
    @Excel(name = "问卷ID")
    private Long wjid;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long qyid;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setWjid(Long wjid)
    {
        this.wjid = wjid;
    }

    public Long getWjid()
    {
        return wjid;
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
                .append("wjid", getWjid())
                .append("qyid", getQyid())
                .toString();
    }
}
