package com.sinosoft.etp.up.courseware.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 t_courseware_student
 *
 *
 * @date 2022-03-31
 */
public class TCoursewareStudent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 课件ID
关联T_COURSEWARE
主键 */
    private Long kjid;

    /** 学员ID
关联SYS_USER
主键 */
    private Long xyid;

    /** 进度 */
    @Excel(name = "进度")
    private String jd;

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
    public void setXyid(Long xyid)
    {
        this.xyid = xyid;
    }

    public Long getXyid()
    {
        return xyid;
    }
    public void setJd(String jd)
    {
        this.jd = jd;
    }

    public String getJd()
    {
        return jd;
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
            .append("xyid", getXyid())
            .append("jd", getJd())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
