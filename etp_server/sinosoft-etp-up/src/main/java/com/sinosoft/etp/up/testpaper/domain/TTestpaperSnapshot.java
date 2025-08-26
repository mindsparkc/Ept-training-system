package com.sinosoft.etp.up.testpaper.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 试卷快照
对象 t_testpaper_snapshot
 *
 *
 * @date 2022-02-28
 */
public class TTestpaperSnapshot extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer id;

    /** 考试id，关联T_EXAMINATION主键 */
    @Excel(name = "考试id，关联T_EXAMINATION主键")
    private Long ksid;

    /** 试卷id */
    @Excel(name = "试卷id")
    private Long sjid;

    /** 试卷快照 */
    @Excel(name = "试卷快照")
    private String snapshot;

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

    /** 删除 */
    private String delFlag;

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
    public void setSnapshot(String snapshot)
    {
        this.snapshot = snapshot;
    }

    public String getSnapshot()
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ksid", getKsid())
            .append("sjid", getSjid())
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
