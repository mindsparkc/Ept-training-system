package com.sinosoft.etp.up.testpaper.dto;

import com.sinosoft.etp.up.examination.domain.TExamination;
import com.sinosoft.etp.up.testpaper.domain.TTestpaper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 试卷快照，以 JSON 形式存储在快照表（T_TESTPAPER_SNAPSHOT）中
 *
 * @author WANGLIJIE
 */
public class PaperSnapshotDTO {
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
     * 以下为试卷的组题信息，包含试题答案及解析、学生答案、得分等信息
     */
    private LinkedHashMap<String, TSubjectAnsweredReview> sj;

    private TestPaperReviewDetail detail;

    /**
     * 答案的正确率
     */
    private Integer rate;

    /**
     * 试卷总分数
     */
    private Double totalScore;

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

    public Map<String, TSubjectAnsweredReview> getSj() {
        return sj;
    }

    public void setSj(LinkedHashMap<String, TSubjectAnsweredReview> sj) {
        this.sj = sj;
    }

    public void addQuestions(TSubjectAnsweredReview qu) {
        if (this.sj == null) {
            this.sj = new LinkedHashMap<>();
        }
        this.sj.put(qu.getId().toString(), qu);
    }

    public TestPaperReviewDetail getDetail() {
        return detail;
    }

    public void setDetail(TestPaperReviewDetail detail) {
        this.detail = detail;
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
