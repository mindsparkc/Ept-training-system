package com.sinosoft.etp.up.questionnaire.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 问卷学生关联对象 t_questionnaire_student
 *
 *
 * @date 2022-02-16
 */
public class TQuestionnaireStudent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 问卷id */
    @Excel(name = "问卷id")
    private Long wjid;

    /** 问卷内容 */
    @Excel(name = "问卷内容")
    private String wjnr;

    /** 学员id */
    @Excel(name = "学员id")
    private Long xyid;

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
    public void setWjnr(String wjnr)
    {
        this.wjnr = wjnr;
    }

    public String getWjnr()
    {
        return wjnr;
    }
    public void setXyid(Long xyid)
    {
        this.xyid = xyid;
    }

    public Long getXyid()
    {
        return xyid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("wjid", getWjid())
            .append("wjnr", getWjnr())
            .append("xyid", getXyid())
            .toString();
    }
}
