package com.sinosoft.etp.cp.sign.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.entity.SysUser;

/**
 * 活动报名与学员关联实体类
 */
public class TSignStudentWithCp extends SysUser {

    private Long id;

    private Long hdid;

    @Excel(name = "学员ID")
    private Long xyid;

    @Excel(name = "学员姓名")
    private String xyName;

    @Excel(name = "是否报名")
    private String sfbm;


    public String getXyName() {
        return xyName;
    }


    public void setXyName(String xyName) {
        this.xyName = xyName;
    }

    public Long getId() {
        return id;
    }

    public TSignStudentWithCp setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getHdid() {
        return hdid;
    }

    public TSignStudentWithCp setHdid(Long hdid) {
        this.hdid = hdid;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TSignStudentWithCp setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }

    public String getSfbm() {
        return sfbm;
    }

    public TSignStudentWithCp setSfbm(String sfbm) {
        this.sfbm = sfbm;
        return this;
    }
}
