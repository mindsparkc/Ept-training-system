package com.sinosoft.etp.cp.task.domain;

import com.sinosoft.common.core.domain.entity.SysUser;

public class TTaskStudentWithCp extends SysUser {

    /** 主键 */
    private Long id;
    /** 课程ID*/
    private Long rwid;
    /** 学员ID*/
    private Long xyid;
    /** 学习进度*/
    private String jd;

    public Long getId() {
        return id;
    }

    public TTaskStudentWithCp setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getRwid() {
        return rwid;
    }

    public TTaskStudentWithCp setRwid(Long rwid) {
        this.rwid = rwid;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TTaskStudentWithCp setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }

    public String getJd() {
        return jd;
    }

    public TTaskStudentWithCp setJd(String jd) {
        this.jd = jd;
        return this;
    }
}
