package com.sinosoft.core.domain;

/**
 * 难度级别及题目数量
 */
public class LevelAndCount {
    /** 难度级别 */
    String level;
    /** 题目数量 */
    Integer count;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}