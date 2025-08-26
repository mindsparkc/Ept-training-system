package com.sinosoft.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 试卷组卷策略
 *      1 选题组卷
 *      2 抽题组卷
 *      3 随机组卷
 *
 * @author wanglijie
 */
public enum PaperStrategyEnum {
    CHOOSE("1", "选题组卷"),
    EXTRACT("2","抽题组卷"),
    RANDOM("3", "随机组卷");

    String code;
    String name;

    PaperStrategyEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, PaperStrategyEnum> keyMap = new HashMap<>();

    static {
        for (PaperStrategyEnum item : PaperStrategyEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    public static PaperStrategyEnum fromCode(Integer code) {
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
