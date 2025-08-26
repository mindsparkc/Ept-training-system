package com.sinosoft.etp.common.progress.domain;


import com.alibaba.fastjson.JSONObject;

public class ProgressVo {
    private Integer completed;
    private Double rate;
    //完成时间
    private Long time;
    private JSONObject lastStudy;

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public JSONObject getLastStudy() {
        return lastStudy;
    }

    public void setLastStudy(JSONObject lastStudy) {
        this.lastStudy = lastStudy;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
