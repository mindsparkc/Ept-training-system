package com.sinosoft.etp.up.curriculum.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 课程与企业关联对象 t_curriculum_company
 *
 * 
 * @date 2021-11-22
 */
public class TCurriculumCompany extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 关联T_CURRICULUM主键 课程ID */
    @Excel(name = "关联T_CURRICULUM主键 课程ID")
    private Long cid;

    /** 关联SYS_DEPT主键 企业ID */
    @Excel(name = "关联SYS_DEPT主键 企业ID")
    private Long qyid;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCid(Long cid)
    {
        this.cid = cid;
    }

    public Long getCid()
    {
        return cid;
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
                .append("cid", getCid())
                .append("qyid", getQyid())
                .toString();
    }
}
