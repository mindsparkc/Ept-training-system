package com.sinosoft.etp.up.task.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * 任务内容项实体类
 *
 * */
public class TTaskCatalog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;
    /** 任务ID*/
    private String mc;
    /** 任务描述 */
    @Excel(name = "任务描述")
    private String ms;

    /** 任务开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date taskBeginTime;

    /** 任务结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date taskEndTime;

    private List<TSource> sources;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public List<TSource> getSources() {
        return sources;
    }

    public void setSources(List<TSource> sources) {
        this.sources = sources;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public Date getTaskBeginTime() {
        return taskBeginTime;
    }

    public void setTaskBeginTime(Date taskBeginTime) {
        this.taskBeginTime = taskBeginTime;
    }

    public Date getTaskEndTime() {
        return taskEndTime;
    }

    public void setTaskEndTime(Date taskEndTime) {
        this.taskEndTime = taskEndTime;
    }

}
