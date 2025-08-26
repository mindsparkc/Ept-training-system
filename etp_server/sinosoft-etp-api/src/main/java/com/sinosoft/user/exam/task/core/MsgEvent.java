package com.sinosoft.user.exam.task.core;

import java.util.concurrent.TimeUnit;

/**
 * 事件消息，用以触发交卷
 *
 * @author wanglijie
 */
public class MsgEvent {
    private Long recordId;

    private String remark;

    private long delay;    // 多长时间之后出发
    private TimeUnit unit; // 时间单位

    public MsgEvent(Long recordId, String remark, long delay) {
        this.recordId = recordId;
        this.remark = remark;
        this.delay = delay;
        this.unit = TimeUnit.SECONDS;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "MsgEvent{" +
                "recordId=" + recordId +
                ", remark='" + remark + '\'' +
                ", delay=" + delay +
                ", unit=" + unit +
                '}';
    }
}
