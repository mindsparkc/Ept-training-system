package com.sinosoft.etp.cp.subjectResource.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 题库资源管理对象 t_subjectresource
 *
 * @author wyx
 * @date 2022-02-24
 */
public class TSubjectResourceWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 题干 */
    @Excel(name = "题干")
    private String tg;

    /** 题型 */
    @Excel(name = "题型")
    private String lx;

    /** 选项 */
    @Excel(name = "选项")
    private String xx;

    /** 正确答案 */
    @Excel(name = "正确答案")
    private String da;

    /** 解析 */
    @Excel(name = "解析")
    private String jx;

    /** 是否为练习是：1否：0
 */
    @Excel(name = "是否为练习是：1否：0 ")
    private String sflx;

    /** 是否为考试 是：1否：0
 */
    @Excel(name = "是否为考试 是：1否：0 ")
    private String sfks;

    /** 来源 1：用户服务中心 2：培训管理中心 */
    @Excel(name = "来源 1：用户服务中心 2：培训管理中心")
    private String ly;

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
    public void setTg(String tg)
    {
        this.tg = tg;
    }

    public String getTg()
    {
        return tg;
    }
    public void setLx(String lx)
    {
        this.lx = lx;
    }

    public String getLx()
    {
        return lx;
    }
    public void setXx(String xx)
    {
        this.xx = xx;
    }

    public String getXx()
    {
        return xx;
    }
    public void setDa(String da)
    {
        this.da = da;
    }

    public String getDa()
    {
        return da;
    }
    public void setJx(String jx)
    {
        this.jx = jx;
    }

    public String getJx()
    {
        return jx;
    }
    public void setSflx(String sflx)
    {
        this.sflx = sflx;
    }

    public String getSflx()
    {
        return sflx;
    }
    public void setSfks(String sfks)
    {
        this.sfks = sfks;
    }

    public String getSfks()
    {
        return sfks;
    }
    public void setLy(String ly)
    {
        this.ly = ly;
    }

    public String getLy()
    {
        return ly;
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
            .append("tg", getTg())
            .append("lx", getLx())
            .append("xx", getXx())
            .append("da", getDa())
            .append("jx", getJx())
            .append("sflx", getSflx())
            .append("sfks", getSfks())
            .append("ly", getLy())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
