package com.sinosoft.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 成绩显示（0不显示成绩 1立即显示 2批改后显示 3定时发布 4人工发布）
 */
public enum ExamShowScoreEnum {
    NoShowScore("0", "不显示成绩"),
    ImShowScore("1", "立即显示"),
    Judgmented("2", "批改后显示"),
    TimingPublished("3", "定时发布"),
    ManualPublished("4", "人工发布");

    String code;
    String name;

    ExamShowScoreEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, ExamShowScoreEnum> keyMap = new HashMap<>();

    static {
        for (ExamShowScoreEnum item : ExamShowScoreEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    public static ExamShowScoreEnum fromCode(Integer code) {
        return keyMap.get(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
