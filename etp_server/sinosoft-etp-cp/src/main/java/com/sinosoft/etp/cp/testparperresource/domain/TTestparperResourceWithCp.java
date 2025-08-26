package com.sinosoft.etp.cp.testparperresource.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 试卷资源管理对象 t_testparperresource
 *
 * @author wyx
 * @date 2022-02-24
 */
public class TTestparperResourceWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 试卷名称 */
    @Excel(name = "试卷名称")
    private String sjmc;

    /** 组卷策略
（0-自选题组卷、1-随机选题组卷、2-随机生成考卷）
 */
    @Excel(name = "组卷策略 ", readConverterExp = "0=-自选题组卷、1-随机选题组卷、2-随机生成考卷")
    private String zjcl;

    /** $column.columnComment */
    @Excel(name = "组卷策略 ", readConverterExp = "$column.readConverterExp()")
    private String fm;

    /** [
{key:单选题,value:12},
{key:复选题,value:10},
{key:填空题,value:23},
]
 */
    @Excel(name = "[ {key:单选题,value:12}, {key:复选题,value:10}, {key:填空题,value:23}, ] ")
    private String tx;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private String sjly;
    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setSjmc(String sjmc)
    {
        this.sjmc = sjmc;
    }

    public String getSjmc()
    {
        return sjmc;
    }
    public void setZjcl(String zjcl)
    {
        this.zjcl = zjcl;
    }

    public String getZjcl()
    {
        return zjcl;
    }
    public void setFm(String fm)
    {
        this.fm = fm;
    }

    public String getFm()
    {
        return fm;
    }
    public void setTx(String tx)
    {
        this.tx = tx;
    }

    public String getTx()
    {
        return tx;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getSjly() {
        return sjly;
    }

    public void setSjly(String sjly) {
        this.sjly = sjly;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sjmc", getSjmc())
            .append("zjcl", getZjcl())
            .append("fm", getFm())
            .append("tx", getTx())
            .append("sjly", getSjly())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
