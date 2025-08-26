package com.sinosoft.user.exam.domain;

import com.sinosoft.core.domain.Exam;
import com.sinosoft.core.domain.Paper;
import com.sinosoft.core.domain.PaperUnit;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 学员请求开始考试时，返回给前端的试卷结构
 *
 * @author wanglijie
 */
public class UserExam extends Exam {
    /** 学员考试记录 ID */
    private Long recordId;
    /** 标记是否是一次新的考试，若为 true 则是一次新 */
//    private boolean
    /** 学员已消耗考试机会 */
    private Integer usedChance;

    /** 试卷总分 */
    private BigDecimal totalScore;

    /** 学员得分 */
    private BigDecimal userScore;

    private List<PaperUnit> paperUnit;

    private Integer quCount;

    /** 答对题目数量 */
    private Integer quCorrect;

    private Paper paper;

    private Date reviewTime;
    private Long reviewUserId;


    public Integer getQuCorrect() {
        return quCorrect;
    }

    public void setQuCorrect(Integer quCorrect) {
        this.quCorrect = quCorrect;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Long getReviewUserId() {
        return reviewUserId;
    }

    public void setReviewUserId(Long reviewUserId) {
        this.reviewUserId = reviewUserId;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
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

    public List<PaperUnit> getPaperUnit() {
        return paperUnit;
    }

    public void setPaperUnit(List<PaperUnit> paperUnit) {
        this.paperUnit = paperUnit;
    }

    public BigDecimal getUserScore() {
        return userScore;
    }

    public void setUserScore(BigDecimal userScore) {
        this.userScore = userScore;
    }

    public Integer getQuCount() {
        return quCount;
    }

    public void setQuCount(Integer quCount) {
        this.quCount = quCount;
    }
}
