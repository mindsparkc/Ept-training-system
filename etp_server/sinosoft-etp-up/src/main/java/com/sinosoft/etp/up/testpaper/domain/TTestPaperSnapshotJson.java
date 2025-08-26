package com.sinosoft.etp.up.testpaper.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.etp.up.examination.domain.TExamination;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TTestPaperSnapshotJson extends BaseEntity {

    /**
     * T_TESTPAPER_STUDENT 试卷学员关联表的ID  在返回数据给 WEB 及 APP 的时候添加 返回查询  试卷信息 给前端时添加
     */
    private Integer id;

    /**
     *  试卷快照 ID，在返回数据给 WEB 及 APP 的时候添加 返回查询  试卷信息 给前端时添加
     */
    private Integer snapshot;

    /**
     * 以下信息为考试信息
     *  即 T_EXAMINATION 的字段及其值
     */
    private TExamination examination;

    /**
     * 以下为试卷的基础信息
     */
    private TTestpaper testpaper;

    /**
     * 试卷的附加信息
     */
    //试卷是生成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create;

    /**
     * 以下为试卷组题信息
     */
    private List<Map<String, Object>> sj;


    /**
     * 以下为 T_SUBJECT(题库)中题目的正确答案及解析
     */
    private List<Map<String, Object>> jx;
    /**
     * 用户提交的答案
     */
    private Map<String, Object> da;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Integer snapshot) {
        this.snapshot = snapshot;
    }

    public TExamination getExamination() {
        return examination;
    }

    public void setExamination(TExamination examination) {
        this.examination = examination;
    }

    public TTestpaper getTestpaper() {
        return testpaper;
    }

    public void setTestpaper(TTestpaper testpaper) {
        this.testpaper = testpaper;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public List<Map<String, Object>> getSj() {
        return sj;
    }

    public void setSj(List<Map<String, Object>> sj) {
        this.sj = sj;
    }

    public List<Map<String, Object>> getJx() {
        return jx;
    }

    public void setJx(List<Map<String, Object>> jx) {
        this.jx = jx;
    }

    public Map<String, Object> getDa() {
        return da;
    }

    public void setDa(Map<String, Object> da) {
        this.da = da;
    }
}
