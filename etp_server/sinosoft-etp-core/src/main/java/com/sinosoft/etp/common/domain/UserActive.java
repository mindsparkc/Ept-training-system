package com.sinosoft.etp.common.domain;

import com.sinosoft.common.core.domain.BaseEntity;

public class UserActive extends BaseEntity {

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
}
