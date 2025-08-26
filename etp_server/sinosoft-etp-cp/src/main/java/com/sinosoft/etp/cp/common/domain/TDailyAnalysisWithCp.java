package com.sinosoft.etp.cp.common.domain;


import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 每日统计分析类
 * @author 86151
 */
public class TDailyAnalysisWithCp extends BaseEntity {

    /** 主键id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 课件id */
    @Excel(name = "课件id")
    private Long kjid;

    /** 课程id */
    @Excel(name = "课程id")
    private Long kcid;

    /** 任务id */
    @Excel(name = "任务id")
    private Long rwid;

    /** 每日课件点击次数 */
    @Excel(name = "每日课件点击次数")
    private Integer dailyCoursewareClicks;

    /** 每日课程点击次数 */
    @Excel(name = "每日课程点击次数")
    private Integer dailyCurriculumClicks;

    /** 每日任务点击次数 */
    @Excel(name = "每日任务点击次数")
    private Integer dailyTaskClicks;

    /** 每日学员登录次数 */
    @Excel(name = "每日学员登录次数")
    private Integer dailyUserLogin;

    /** 每日视频类学习时长 */
    @Excel(name = "每日视频类学习时长")
    private String dailyVideoStudyTime;

    /** 每日富文本类学习时长 */
    @Excel(name = "每日富文本类学习时长")
    private String dailyRichtextStudyTime;

    /** 每日文件类学习时长 */
    @Excel(name = "每日文件类学习时长")
    private String dailyFileStudyTime;

    /** 每日用户学习时长 */
    @Excel(name = "每日用户学习时长")
    private String dailyUserStudyTime;

    /** 删除标记  0：未删除，1：已删除   */
    private String delFlag;

    /** 统计日期 */
    @Excel(name = "统计日期")
    private String specificDate;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setKjid(Long kjid)
    {
        this.kjid = kjid;
    }

    public Long getKjid()
    {
        return kjid;
    }
    public void setKcid(Long kcid)
    {
        this.kcid = kcid;
    }

    public Long getKcid()
    {
        return kcid;
    }
    public void setRwid(Long rwid)
    {
        this.rwid = rwid;
    }

    public Long getRwid()
    {
        return rwid;
    }
    public void setDailyCoursewareClicks(Integer dailyCoursewareClicks)
    {
        this.dailyCoursewareClicks = dailyCoursewareClicks;
    }

    public Integer getDailyCoursewareClicks()
    {
        return dailyCoursewareClicks;
    }
    public void setDailyCurriculumClicks(Integer dailyCurriculumClicks)
    {
        this.dailyCurriculumClicks = dailyCurriculumClicks;
    }

    public Integer getDailyCurriculumClicks()
    {
        return dailyCurriculumClicks;
    }
    public void setDailyTaskClicks(Integer dailyTaskClicks)
    {
        this.dailyTaskClicks = dailyTaskClicks;
    }

    public Integer getDailyTaskClicks()
    {
        return dailyTaskClicks;
    }
    public void setDailyUserLogin(Integer dailyUserLogin)
    {
        this.dailyUserLogin = dailyUserLogin;
    }

    public Integer getDailyUserLogin()
    {
        return dailyUserLogin;
    }
    public void setDailyVideoStudyTime(String dailyVideoStudyTime)
    {
        this.dailyVideoStudyTime = dailyVideoStudyTime;
    }

    public String getDailyVideoStudyTime()
    {
        return dailyVideoStudyTime;
    }
    public void setDailyRichtextStudyTime(String dailyRichtextStudyTime)
    {
        this.dailyRichtextStudyTime = dailyRichtextStudyTime;
    }

    public String getDailyRichtextStudyTime()
    {
        return dailyRichtextStudyTime;
    }
    public void setDailyFileStudyTime(String dailyFileStudyTime)
    {
        this.dailyFileStudyTime = dailyFileStudyTime;
    }

    public String getDailyFileStudyTime()
    {
        return dailyFileStudyTime;
    }
    public void setDailyUserStudyTime(String dailyUserStudyTime)
    {
        this.dailyUserStudyTime = dailyUserStudyTime;
    }

    public String getDailyUserStudyTime()
    {
        return dailyUserStudyTime;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }
    public void setSpecificDate(String specificDate)
    {
        this.specificDate = specificDate;
    }

    public String getSpecificDate()
    {
        return specificDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("kjid", getKjid())
                .append("kcid", getKcid())
                .append("rwid", getRwid())
                .append("dailyCoursewareClicks", getDailyCoursewareClicks())
                .append("dailyCurriculumClicks", getDailyCurriculumClicks())
                .append("dailyTaskClicks", getDailyTaskClicks())
                .append("dailyUserLogin", getDailyUserLogin())
                .append("dailyVideoStudyTime", getDailyVideoStudyTime())
                .append("dailyRichtextStudyTime", getDailyRichtextStudyTime())
                .append("dailyFileStudyTime", getDailyFileStudyTime())
                .append("dailyUserStudyTime", getDailyUserStudyTime())
                .append("delFlag", getDelFlag())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("specificDate", getSpecificDate())
                .toString();
    }
}
