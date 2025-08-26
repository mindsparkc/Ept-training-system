package com.sinosoft.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 1-单选题；2-复选题；3-判断题；4-填空题；5-问答题；6-组合题
 */
public enum QuestionTypeEnum {
    SINGLE_CHOICE("1", "单选题"),
    MULTIPLE_CHOICE("2","多选题"),
    TRUE_FALSE("3", "判断题"),
    FILL_BLANK("4", "填空题"),
    ASK("5", "问答题"),
    COMBINATORIAL("6", "组合题");

    String code;
    String name;

    QuestionTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, QuestionTypeEnum> keyMap = new HashMap<>();

    static {
        for (QuestionTypeEnum item : QuestionTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    public static QuestionTypeEnum fromCode(Integer code) {
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
