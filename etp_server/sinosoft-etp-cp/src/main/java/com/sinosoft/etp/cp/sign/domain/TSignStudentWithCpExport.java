package com.sinosoft.etp.cp.sign.domain;

import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;

/**
 * 活动报名与学员关联实体类
 */
public class TSignStudentWithCpExport extends BaseEntity {

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

    public TSignStudentWithCpExport setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getHdid() {
        return hdid;
    }

    public TSignStudentWithCpExport setHdid(Long hdid) {
        this.hdid = hdid;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TSignStudentWithCpExport setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }

    public String getSfbm() {
        return sfbm;
    }

    public TSignStudentWithCpExport setSfbm(String sfbm) {
        this.sfbm = sfbm;
        return this;
    }
}
