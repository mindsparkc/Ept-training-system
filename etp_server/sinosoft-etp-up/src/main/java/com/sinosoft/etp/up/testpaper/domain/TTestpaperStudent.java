package com.sinosoft.etp.up.testpaper.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 试卷学员关联对象 t_testpaper_student
 *
 *
 * @date 2022-02-28
 */
public class TTestpaperStudent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer id;

    /** 考试id，关联T_EXAMINATION主键 */
    @Excel(name = "考试id，关联T_EXAMINATION主键")
    private Long ksid;

    private String ksmc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ksjssj;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date kskssj;

    //答卷时间
    private Long djsc;

    //封面
    @PreProcessField
    private String fm;
    //分数等级
    private String fsdj;

    /** 试卷id */
    @Excel(name = "试卷id")
    private Long sjid;

    /** 学员id */
    @Excel(name = "学员id")
    private Long xyid;

    /** 学习进度 */
    @Excel(name = "学习进度")
    private String jd;

    /** 试卷快照 */
    @Excel(name = "试卷快照")
    private Integer snapshot;

    /** 试卷快照生成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "试卷快照生成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date snapCreateTime;

    /** 答案 */
    @Excel(name = "答案")
    private String da;

    /** 成绩 */
    @Excel(name = "成绩")
    private Long score;

    /** 正确率 */
    @Excel(name = "正确率")
    private Long rate;

    /** 交卷方式  交卷方式:0,未交卷;1,人工交卷;2.强制交卷*/
    private String handType;

    /** 交卷时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handTime;

    /** 删除 */
    private String delFlag;

    public Date getKskssj() {
        return kskssj;
    }

    public void setKskssj(Date kskssj) {
        this.kskssj = kskssj;
    }

    public String getFsdj() {
        return fsdj;
    }

    public void setFsdj(String fsdj) {
        this.fsdj = fsdj;
    }

    public Date getKsjssj() {
        return ksjssj;
    }

    public void setKsjssj(Date ksjssj) {
        this.ksjssj = ksjssj;
    }

    public Long getDjsc() {
        return djsc;
    }

    public void setDjsc(Long djsc) {
        this.djsc = djsc;
    }

    public String getFm() {
        return fm;
    }

    public void setFm(String fm) {
        this.fm = fm;
    }

    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
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
    public void setXyid(Long xyid)
    {
        this.xyid = xyid;
    }

    public Long getXyid()
    {
        return xyid;
    }
    public void setJd(String jd)
    {
        this.jd = jd;
    }

    public String getJd()
    {
        return jd;
    }
    public void setSnapshot(Integer snapshot)
    {
        this.snapshot = snapshot;
    }

    public Integer getSnapshot()
    {
        return snapshot;
    }
    public void setSnapCreateTime(Date snapCreateTime)
    {
        this.snapCreateTime = snapCreateTime;
    }

    public Date getSnapCreateTime()
    {
        return snapCreateTime;
    }
    public void setDa(String da)
    {
        this.da = da;
    }

    public String getDa()
    {
        return da;
    }
    public void setScore(Long score)
    {
        this.score = score;
    }

    public Long getScore()
    {
        return score;
    }
    public void setRate(Long rate)
    {
        this.rate = rate;
    }

    public Long getRate()
    {
        return rate;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getHandType() {
        return handType;
    }

    public void setHandType(String handType) {
        this.handType = handType;
    }

    public Date getHandTime() {
        return handTime;
    }

    public void setHandTime(Date handTime) {
        this.handTime = handTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ksid", getKsid())
            .append("sjid", getSjid())
            .append("xyid", getXyid())
            .append("jd", getJd())
            .append("snapshot", getSnapshot())
            .append("snapCreateTime", getSnapCreateTime())
            .append("da", getDa())
            .append("score", getScore())
            .append("rate", getRate())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
