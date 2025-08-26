package com.sinosoft.etp.up.testpaper.vo;

import com.sinosoft.etp.up.examination.domain.TExamination;
import com.sinosoft.etp.up.testpaper.domain.TTestpaper;
import com.sinosoft.etp.up.testpaper.dto.TSubjectAnswered;

import java.util.ArrayList;
import java.util.List;

/**
 * 试卷快照，应用于用户点击“开始考试”，加载试卷
 */
public class PaperSnapshotVo {
    /**
     * 试卷学员关联表 ID
     */
    private Integer id;
    /**
     * 试卷快照 ID
     */
    private Integer snapshot;
    /**
     * 取自 T_EXAMINATION 表中的信息，为当前考试的基本信息
     */
    private TExamination examination;

    /**
     * 取自 T_TEST_PAPER 表中的信息，为当前试卷的基本信息
     */
    private TTestpaper testpaper;

    /**
     * 以下为试卷的组题信息，包含题目信息、学生答案
     */
    private List<TSubjectAnswered> sj;

    /**
     * 试卷总分数
     */
    private Double totalScore;

    /**
     * 答案的正确率
     */
    private Integer rate;

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

    public List<TSubjectAnswered> getSj() {
        return sj;
    }

    public void setSj(List<TSubjectAnswered> sj) {
        this.sj = sj;
    }

    public void addQue(TSubjectAnswered subject) {
        if (this.sj == null) {
            this.sj = new ArrayList<>();
        }
        this.sj.add(subject);
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}
