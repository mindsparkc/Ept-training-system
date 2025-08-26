package com.sinosoft.etp.cp.task.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 任务对象 t_task
 *
 *
 * @date 2021-12-08
 */
public class TTaskWithCp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String mc;

    /** 任务描述 */
    @Excel(name = "任务描述")
    private String ms;

    /** 封面图 */
    @Excel(name = "封面图")
    @PreProcessField
    private String fmt;

    /** 开放程度 0完全公开 1限定用户 2不公开 */
    @Excel(name = "开放程度 0完全公开 1限定用户 2不公开")
    private String kfcd;

    /** 任务开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskBeginTime;

    /** 任务结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskEndTime;

    /** 任务推送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "任务推送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskSendTime;

    /** 任务状态 */
    private String states;

    /** 删除标志 */
    private String delFlag;

    private String tags;

    private List<TTaskContentWithCp> taskContentList;

    private List<TTaskStudentWithCp> sysUsers;

    public List<TTaskStudentWithCp> getSysUsers() {
        return sysUsers;
    }

    public TTaskWithCp setSysUsers(List<TTaskStudentWithCp> sysUsers) {
        this.sysUsers = sysUsers;
        return this;
    }

    public List<TTaskContentWithCp> getTaskContentList() {
        return taskContentList;
    }

    public TTaskWithCp setTaskContentList(List<TTaskContentWithCp> taskContentList) {
        this.taskContentList = taskContentList;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public TTaskWithCp setTags(String tags) {
        this.tags = tags;
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
    public void setTaskBeginTime(Date taskBeginTime)
    {
        this.taskBeginTime = taskBeginTime;
    }

    public Date getTaskBeginTime()
    {
        return taskBeginTime;
    }
    public void setTaskEndTime(Date taskEndTime)
    {
        this.taskEndTime = taskEndTime;
    }

    public Date getTaskEndTime()
    {
        return taskEndTime;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public Date getTaskSendTime() {
        return taskSendTime;
    }

    public TTaskWithCp setTaskSendTime(Date taskSendTime) {
        this.taskSendTime = taskSendTime;
        return this;
    }

    public String getStates() {
        return states;
    }

    public TTaskWithCp setStates(String states) {
        this.states = states;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("mc", getMc())
                .append("ms", getMs())
                .append("fmt", getFmt())
                .append("kfcd", getKfcd())
                .append("states", getStates())
                .append("tags", getTags())
                .append("sysUsers", getSysUsers())
                .append("taskContents", getTaskContentList())
                .append("taskBeginTime", getTaskBeginTime())
                .append("taskEndTime", getTaskEndTime())
                .append("taskSendTime", getTaskSendTime())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
