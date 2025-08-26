package com.sinosoft.etp.cp.subject.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 题库企业关联对象 t_subject_company
 *
 * 
 * @date 2021-11-12
 */
public class TSubjectCompanyWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 题库id: 关联T_SUBJECT主键 */
    @Excel(name = "题库id: 关联T_SUBJECT主键")
    private Long tkid;

    /** 企业ID：关联SYS_DEPT主键 */
    @Excel(name = "企业ID：关联SYS_DEPT主键")
    private Long qyid;

    /** 删除 */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTkid(Long tkid)
    {
        this.tkid = tkid;
    }

    public Long getTkid()
    {
        return tkid;
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
            .append("tkid", getTkid())
            .append("qyid", getQyid())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
