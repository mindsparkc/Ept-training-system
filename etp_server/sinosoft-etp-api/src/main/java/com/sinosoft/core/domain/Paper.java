package com.sinosoft.core.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷基础信息对象 paper
 *
 *
 * @date 2022-08-24
 */
public class Paper extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 试卷编号 */
    private Long paperId;

    /** 版本号（Linux时间戳） */
    @Excel(name = "版本号", readConverterExp = "L=inux时间戳")
    private Long version;

    /** 试卷名称 */
    @Excel(name = "试卷名称")
    private String name;

    /** 考试描述 */
    @Excel(name = "考试描述")
    private String content;

    /** 组卷策略（1抽题组卷 2选题组卷 3随机组卷 4自定义组卷） */
    @Excel(name = "组卷策略", readConverterExp = "1=抽题组卷,2=选题组卷,3=随机组卷,4=自定义组卷")
    private String strategy;

    /** 试卷总分 */
    @Excel(name = "试卷总分")
    private BigDecimal totalScore;

    /** 建议时长(0不限时；限定N[&gt;0]分钟) */
    @Excel(name = "建议时长(0不限时；限定N[&gt;0]分钟)")
    private Long suggestTime;

    /** 及格分 */
    @Excel(name = "及格分")
    private BigDecimal qualifyScore;

    /** 题目数量 */
    @Excel(name = "题目数量")
    private Integer quCount;

    private List<LevelAndCount> lc;

    /** 试卷状态（0暂存 99发布） */
    @Excel(name = "试卷状态", readConverterExp = "0=暂存,9=9发布")
    private Long status;

    /** 数据归属用户 */
    @Excel(name = "数据归属用户")
    private Long userId;

    /** 数据归属部门 */
    @Excel(name = "数据归属部门")
    private Long deptId;

    /** 删除标记（0有效 2删除） */
    private String delFlag;

    /** 试卷被考试关联的次数 */
    private Integer relateNum;

    private String flag;

    public Integer getRelateNum() {
        return relateNum;
    }

    public void setRelateNum(Integer relateNum) {
        this.relateNum = relateNum;
    }

    public List<LevelAndCount> getLc() {
        return lc;
    }

    public void setLc(List<LevelAndCount> lc) {
        this.lc = lc;
    }

    public void setPaperId(Long paperId)
    {
        this.paperId = paperId;
    }

    public Long getPaperId()
    {
        return paperId;
    }
    public void setVersion(Long version)
    {
        this.version = version;
    }

    public Long getVersion()
    {
        return version;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setStrategy(String strategy)
    {
        this.strategy = strategy;
    }

    public String getStrategy()
    {
        return strategy;
    }
    public void setTotalScore(BigDecimal totalScore)
    {
        this.totalScore = totalScore;
    }

    public BigDecimal getTotalScore()
    {
        return totalScore==null?BigDecimal.ZERO:totalScore;
    }

    public void setSuggestTime(Long suggestTime)
    {
        this.suggestTime = suggestTime;
    }

    public Long getSuggestTime()
    {
        return suggestTime;
    }
    public void setQualifyScore(BigDecimal qualifyScore)
    {
        this.qualifyScore = qualifyScore;
    }

    public BigDecimal getQualifyScore()
    {
        return qualifyScore;
    }
    public void setQuCount(Integer quCount)
    {
        this.quCount = quCount;
    }

    public Integer getQuCount()
    {
        return quCount;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("paperId", getPaperId())
            .append("version", getVersion())
            .append("name", getName())
            .append("content", getContent())
            .append("strategy", getStrategy())
            .append("totalScore", getTotalScore())
            .append("suggestTime", getSuggestTime())
            .append("qualifyScore", getQualifyScore())
            .append("quCount", getQuCount())
            .append("status", getStatus())
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
