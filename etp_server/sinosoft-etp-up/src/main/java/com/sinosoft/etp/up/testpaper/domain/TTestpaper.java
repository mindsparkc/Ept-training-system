package com.sinosoft.etp.up.testpaper.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 试卷对象 t_testpaper
 *
 *
 * @date 2021-11-19
 */
public class TTestpaper extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 试卷名称 */
    @Excel(name = "试卷名称")
    private String sjmc;

    /** 组卷策略： 0-自选题组卷、1-随机选题组卷、2-随机生成考卷 */
    @Excel(name = "组卷策略")
    private String zjcl;

    /** 封面 */
    private String fm;

    /** 发布状态：0-暂存，1-发布 */
    @Excel(name = "发布状态")
    private String fb;
    /** 题型及数量 [
     {key:单选题,value:12},
     {key:复选题,value:10},
     {key:填空题,value:23},
     ]*/
    private String tx;
    /** 删除 */
    private String delFlag;
    private String tags;
    private String tagFlag;


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
    public void setFb(String fb)
    {
        this.fb = fb;
    }

    public String getFb()
    {
        return fb;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTagFlag() {
        return tagFlag;
    }

    public void setTagFlag(String tagFlag) {
        this.tagFlag = tagFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sjmc", getSjmc())
            .append("zjcl", getZjcl())
            .append("fm", getFm())
            .append("fb", getFb())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
