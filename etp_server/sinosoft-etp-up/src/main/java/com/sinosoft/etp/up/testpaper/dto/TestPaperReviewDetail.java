package com.sinosoft.etp.up.testpaper.dto;

import java.util.Map;

/**
 * 试卷评卷详情
 *
 * @author WANGLIJIE
 */
public class TestPaperReviewDetail {
    /**
     * 题型
     */
    private Integer type;
    /**
     * 题型名称
     */
    private String typeName;
    /**
     * 总题目数量
     */
    private Integer totalNum=0;
    /**
     * 正确题目数量
     */
    private Integer correctNum=0;

    /**
     * 试卷总分数
     */
    private Long totalScore=0L;
    /**
     * 学员得分
     */
    private Long score=0L;
    /**
     * 级别
     */
    private String level;

    private Map<String, TestPaperReviewDetail> details;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(Integer correctNum) {
        this.correctNum = correctNum;
    }

    public void addCorrectNum(Integer totalNum) {
        this.correctNum +=totalNum ;
    }

    public Integer getTotalNum(int size) {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public void addTotalNum(Integer totalNum) {
        this.totalNum +=totalNum ;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public void addScore(Long score) {
        this.score += score;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public void addTotalScore(Long score) {
        this.totalScore += score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public Map<String, TestPaperReviewDetail> getDetails() {
        return details;
    }

    public void setDetails(Map<String, TestPaperReviewDetail> details) {
        this.details = details;
    }
}
