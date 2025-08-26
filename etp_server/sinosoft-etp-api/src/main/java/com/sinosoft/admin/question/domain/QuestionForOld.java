package com.sinosoft.admin.question.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 题库对象 t_subject
 *
 *
 * @author 86151
 * @date 2021-11-08
 */
public class QuestionForOld extends BaseEntity {
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

    /** 选项 ：仅单选、复选时有值。可以存json */
    @Excel(name = "选项")
    private String xx;

    /** 正确答案 */
    @Excel(name = "正确答案")
    private String da;

    /** 答案解析 */
    @Excel(name = "答案解析")
    private String jx;

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
    private String beginTime;
    private String endTime;
    private String tags;
    private Long categoryId;
    private String categoryName;
    //难易程度
    private String level;
    //纯文字题干
    private String tgDisplat;

    public String getTgDisplat() {
        return tgDisplat;
    }

    public void setTgDisplat(String tgDisplat) {
        this.tgDisplat = tgDisplat;
    }

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
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
