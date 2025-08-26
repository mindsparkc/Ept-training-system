package com.sinosoft.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 开放规则，适用于考试、课程、任务等
 * 1公开 2限定部门 3限定岗位 4限定成员 5口令密码
 *
 * @author wanglijie
 */
public enum OpenTypeEnum {
    PUBLIC("1", "公开"),
    DEPT("2","限定部门"),
    POST("3", "限定岗位"),
    USER("4", "限定成员"),
    PASSWD("5", "口令密码");

    String code;
    String name;

    OpenTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, OpenTypeEnum> keyMap = new HashMap<>();

    static {
        for (OpenTypeEnum item : OpenTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    public static OpenTypeEnum fromCode(Integer code) {
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
