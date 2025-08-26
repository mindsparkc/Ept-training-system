package com.sinosoft.etp.cp.analysis.domain;

import com.sinosoft.common.core.domain.BaseEntity;

import java.util.HashMap;

public class SubjectAndExaminationWithCp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 试题id */
    private Long id;
    /** 题干 */
    private String tg;
    /** 题目类型 */
    private String lx;
    /** 题目选项 */
    private String xx;
    /** 题目答案 */
    private String da;
    /** 题目解析 */
    private String jx;
    /** 单选题选项几率 */
    private HashMap<Object,String> dxArr;
    /** 多选题选项几率 */
    private HashMap<Object,String> fxArr;
    /** 判断题选项几率 */
    private HashMap<Object,String> pdArr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getXx() {
        return xx;
    }

    public void setXx(String xx) {
        this.xx = xx;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getJx() {
        return jx;
    }

    public void setJx(String jx) {
        this.jx = jx;
    }

    public HashMap<Object, String> getDxArr() {
        return dxArr;
    }

    public void setDxArr(HashMap<Object, String> dxArr) {
        this.dxArr = dxArr;
    }

    public HashMap<Object, String> getFxArr() {
        return fxArr;
    }

    public void setFxArr(HashMap<Object, String> fxArr) {
        this.fxArr = fxArr;
    }

    public HashMap<Object, String> getPdArr() {
        return pdArr;
    }

    public void setPdArr(HashMap<Object, String> pdArr) {
        this.pdArr = pdArr;
    }
}
