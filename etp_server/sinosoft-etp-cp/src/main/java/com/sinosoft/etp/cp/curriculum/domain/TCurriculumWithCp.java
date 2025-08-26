package com.sinosoft.etp.cp.curriculum.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * curriculum对象 t_curriculum
 *
 *
 * @date 2021-11-15
 */
public class TCurriculumWithCp extends BaseEntity
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

    /** 学习人数*/
    private Long regNum;
    private Long categoryId;
    private String categoryName;

    //是否推荐
    private String sftj;

    /** 关联分类 */
    List<List<Long>> categoryIds;

    /** 指定用户*/
    private List<TCurriculumStudentWithCp> sysUsers;

    /** 课程内容 */
    private List<TCurriculumContentWithCp> tCurriculumContents;

    public List<TCurriculumStudentWithCp> getSysUsers() {
        return sysUsers;
    }

    public TCurriculumWithCp setSysUsers(List<TCurriculumStudentWithCp> sysUsers) {
        this.sysUsers = sysUsers;
        return this;
    }

    public Long getRegNum() {
        return regNum;
    }

    public TCurriculumWithCp setRegNum(Long regNum) {
        this.regNum = regNum;
        return this;
    }

    public List<TCurriculumContentWithCp> gettCurriculumContents() {
        return tCurriculumContents;
    }

    public TCurriculumWithCp settCurriculumContents(List<TCurriculumContentWithCp> tCurriculumContents) {
        this.tCurriculumContents = tCurriculumContents;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public TCurriculumWithCp setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public String getStates() {
        return states;
    }

    public TCurriculumWithCp setStates(String states) {
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

    public String getSftj() {
        return sftj;
    }

    public void setSftj(String sftj) {
        this.sftj = sftj;
    }
    public boolean hasCategoryIds() {
        return categoryIds!=null && categoryIds.size()>0;
    }

    public List<List<Long>> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<List<Long>> categoryIds) {
        this.categoryIds = categoryIds;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mc", getMc())
            .append("ms", getMs())
            .append("fmt", getFmt())
            .append("kfcd", getKfcd())
            .append("sysUsers", getSysUsers())
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
