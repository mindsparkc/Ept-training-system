package com.sinosoft.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 考试信息对象 exam
 *
 * @author ruoyi
 * @date 2022-09-04
 */
public class Exam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 考试编号 */
    private Long examId;

    /** 考试名称 */
    @Excel(name = "考试名称")
    private String name;

    /** 考试描述 */
    @Excel(name = "考试描述")
    private String content;

    /** 封面 */
    @PreProcessField
    @Excel(name = "封面")
    private String cover;

    /** 开放策略（1公开 2部门 3岗位 4限定成员 5口令密码） */
    @Excel(name = "开放策略", readConverterExp = "1=公开,2=部门,3=岗位,4=限定成员 5口令密码")
    private String openType;

    /** 考试口令密码（在数据库中使用 BCrypt 对口令进行加密） */
    @Excel(name = "考试口令密码", readConverterExp = "在=数据库中使用,B=Crypt,对=口令进行加密")
    private String password;

    /** 是否开启口令，开启口令后只有输入密码才能进行考试 */
    private boolean pwdFlag;

    /** 试卷编号 */
    @Excel(name = "试卷编号")
    private Long paperId;

    /** 考试状态（1暂存 2发布 3正常结束 4强制结束），允许管理员强制结束已经开始的考试，学员只能搜索到已发布的考试 */
    @Excel(name = "考试状态", readConverterExp = "1=暂存,2=发布,3=正常结束,4=强制结束")
    private Integer status;

    /** 考试限时（0不限时 限制N[&gt;0]分钟） */
    @Excel(name = "考试限时", readConverterExp = "0=不限时,限=制N[&gt;0]分钟")
    private Integer totalTime;

    /** 最低交卷时长（0不限时 限制N[&gt;0]分钟） */
    @Excel(name = "最低交卷时长", readConverterExp = "0=不限时,限=制N[&gt;0]分钟")
    private Integer handMinTime;

    /** 允许考试次数 */
    @Excel(name = "允许考试次数")
    private Integer chance;

    /** 及格分 */
    @Excel(name = "及格分")
    private BigDecimal qualifyScore;

    /** 是否限制考试开始时间（1限制 2不限制），若限制考试开始时间，则需要填写lat_time和start_time */
    @Excel(name = "是否限制考试开始时间", readConverterExp = "1=限制,2=不限制")
    private String timeLimit;

    /** 考试发布时间（若不限制考试开始时间，则考试发布即能开始考试） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "考试发布时间", readConverterExp = "若=不限制考试开始时间，则考试发布即能开始考试")
    private Date publishTime;

    /** 考试开始时间（仅在限制考试开始时间时有效） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "考试开始时间", readConverterExp = "仅=在限制考试开始时间时有效")
    private Date startTime;

    /** 考试结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "考试结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 允许迟到时长(分钟) */
    @Excel(name = "允许迟到时长(分钟)")
    private Integer lateTime;

    /** 允许离开考试页面次数 */
    @Excel(name = "允许离开考试页面次数")
    private Integer leaveLimit;

    /** 允许无操作时间（分钟） */
    @Excel(name = "允许无操作时间", readConverterExp = "分=钟")
    private Integer noActLimit;

    /** 成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布） */
    @Excel(name = "成绩显示", readConverterExp = "0=不显示成绩,1=立即显示,2=批改后显示,3=定时发布,4=人工发布")
    private String showScore;

    /** 考试成绩公布时间，如果是定时发布，则该字段必填 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date showScoreTime;

    /** 查看试卷（0不允许查看 1不显示答案及解析 2仅显示答案 3显示答案及解析） */
    @Excel(name = "查看试卷", readConverterExp = "0=不允许查看,1=不显示答案及解析,2=仅显示答案,3=显示答案及解析")
    private String showExam;

    /** 显示试卷评语（0不显示 1显示） */
    @Excel(name = "显示试卷评语", readConverterExp = "0=不显示,1=显示")
    private String showComment;

    /** 答题设备（0不限 1仅PC 2仅手机） */
    @Excel(name = "答题设备", readConverterExp = "0=不限,1=仅PC,2=仅手机")
    private String answerDriver;

    /** 数据归属用户 */
    @Excel(name = "数据归属用户")
    private Long userId;

    /** 数据归属部门 */
    @Excel(name = "数据归属部门")
    private Long deptId;

    /** 删除标记（0有效 2删除） */
    private String delFlag;

    public void setExamId(Long examId)
    {
        this.examId = examId;
    }

    public Long getExamId()
    {
        return examId;
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
    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public String getCover()
    {
        return cover;
    }
    public void setOpenType(String openType)
    {
        this.openType = openType;
    }

    public String getOpenType()
    {
        return openType;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean isPwdFlag() {
        return pwdFlag;
    }

    public void setPwdFlag(boolean pwdFlag) {
        this.pwdFlag = pwdFlag;
    }

    public void setPaperId(Long paperId)
    {
        this.paperId = paperId;
    }

    public Long getPaperId()
    {
        return paperId;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setTotalTime(Integer totalTime)
    {
        this.totalTime = totalTime;
    }

    public Integer getTotalTime()
    {
        return totalTime;
    }
    public void setHandMinTime(Integer handMinTime)
    {
        this.handMinTime = handMinTime;
    }

    public Integer getHandMinTime()
    {
        return handMinTime;
    }
    public void setChance(Integer chance)
    {
        this.chance = chance;
    }

    public Integer getChance()
    {
        return chance;
    }
    public void setQualifyScore(BigDecimal qualifyScore)
    {
        this.qualifyScore = qualifyScore;
    }

    public BigDecimal getQualifyScore()
    {
        return qualifyScore;
    }
    public void setTimeLimit(String timeLimit)
    {
        this.timeLimit = timeLimit;
    }

    public String getTimeLimit()
    {
        return timeLimit;
    }
    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setLateTime(Integer lateTime)
    {
        this.lateTime = lateTime;
    }

    public Integer getLateTime()
    {
        return lateTime;
    }
    public void setLeaveLimit(Integer leaveLimit)
    {
        this.leaveLimit = leaveLimit;
    }

    public Integer getLeaveLimit()
    {
        return leaveLimit;
    }
    public void setNoActLimit(Integer noActLimit)
    {
        this.noActLimit = noActLimit;
    }

    public Integer getNoActLimit()
    {
        return noActLimit;
    }
    public void setShowScore(String showScore)
    {
        this.showScore = showScore;
    }

    public String getShowScore()
    {
        return showScore;
    }

    public Date getShowScoreTime() {
        return showScoreTime;
    }

    public void setShowScoreTime(Date showScoreTime) {
        this.showScoreTime = showScoreTime;
    }

    public void setShowExam(String showExam)
    {
        this.showExam = showExam;
    }

    public String getShowExam()
    {
        return showExam;
    }
    public void setShowComment(String showComment)
    {
        this.showComment = showComment;
    }

    public String getShowComment()
    {
        return showComment;
    }
    public void setAnswerDriver(String answerDriver)
    {
        this.answerDriver = answerDriver;
    }

    public String getAnswerDriver()
    {
        return answerDriver;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("examId", getExamId())
            .append("name", getName())
            .append("content", getContent())
            .append("cover", getCover())
            .append("openType", getOpenType())
            .append("password", getPassword())
            .append("paperId", getPaperId())
            .append("status", getStatus())
            .append("totalTime", getTotalTime())
            .append("handMinTime", getHandMinTime())
            .append("chance", getChance())
            .append("qualifyScore", getQualifyScore())
            .append("timeLimit", getTimeLimit())
            .append("publishTime", getPublishTime())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("lateTime", getLateTime())
            .append("leaveLimit", getLeaveLimit())
            .append("noActLimit", getNoActLimit())
            .append("showScore", getShowScore())
            .append("showExam", getShowExam())
            .append("showComment", getShowComment())
            .append("answerDriver", getAnswerDriver())
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
