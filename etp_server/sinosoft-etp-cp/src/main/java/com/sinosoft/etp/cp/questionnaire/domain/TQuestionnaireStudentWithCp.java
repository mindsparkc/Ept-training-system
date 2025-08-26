package com.sinosoft.etp.cp.questionnaire.domain;

import com.sinosoft.common.core.domain.entity.SysUser;

public class TQuestionnaireStudentWithCp extends SysUser {

    private Long id;

    private Long wjid;

    private String wjnr;

    private Long xyid;

    public Long getId() {
        return id;
    }

    public TQuestionnaireStudentWithCp setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getWjid() {
        return wjid;
    }

    public TQuestionnaireStudentWithCp setWjid(Long wjid) {
        this.wjid = wjid;
        return this;
    }

    public String getWjnr() {
        return wjnr;
    }

    public TQuestionnaireStudentWithCp setWjnr(String wjnr) {
        this.wjnr = wjnr;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TQuestionnaireStudentWithCp setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }
}
