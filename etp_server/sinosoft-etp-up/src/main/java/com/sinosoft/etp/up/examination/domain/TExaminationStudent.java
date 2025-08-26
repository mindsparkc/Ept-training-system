package com.sinosoft.etp.up.examination.domain;

import com.sinosoft.common.core.domain.entity.SysUser;

/**
 * @type create
 * @auther cfl
 * 2021-12-31
 * 考试与学员的关联实体类
 */
public class TExaminationStudent extends SysUser {

    /** 主键 */
    private Long id;
    /** 考试ID*/
    private Long ksid;
    /** 学员ID*/
    private Long xyid;
    /** 学习进度*/
    private String jd;

    public Long getId() {
        return id;
    }

    public TExaminationStudent setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getKsid() {
        return ksid;
    }

    public TExaminationStudent setKsid(Long ksid) {
        this.ksid = ksid;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TExaminationStudent setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }

    public String getJd() {
        return jd;
    }

    public TExaminationStudent setJd(String jd) {
        this.jd = jd;
        return this;
    }
}
