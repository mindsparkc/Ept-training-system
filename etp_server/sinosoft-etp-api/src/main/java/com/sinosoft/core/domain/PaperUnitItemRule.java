package com.sinosoft.core.domain;

import java.util.List;

public class PaperUnitItemRule {
    /** 为了兼容一个规则多个分类 ---- 暂时不生效 */
    private List<List<Long>> catIds;
    /** 一个规则，一个分类 */
    private List<Long> catId;
    /** 在网页中显示的分类名 */
    private String catDisplay;
    /** 知识点组卷，暂未启用 */
    private List<Long> knowledgeId;
    /** 各难度级别题目数量 */
    private List<LevelAndCount> lc;

    public List<List<Long>> getCatIds() {
        return catIds;
    }

    public void setCatIds(List<List<Long>> catIds) {
        this.catIds = catIds;
    }

    public List<Long> getCatId() {
        return catId;
    }

    public void setCatId(List<Long> catId) {
        this.catId = catId;
    }

    public String getCatDisplay() {
        return catDisplay;
    }

    public void setCatDisplay(String catDisplay) {
        this.catDisplay = catDisplay;
    }

    public List<Long> getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(List<Long> knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public List<LevelAndCount> getLc() {
        return lc;
    }

    public void setLc(List<LevelAndCount> lc) {
        this.lc = lc;
    }
}
