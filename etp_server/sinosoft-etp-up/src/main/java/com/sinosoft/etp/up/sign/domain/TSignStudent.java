package com.sinosoft.etp.up.sign.domain;

import com.sinosoft.common.core.domain.entity.SysUser;

/**
 * 活动报名与学员关联实体类
 */
public class TSignStudent extends SysUser {

    private Long id;

    private Long hdid;

    private Long xyid;

    private String sfbm;

    public Long getId() {
        return id;
    }

    public TSignStudent setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getHdid() {
        return hdid;
    }

    public TSignStudent setHdid(Long hdid) {
        this.hdid = hdid;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TSignStudent setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }

    public String getSfbm() {
        return sfbm;
    }

    public TSignStudent setSfbm(String sfbm) {
        this.sfbm = sfbm;
        return this;
    }
}
