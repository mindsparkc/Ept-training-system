package com.sinosoft.etp.up.curriculum.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 课程习题记录对象 t_curriculum_subject
 *
 *
 * @date 2022-05-09
 */
public class TCurriculumSubject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 课程id */
    @Excel(name = "课程id")
    private Long kcid;

    /** 学员id */
    @Excel(name = "学员id")
    private Long xyid;

    /** 试题id */
    @Excel(name = "试题id")
    private Long stid;

    /** 学员答案 */
    @Excel(name = "学员答案")
    private String xyda;

    /** 删除标记 */
    private String delFlag;

    /** 备注课件名称 */
    private String remake;

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setKcid(Long kcid)
    {
        this.kcid = kcid;
    }

    public Long getKcid()
    {
        return kcid;
    }
    public void setXyid(Long xyid)
    {
        this.xyid = xyid;
    }

    public Long getXyid()
    {
        return xyid;
    }
    public void setStid(Long stid)
    {
        this.stid = stid;
    }

    public Long getStid()
    {
        return stid;
    }
    public void setXyda(String xyda)
    {
        this.xyda = xyda;
    }

    public String getXyda()
    {
        return xyda;
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
            .append("kcid", getKcid())
            .append("xyid", getXyid())
            .append("stid", getStid())
            .append("xyda", getXyda())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
