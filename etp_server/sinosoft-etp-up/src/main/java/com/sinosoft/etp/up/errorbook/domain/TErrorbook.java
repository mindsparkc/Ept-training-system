package com.sinosoft.etp.up.errorbook.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 错题集对象 t_errorbook
 *
 *
 * @date 2022-05-11
 */
public class TErrorbook extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 试题id */
    private Long stid;

    /** 课程id */
    private Long kcid;

    /** 考试id */
    private Long ksid;

    /** 学员id */
    private Long xyid;

    /** 题干 */
    @Excel(name = "题干")
    private String tg;

    /** 选项 ：仅单选、复选时有值。可以存json */
    @Excel(name = "选项 ：仅单选、复选时有值。可以存json")
    private String xx;

    /** 题目类型 */
    @Excel(name = "题目类型")
    private String stlx;

    /** 正确答案 */
    @Excel(name = "正确答案")
    private String da;

    /** 学员答案 */
    @Excel(name = "学员答案")
    private String answered;

    /** 题目解析 */
    @Excel(name = "题目解析")
    private String jx;

    /** 错题类型；1-考试，2-练习 */
    @Excel(name = "错题类型；1-考试，2-练习")
    private String ctlx;

    /** 备注 */
    @Excel(name = "备注")
    private String remake;

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
    public void setStid(Long stid)
    {
        this.stid = stid;
    }

    public Long getStid()
    {
        return stid;
    }
    public void setKcid(Long kcid)
    {
        this.kcid = kcid;
    }

    public Long getKcid()
    {
        return kcid;
    }
    public void setKsid(Long ksid)
    {
        this.ksid = ksid;
    }

    public Long getKsid()
    {
        return ksid;
    }
    public void setXyid(Long xyid)
    {
        this.xyid = xyid;
    }

    public Long getXyid()
    {
        return xyid;
    }
    public void setTg(String tg)
    {
        this.tg = tg;
    }

    public String getTg()
    {
        return tg;
    }
    public void setXx(String xx)
    {
        this.xx = xx;
    }

    public String getXx()
    {
        return xx;
    }
    public void setStlx(String stlx)
    {
        this.stlx = stlx;
    }

    public String getStlx()
    {
        return stlx;
    }
    public void setDa(String da)
    {
        this.da = da;
    }

    public String getDa()
    {
        return da;
    }
    public void setAnswered(String answered)
    {
        this.answered = answered;
    }

    public String getAnswered()
    {
        return answered;
    }
    public void setJx(String jx)
    {
        this.jx = jx;
    }

    public String getJx()
    {
        return jx;
    }
    public void setCtlx(String ctlx)
    {
        this.ctlx = ctlx;
    }

    public String getCtlx()
    {
        return ctlx;
    }
    public void setRemake(String remake)
    {
        this.remake = remake;
    }

    public String getRemake()
    {
        return remake;
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
            .append("stid", getStid())
            .append("kcid", getKcid())
            .append("ksid", getKsid())
            .append("xyid", getXyid())
            .append("tg", getTg())
            .append("xx", getXx())
            .append("stlx", getStlx())
            .append("da", getDa())
            .append("answered", getAnswered())
            .append("jx", getJx())
            .append("ctlx", getCtlx())
            .append("remake", getRemake())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
