package com.sinosoft.etp.cp.courseware.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 课件企业关联对象 t_courseware_company
 *
 *
 * @date 2021-11-12
 */
public class TCoursewareCompanyWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 课件ID
        关联T_COURSEWARE
        主键
     */
    @Excel(name = "课件ID")
    private Long kjid;

    /** 企业ID
        关联SYS_DEPT
        主键
     */
    @Excel(name = "企业ID")
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
    public void setKjid(Long kjid)
    {
        this.kjid = kjid;
    }

    public Long getKjid()
    {
        return kjid;
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
            .append("kjid", getKjid())
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
