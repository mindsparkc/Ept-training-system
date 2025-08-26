package com.sinosoft.etp.cp.common.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 近七天的数据统计对象
 * @author 86151
 */
public class TAnalysisCountWithCp extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

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

    /** 近7天课件点击次数 */
    @Excel(name = "近7天课件点击次数")
    private Long coursewareClickCount;

    /** 近7天课程点击次数 */
    @Excel(name = "近7天课程点击次数")
    private Long curriculumClickCount;

    /** 近7天任务点击次数 */
    @Excel(name = "近7天任务点击次数")
    private Long taskClickCount;

    /** 近7天学员登录次数 */
    @Excel(name = "近7天学员登录次数")
    private Long userLoginCount;

    /** 近7天学习时长 */
    @Excel(name = "近7天学习时长")
    private Long studyTimeCount;

    /** 近7天平均学习时长 */
    @Excel(name = "近7天平均学习时长")
    private Long studyTimeAverage;

    /** 近7天视频类学习时长 */
    @Excel(name = "近7天视频类学习时长")
    private Long videoTimeCount;

    /** 近7天富文本学习时长 */
    @Excel(name = "近7天富文本学习时长")
    private Long textTimeCount;

    /** 近7天文件类学习时长 */
    @Excel(name = "近7天文件类学习时长")
    private Long fileTimeCount;

    /** 删除标志 */
    private String delFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
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
    public void setCoursewareClickCount(Long coursewareClickCount)
    {
        this.coursewareClickCount = coursewareClickCount;
    }

    public Long getCoursewareClickCount()
    {
        return coursewareClickCount;
    }
    public void setCurriculumClickCount(Long curriculumClickCount)
    {
        this.curriculumClickCount = curriculumClickCount;
    }

    public Long getCurriculumClickCount()
    {
        return curriculumClickCount;
    }
    public void setTaskClickCount(Long taskClickCount)
    {
        this.taskClickCount = taskClickCount;
    }

    public Long getTaskClickCount()
    {
        return taskClickCount;
    }
    public void setUserLoginCount(Long userLoginCount)
    {
        this.userLoginCount = userLoginCount;
    }

    public Long getUserLoginCount()
    {
        return userLoginCount;
    }
    public void setStudyTimeCount(Long studyTimeCount)
    {
        this.studyTimeCount = studyTimeCount;
    }

    public Long getStudyTimeCount()
    {
        return studyTimeCount;
    }
    public void setStudyTimeAverage(Long studyTimeAverage)
    {
        this.studyTimeAverage = studyTimeAverage;
    }

    public Long getStudyTimeAverage()
    {
        return studyTimeAverage;
    }
    public void setVideoTimeCount(Long videoTimeCount)
    {
        this.videoTimeCount = videoTimeCount;
    }

    public Long getVideoTimeCount()
    {
        return videoTimeCount;
    }
    public void setTextTimeCount(Long textTimeCount)
    {
        this.textTimeCount = textTimeCount;
    }

    public Long getTextTimeCount()
    {
        return textTimeCount;
    }
    public void setFileTimeCount(Long fileTimeCount)
    {
        this.fileTimeCount = fileTimeCount;
    }

    public Long getFileTimeCount()
    {
        return fileTimeCount;
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
                .append("id", getId())
                .append("deptId", getDeptId())
                .append("userId", getUserId())
                .append("kjid", getKjid())
                .append("kcid", getKcid())
                .append("rwid", getRwid())
                .append("coursewareClickCount", getCoursewareClickCount())
                .append("curriculumClickCount", getCurriculumClickCount())
                .append("taskClickCount", getTaskClickCount())
                .append("userLoginCount", getUserLoginCount())
                .append("studyTimeCount", getStudyTimeCount())
                .append("studyTimeAverage", getStudyTimeAverage())
                .append("videoTimeCount", getVideoTimeCount())
                .append("textTimeCount", getTextTimeCount())
                .append("fileTimeCount", getFileTimeCount())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
