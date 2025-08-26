package com.sinosoft.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 试卷状态（0未交卷 1未判卷 3未发布 9已完成）
 *
 * 试卷状态
 * @author wanglijie
 */
public enum ExamRecordStatusEnum {
    UNDELIVERED("0", "未交卷"),
    NOT_JUDGED("1", "已交卷但未判卷"),
    UNPUBLISHED("4", "已交卷但未发布成绩"),
    GIVEUP("8", "放弃考试"),
    DONE("9", "已完成");


    String code;
    String name;

    ExamRecordStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, ExamRecordStatusEnum> keyMap = new HashMap<>();

    static {
        for (ExamRecordStatusEnum item : ExamRecordStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    public static ExamRecordStatusEnum fromCode(Integer code) {
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
