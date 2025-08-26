package com.sinosoft.etp.up.testpaper.dto;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TSubjectExt extends BaseEntity {
    Double score;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 题干 */
    @Excel(name = "题干")
    private String tg;

    /** 类型：
     单选题：1
     复选题：2
     判断题：3
     填空题：4
     问答题：5 */
    @Excel(name = "类型")
    private String lx;
    /** 类型名称*/
    private String lxbq;

    /** 选项 ：仅单选、复选时有值。可以存json */
    @Excel(name = "选项")
    private String xx;

    // 填空题有几个空
    private Integer place;

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    private Integer tkid;
    private Integer sjid;
    private Integer sort;

    /** 是否练习
     是：1
     否：0 */
    @Excel(name = "是否练习")
    private String sflx;

    /** 是否考试 是：1
     否：0 */
    @Excel(name = "是否考试")
    private String sfks;

    /** 删除 */
    private String delFlag;
    private String tags;

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

    public void setSflx(String sflx)
    {
        this.sflx = sflx;
    }

    public Integer getTkid() {
        return tkid;
    }

    public void setTkid(Integer tkid) {
        this.tkid = tkid;
    }

    public Integer getSjid() {
        return sjid;
    }

    public void setSjid(Integer sjid) {
        this.sjid = sjid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLxbq() {
        return lxbq;
    }

    public void setLxbq(String lxbq) {
        this.lxbq = lxbq;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tg", getTg())
                .append("lx", getLx())
                .append("xx", getXx())
                .append("sflx", getSflx())
                .append("sfks", getSfks())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
