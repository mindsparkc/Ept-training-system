package com.sinosoft.etp.cp.subject.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断输入的一行数据是什么？
 *
 * @author WANGLIJIE
 */
public class LineTypeRuling {
//    @Bean
//    public LineTypeRuling lineTypeRuling() {
//        return new LineTypeRuling();
//    }

    private String line;
    private Integer pos = 0;

    // 题干以 1. 或 1、或（1）开头的行
    private static final Pattern stemPattern = Pattern.compile("^[1-9]\\d*\\.|^[1-9]\\d*、|^[（,(][1-9]\\d*[）,)]");
    private static final Pattern optionsPattern = Pattern.compile("^[A-Z]\\、|^[A-Z]\\.");

    public LineTypeRuling(String line) {
        this.line = line.trim();
    }

    /**
     * 获取当前行类型
     *
     * @return
     */
    public LineType checkLineType() {
        if (line.startsWith("//")) {
            return LineType.notes;
        } else if (checkLineIsType()) {
            return LineType.type;
        } else if (checkLineIsStem()) {
            return LineType.stem;
        } else if (checkLineIsOptions()) {
            return LineType.options;
        } else if (checkLineIsAnswer()) {
            return LineType.answer;
        } else if (checkLineIsParse()) {
            return LineType.parse;
        } else if (checkLineIsLabel()) {
            return LineType.label;
        } else if (checkLineIsScope()) {
            return LineType.scope;
        }
        return LineType.undefined;
    }

    /**
     * 返回原字符串
     *
     * @return
     */
    public String getSourceText() {
        return line;
    }

    /**
     * 返回处理掉标识符之后的字符串
     *
     * @return
     */
    public String getText() {
        return line.substring(pos).trim();
    }

    /**
     * 判断是否是题型
     */
    private boolean checkLineIsType() {
        switch (line) {
            case "单选题":
            case "复选题":
            case "判断题":
            case "填空题":
            case "问答题":
                return true;
            default:
                return false;
        }
    }

    /**
     * 判断是否是题干
     *
     * @return
     */
    private boolean checkLineIsStem() {
        // 题干以 1. 或 1、 或 （1） 的形式出现，以正则表达式的形式判断
        Matcher matcher = stemPattern.matcher(line);
        boolean b = matcher.find();
        if (b) {
            pos = matcher.group().length();
        }
        return b;
    }


    //    options,  // 选项
    private boolean checkLineIsOptions() {
        Matcher matcher = optionsPattern.matcher(line);
        boolean b = matcher.find();
        if (b) {
            pos = matcher.group().length();
        }
        return b;
    }
    //    answer,   // 答案
    private boolean checkLineIsAnswer() {
        boolean b = line.startsWith("答案：") || line.startsWith("答案:");
        if (b) pos = 3;
        return b;
    }
    //    parse,    // 解析
    private boolean checkLineIsParse() {
        boolean b = line.startsWith("解析：") || line.startsWith("解析:");
        if (b) pos = 3;
        return b;
    }

    //    label,    // 标签
    private boolean checkLineIsLabel() {
        boolean b = line.startsWith("标签：") || line.startsWith("标签:");
        if (b) pos = 3;
        return b;
    }
    //    scope     // 应用范围
    private boolean checkLineIsScope() {
        boolean b = line.startsWith("范围：") || line.startsWith("范围:");
        if (b) pos = 3;
        return b;
    }

    public static String getSubjectTypeCode(String type) {
        switch (type) {
            case "单选题":
                return "1";
            case "复选题":
                return "2";
            case "判断题":
                return "3";
            case "填空题":
                return "4";
            case "问答题":
                return "5";
            default:
                return "0";
        }
    }
}
