package com.sinosoft.etp.up.examination.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 考试企业关联对象 t_examination_company
 *
 *
 * @date 2021-12-28
 */
public class TExaminationCompany extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 考试id: T_EXAMINATION主键 */
    @Excel(name = "考试id: T_EXAMINATION 主键")
    private Long ksid;

    /** 企业ID：关联SYS_DEPT主键 */
    @Excel(name = "企业ID：关联SYS_DEPT主键")
    private Long qyid;

    /** 删除 */
    private String delFlag;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getKsid()
    {
        return ksid;
    }

    public void setKsid(Long ksid)
    {
        this.ksid = ksid;
    }

    public Long getQyid()
    {
        return qyid;
    }

    public void setQyid(Long qyid)
    {
        this.qyid = qyid;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ksid", getKsid())
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
