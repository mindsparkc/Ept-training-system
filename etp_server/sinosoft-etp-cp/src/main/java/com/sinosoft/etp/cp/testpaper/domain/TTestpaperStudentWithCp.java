package com.sinosoft.etp.cp.testpaper.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 试卷关联学员对象 t_testpaper_student
 *
 * 
 * @date 2022-4-1
 */
public class TTestpaperStudentWithCp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 考试id */
    @Excel(name = "考试id")
    private Long ksid;

    /** 试卷id */
    @Excel(name = "试卷id")
    private Long sjid;

    /** 学员id */
    private Long xyid;

    /** 交卷时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handTime;

    /** 交卷方式 */
    private String handType;

    /** 答案 */
    private String da;

    /** 正确率 */
    private Double rate;

    /** 题目类型 */
    @Excel(name = "题目类型")
    private String type;

    /** 分值 */
    @Excel(name = "分值")
    private Integer score;

    /** 删除 */
    private String delFlag;

    /** 分数等级 */
    private String fsdj;

    public String getFsdj() {
        return fsdj;
    }

    public void setFsdj(String fsdj) {
        this.fsdj = fsdj;
    }

    public Long getXyid() {
        return xyid;
    }

    public void setXyid(Long xyid) {
        this.xyid = xyid;
    }

    public Date getHandTime() {
        return handTime;
    }

    public void setHandTime(Date handTime) {
        this.handTime = handTime;
    }

    public String getHandType() {
        return handType;
    }

    public void setHandType(String handType) {
        this.handType = handType;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setKsid(Long ksid)
    {
        this.ksid = ksid;
    }

    public Long getKsid()
    {
        return ksid;
    }
    public void setSjid(Long sjid)
    {
        this.sjid = sjid;
    }

    public Long getSjid()
    {
        return sjid;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setScore(Integer score)
    {
        this.score = score;
    }

    public Integer getScore()
    {
        return score;
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
            .append("ksid", getKsid())
            .append("sjid", getSjid())
            .append("type", getType())
            .append("score", getScore())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
