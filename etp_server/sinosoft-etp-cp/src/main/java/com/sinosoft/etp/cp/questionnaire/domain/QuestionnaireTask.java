package com.sinosoft.etp.cp.questionnaire.domain;

import com.alibaba.fastjson.JSONObject;
import com.sinosoft.common.core.domain.BaseEntity;

public class QuestionnaireTask extends BaseEntity {
    private static final long serialVersionUID = 1L;


    private Long id;
    /** 问卷ID */
    private Long wjid;

    /** 题目ID */
    private String tmid;
    /** 题目类型 */
    private String type;
    /** 题干 */
    private String tg;
    /** 选项 */
    private String option;
    /** 选项名称 */
    private String optionName;
    /** 问答题 */
    private String wdda;
    /** 选择数量 */
    private int num;
    /** 各个答案的数量-- 单选和复选使用*/
    private JSONObject daNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWjid() {
        return wjid;
    }

    public void setWjid(Long wjid) {
        this.wjid = wjid;
    }

    public String getTmid() {
        return tmid;
    }

    public void setTmid(String tmid) {
        this.tmid = tmid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getWdda() {
        return wdda;
    }

    public void setWdda(String wdda) {
        this.wdda = wdda;
    }
}
