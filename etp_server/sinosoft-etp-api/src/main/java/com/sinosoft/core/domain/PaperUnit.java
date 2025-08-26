package com.sinosoft.core.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * 代表一个试卷单元的组题规则，一个试卷单元可以有多个组题规则，存储在 unitRules 中
 * 每个组题规则可以是 1选题 2抽题 3随机 三种组卷策略，若是
 *  - 1选题 2抽题，需要指定 quId 及题目的 version（版本号）
 *  - 3随机，需要指定题库及各难度级别的题目数量
 *
 * @author wanglijie
 */
public class PaperUnit {
    private static final long serialVersionUID = 1L;

    /** 试卷单元编号 */
    private Long paperUnitId;
    /** 所属试卷编号，及试卷版本号 */
    private Long paperId;
    private Long paperVer;
    /** 显示名称（默认为type的对应命名） */
    private String display;
    /** 题目类型 */
    private String quType;
    /** 题目数量 */
    private Integer quCount;

    private List<LevelAndCount> lc;

    /** 总分值 */
    private BigDecimal totalScore = BigDecimal.valueOf(0.0);
    /** 学员得分 */
    private BigDecimal userScore;
    /** 每题的默认分值 */
    private BigDecimal eachScore = BigDecimal.valueOf(0.0);
    /** 题目排序策略（0正序 1随机） */
    private String quOrderFlag = "0";
    /** 候选答案排序策略（0正序 1随机） */
    private String answerOrderFlag = "0";
    /** 排序 */
    private Integer sort;
    /** 本单元组题规则 */
    private List<PaperUnitItem> unitRule;

    /** 组织的题目 */
    private List<PaperUnitItem> questions;


    public List<LevelAndCount> getLc() {
        return lc;
    }

    public void setLc(List<LevelAndCount> lc) {
        this.lc = lc;
    }

    public Long getPaperUnitId() {
        return paperUnitId;
    }

    public void setPaperUnitId(Long paperUnitId) {
        this.paperUnitId = paperUnitId;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Long getPaperVer() {
        return paperVer;
    }

    public void setPaperVer(Long paperVer) {
        this.paperVer = paperVer;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getQuType() {
        return quType;
    }

    public void setQuType(String quType) {
        this.quType = quType;
    }

    public Integer getQuCount() {
        return quCount;
    }

    public void setQuCount(Integer quCount) {
        this.quCount = quCount;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public BigDecimal getEachScore() {
        return eachScore;
    }

    public void setEachScore(BigDecimal eachScore) {
        this.eachScore = eachScore;
    }

    public String getQuOrderFlag() {
        return quOrderFlag;
    }

    public void setQuOrderFlag(String quOrderFlag) {
        this.quOrderFlag = quOrderFlag;
    }

    public String getAnswerOrderFlag() {
        return answerOrderFlag;
    }

    public void setAnswerOrderFlag(String answerOrderFlag) {
        this.answerOrderFlag = answerOrderFlag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<PaperUnitItem> getUnitRule() {
        return unitRule;
    }

    public void setUnitRule(List<PaperUnitItem> unitRule) {
        this.unitRule = unitRule;
    }

    public List<PaperUnitItem> getQuestions() {
        return questions;
    }

    public void setQuestions(List<PaperUnitItem> questions) {
        this.questions = questions;
    }

    public BigDecimal getUserScore() {
        return userScore;
    }

    public void setUserScore(BigDecimal userScore) {
        this.userScore = userScore;
    }
}
