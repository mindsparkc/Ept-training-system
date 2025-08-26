package com.sinosoft.etp.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.core.domain.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 86151
 */
@Component
public class TEventLog extends BaseEntity {

    /** 主键id */
    private Long id;
    /** 事件类型 */
    private String eventType;
    /** 课件id */
    private Long kjid;
    /** 课程id */
    private Long kcid;
    /** 任务id */
    private Long rwid;
    /** 考试id */
    private Long ksid;
    /** 学习记录 */
    private String offset;
    /** 触发时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date triggerTime;
    /** 学习时长 */
    private String studyDuration;
    /** 用户id */
    private Long userId;
    /** 年月日 */
    private String specificDate;
    /** 统计数量 */
    private Integer counts;


    public Integer getCounts() {
        return counts;
    }

    public void setCount(Integer counts) {
        this.counts = counts;
    }

    public String getSpecificDate() {
        return specificDate;
    }

    public void setSpecificDate(String specificDate) {
        this.specificDate = specificDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getKjid() {
        return kjid;
    }

    public void setKjid(Long kjid) {
        this.kjid = kjid;
    }

    public Long getKcid() {
        return kcid;
    }

    public void setKcid(Long kcid) {
        this.kcid = kcid;
    }

    public Long getRwid() {
        return rwid;
    }

    public void setRwid(Long rwid) {
        this.rwid = rwid;
    }

    public Long getKsid() {
        return ksid;
    }

    public void setKsid(Long ksid) {
        this.ksid = ksid;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getStudyDuration() {
        return studyDuration;
    }

    public void setStudyDuration(String studyDuration) {
        this.studyDuration = studyDuration;
    }
}
