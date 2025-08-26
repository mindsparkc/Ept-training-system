package com.sinosoft.core.domain;

public class PaperUnitItem extends Question {
    /** 试卷规则ID */
    private Long ruleId;
    /** 所属试卷编号 */
    private Long paperId;
    /** 试卷单元编号 */
    private Long paperUnitId;
    /** 组卷策略（1选题组卷 2抽题组卷 3随机组卷） */
    private String strategy;
    /** 分值 分值,如果是随机抽题，指定随机每道题的得分*/
//    private BigDecimal score;

    /** 组卷规则{catId:[1,2],knowledgeId:[1,2],score:13, rule:[{level:2, count: 3}, {level: 1, count: 4}]} */
    private PaperUnitItemRule rule;

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Long getPaperUnitId() {
        return paperUnitId;
    }

    public void setPaperUnitId(Long paperUnitId) {
        this.paperUnitId = paperUnitId;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

//    public BigDecimal getScore() {
//        return score;
//    }
//
//    public void setScore(BigDecimal score) {
//        this.score = score;
//    }

    public PaperUnitItemRule getRule() {
        return rule;
    }

    public void setRule(PaperUnitItemRule rule) {
        this.rule = rule;
    }
}