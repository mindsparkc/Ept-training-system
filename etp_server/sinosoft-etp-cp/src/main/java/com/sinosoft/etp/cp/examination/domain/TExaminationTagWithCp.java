package com.sinosoft.etp.cp.examination.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 考试标签关联对象 t_examination_tag
 *
 * 
 * @date 2021-12-28
 */
public class TExaminationTagWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 考试id：关联T_EXAMINATION主键 */
    @Excel(name = "考试id：关联T_EXAMINATION主键")
    private Long ksid;

    /** 标签id：关联T_TAG主键 */
    @Excel(name = "标签id：关联T_TAG主键")
    private Long bqid;

    /** 企业id：关联SYS_DEPT主键。运营端登记的标签为：0 */
    @Excel(name = "企业id：关联SYS_DEPT主键。运营端登记的标签为：0")
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

    public Long getBqid()
    {
        return bqid;
    }

    public void setBqid(Long bqid)
    {
        this.bqid = bqid;
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
            .append("bqid", getBqid())
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
