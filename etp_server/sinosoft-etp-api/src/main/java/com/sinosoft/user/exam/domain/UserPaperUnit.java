package com.sinosoft.user.exam.domain;

import java.math.BigDecimal;

/**
 * 学员获取试卷组成成分，爆开单元名称、题型、题目数量、单元分值
 *
 * @author wanglijie
 */
public class UserPaperUnit {
    /** 试卷单元编号 */
    private Long paperUnitId;
    /** 显示名称（默认为type的对应命名） */
    private String display;
    /** 题目类型 */
    private String quType;
    /** 题目数量 */
    private String quCount;
    /** 总分值 */
    private BigDecimal totalScore = BigDecimal.valueOf(0.0);
    /** 排序 */
    private Integer sort;

    public Long getPaperUnitId() {
        return paperUnitId;
    }

    public void setPaperUnitId(Long paperUnitId) {
        this.paperUnitId = paperUnitId;
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

    public String getQuCount() {
        return quCount;
    }

    public void setQuCount(String quCount) {
        this.quCount = quCount;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
