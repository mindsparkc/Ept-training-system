package com.sinosoft.etp.cp.subject.utils;

/**
 * 遍历 Word 时，判断每一行的类型
 */
public enum LineType {
    undefined, // 未定义的类型
    type,     // 题目类型
    notes,    // 注释
    stem,     // 题干
    options,  // 选项
    answer,   // 答案
    parse,    // 解析
    label,    // 标签
    scope     // 应用范围
}
