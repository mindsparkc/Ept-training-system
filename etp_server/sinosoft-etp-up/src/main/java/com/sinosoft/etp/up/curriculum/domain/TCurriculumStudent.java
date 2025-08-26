package com.sinosoft.etp.up.curriculum.domain;

import com.sinosoft.common.core.domain.entity.SysUser;

/**
 * 课程与学员的关联实体类
 */
public class TCurriculumStudent extends SysUser {

    /** 主键 */
    private Long id;
    /** 课程ID*/
    private Long kcid;
    /** 学员ID*/
    private Long xyid;
    /** 学习进度*/
    private String jd;

    public Long getId() {
        return id;
    }

    public TCurriculumStudent setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getKcid() {
        return kcid;
    }

    public TCurriculumStudent setKcid(Long kcid) {
        this.kcid = kcid;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TCurriculumStudent setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }

    public String getJd() {
        return jd;
    }

    public TCurriculumStudent setJd(String jd) {
        this.jd = jd;
        return this;
    }

    @Override
    public String toString() {
        return "TCurriculumStudent{" +
                "id=" + id +
                ", kcid=" + kcid +
                ", xyid=" + xyid +
                ", jd='" + jd + '\'' +
                '}';
    }
}
