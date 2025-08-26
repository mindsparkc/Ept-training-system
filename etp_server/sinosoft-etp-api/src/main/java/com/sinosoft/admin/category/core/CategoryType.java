package com.sinosoft.admin.category.core;

import java.util.HashMap;
import java.util.Map;

public class CategoryType {
    private static Map<CategoryEnum, String> map1 = new HashMap<>();
    private static Map<CategoryEnum, String> map2 = new HashMap<>();
    static {
        // 资源(课件 课程等)分类表名
        map1.put(CategoryEnum.QUESTION_CATEGORY, "question_category");
        map1.put(CategoryEnum.PAPER_CATEGORY, "paper_category");
        map1.put(CategoryEnum.EXAM_CATEGORY, "exam_category");
        map1.put(CategoryEnum.COURSEWARE_CATEGORY, "courseware_category");
        map1.put(CategoryEnum.CURRICULUM_CATEGORY, "curriculum_category");
        map1.put(CategoryEnum.KNOWLEDGE_CATEGORY, "knowledge_category");
        map1.put(CategoryEnum.TRAIN_CATEGORY, "train_category");

        // 资源(课件 课程等)和资源分类进行关联的表名
        map2.put(CategoryEnum.QUESTION_CATEGORY, "question_inter_category");
        map2.put(CategoryEnum.PAPER_CATEGORY, "paper_inter_category");
        map2.put(CategoryEnum.EXAM_CATEGORY, "exam_inter_category");
        map2.put(CategoryEnum.COURSEWARE_CATEGORY, "courseware_inter_category");
        map2.put(CategoryEnum.CURRICULUM_CATEGORY, "curriculum_inter_category");
        map2.put(CategoryEnum.KNOWLEDGE_CATEGORY, "knowledge_inter_category");
        map2.put(CategoryEnum.TRAIN_CATEGORY, "train_inter_category");
    }

    public static String getTableName(CategoryEnum categoryEnum) {
        return map1.get(categoryEnum);
    }

    public static String getInterTableName(CategoryEnum categoryEnum) {
        return map2.get(categoryEnum);
    }

    public enum CategoryEnum {
        QUESTION_CATEGORY,
        PAPER_CATEGORY,
        EXAM_CATEGORY,
        COURSEWARE_CATEGORY,
        CURRICULUM_CATEGORY,
        KNOWLEDGE_CATEGORY,
        TRAIN_CATEGORY
    }
}
