package com.sinosoft.user.exam.domain;

import com.sinosoft.core.domain.Exam;

import java.math.BigDecimal;
import java.util.List;

public class UserSimpleExam extends Exam {

    /** 学员考试记录 ID */
    private Long recordId;

    /** 学员已消耗考试机会 */
    private Integer usedChance;

    /** 试卷总分 */
    private BigDecimal totalScore;

    /** 试卷组成 */
    private List<UserPaperUnit> paperUnit;

    private List<UserExamRecord> records;

    public List<UserExamRecord> getRecords() {
        return records;
    }

    public void setRecords(List<UserExamRecord> records) {
        this.records = records;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Integer getUsedChance() {
        return usedChance;
    }

    public void setUsedChance(Integer usedChance) {
        this.usedChance = usedChance;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public List<UserPaperUnit> getPaperUnit() {
        return paperUnit;
    }

    public void setPaperUnit(List<UserPaperUnit> paperUnit) {
        this.paperUnit = paperUnit;
    }
}
