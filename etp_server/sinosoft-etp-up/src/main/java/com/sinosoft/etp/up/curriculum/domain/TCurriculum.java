package com.sinosoft.etp.up.curriculum.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * curriculum对象 t_curriculum
 *
 *
 * @date 2021-11-15
 */
public class TCurriculum extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String mc;

    /** 课程描述 */
    @Excel(name = "课程描述")
    private String ms;

    /** 封面图 */
    @Excel(name = "封面图")
    @PreProcessField
    private String fmt;

    /** 开放程度 */
    @Excel(name = "开放程度")
    private String kfcd;

    /** 逻辑删除位 */
    private String delFlag;

    /** 状态 */
    private String states;

    /** 标签 */
    private String tags;

    private Long xyid;
    private Long qyid;
    private String jd;

    private String kcjd;
    /** 领取状态*/
    private String getStatus;
    private String getStatusLabel;
    private Long currentId;
    //领取课程的人数
    private int num;
    //是否推荐
    private String sftj;
    private Long categoryId;
    //排序 最近领取和最近更新
    private String order;

    private String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /** 课程内容 */
    private List<TCurriculumContent> tCurriculumContents;

    public List<TCurriculumContent> gettCurriculumContents() {
        return tCurriculumContents;
    }

    public TCurriculum settCurriculumContents(List<TCurriculumContent> tCurriculumContents) {
        this.tCurriculumContents = tCurriculumContents;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public TCurriculum setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public String getStates() {
        return states;
    }

    public TCurriculum setStates(String states) {
        this.states = states;
        return this;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setMc(String mc)
    {
        this.mc = mc;
    }

    public String getMc()
    {
        return mc;
    }
    public void setMs(String ms)
    {
        this.ms = ms;
    }

    public String getMs()
    {
        return ms;
    }
    public void setFmt(String fmt)
    {
        this.fmt = fmt;
    }

    public String getFmt()
    {
        return fmt;
    }
    public void setKfcd(String kfcd)
    {
        this.kfcd = kfcd;
    }

    public String getKfcd()
    {
        return kfcd;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public Long getXyid() {
        return xyid;
    }

    public void setXyid(Long xyid) {
        this.xyid = xyid;
    }

    public Long getQyid() {
        return qyid;
    }

    public void setQyid(Long qyid) {
        this.qyid = qyid;
    }

    public String getKcjd() {
        return kcjd;
    }

    public void setKcjd(String kcjd) {
        this.kcjd = kcjd;
    }

    public String getGetStatus() {
        return getStatus;
    }

    public void setGetStatus(String getStatus) {
        this.getStatus = getStatus;
    }

    public String getGetStatusLabel() {
        return getStatusLabel;
    }

    public void setGetStatusLabel(String getStatusLabel) {
        this.getStatusLabel = getStatusLabel;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getSftj() {
        return sftj;
    }

    public void setSftj(String sftj) {
        this.sftj = sftj;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mc", getMc())
            .append("ms", getMs())
            .append("fmt", getFmt())
            .append("kfcd", getKfcd())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("tags",getTags())
            .append("curriculumContents",gettCurriculumContents())
            .toString();
    }
}
