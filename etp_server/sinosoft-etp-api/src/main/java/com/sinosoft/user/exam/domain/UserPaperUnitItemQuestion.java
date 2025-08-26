package com.sinosoft.user.exam.domain;

import com.sinosoft.core.domain.Question;

/**
 * 根据试卷单元组成规则，获取到的题目
 *
 * 当抽题和选题组卷时，ruleId 实际存储的是 qu_id
 * 当为随机组卷时，ruleId 存储的为 PaperUnitItem#ruleId
 *
 * @author wanglijie
 */
public class UserPaperUnitItemQuestion extends Question {
    // 供随机组卷使用
    // 若是抽题或选题，则 key = quId
    // 若是随机，则 key = ruleId_level（题目难易程度），便于组卷时根据难易程度提取指定数量的试卷
    private String key;
    private Long ruleId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
}
