package com.sinosoft.etp.common.utils;

import java.util.HashMap;
import java.util.Map;

// 标签关联表， 标签表 ：t_tag
public  enum TableFiledEnum {
    WJ("t_questionnaire_tag","wjid"),
    HD("t_sign_tag","hdid"),
    RW("t_task_tag","rwid"),
    KC("t_curriculum_tag","kcid"),
    TK("t_subject_tag","tkid"),
    KJ("t_courseware_tag","kjid"),
    SJ("t_testpaper_tag","sjid"),
    KS("t_examination_tag","ksid"),
    ZS("t_certificate_tag","zsid");

    private String tableName;
    private String filed;


    private TableFiledEnum(String tableName, String filed){
        this.tableName = tableName;
        this.filed = filed;
    }


    public static String getTableNameStr(String type){
        for(TableFiledEnum item: TableFiledEnum.values()){
            if(type.equals(item.name())){
                return item.getTableName();
            }
        }
        return null;
    }

    public static String getFiledStr(String type){
        for(TableFiledEnum item: TableFiledEnum.values()){
            if(type.equals(item.name())){
                return item.getFiled();
            }
        }
        return null;
    }

    public static Map<String,String> tableFiledMap(String type){
        Map<String,String> map = new HashMap<>();
        for(TableFiledEnum item: TableFiledEnum.values()){
            if(type.equals(item.name())){
               map.put("tableName",item.getTableName());
               map.put("filed",item.getFiled());
               return map;
            }
        }
        return null;
    }
    public String getTableName() {
        return tableName;
    }

    public String getFiled() {
        return filed;
    }
}
