package com.sinosoft.etp.up.questionnaire.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 问卷调查和标签关联表对象 t_questionnaire_tag
 *
 *
 * @date 2022-02-16
 */
public class TQuestionnaireTag extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 问卷id */
    @Excel(name = "问卷id")
    private Long wjid;

    /** 标签id */
    @Excel(name = "标签id")
    private Long bqid;

    /** 企业id */
    @Excel(name = "企业id")
    private Long qyid;

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
    public void setWjid(Long wjid)
    {
        this.wjid = wjid;
    }

    public Long getWjid()
    {
        return wjid;
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
            .append("wjid", getWjid())
            .append("bqid", getBqid())
            .append("qyid", getQyid())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
