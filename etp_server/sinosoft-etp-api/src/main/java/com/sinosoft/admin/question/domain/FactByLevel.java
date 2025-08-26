package com.sinosoft.admin.question.domain;

public class FactByLevel {
    private static final long serialVersionUID = 1L;

    private String level;
    private Integer count;

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
