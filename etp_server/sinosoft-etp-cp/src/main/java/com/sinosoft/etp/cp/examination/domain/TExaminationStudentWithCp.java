package com.sinosoft.etp.cp.examination.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.core.domain.entity.SysUser;

import java.util.Date;


/**
 * @type create
 * @auther cfl
 * 2021-12-31
 * 考试与学员的关联实体类
 */
public class TExaminationStudentWithCp extends SysUser {

    /** 主键 */
    private Long id;
    /** 考试ID*/
    private Long ksid;
    /** 学员ID*/
    private Long xyid;
    /** 学习进度*/
    private String jd;
    /** 成绩*/
    private double score;
    /** 正确率*/
    private double rate;
    /** 判卷状态 */
    private String pjzt;
    /** 考试结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handTime;

    public Date getHandTime(){
        return handTime;
    }

    public TExaminationStudentWithCp setHandTime(Date handTime){
        this.handTime = handTime;
        return this;
    }

    public String getPjzt(){
        return pjzt;
    }

    public TExaminationStudentWithCp setPjzt(String pjzt){
        this.pjzt = pjzt;
        return this;
    }

    public double getScore() {
        return score;
    }

    public TExaminationStudentWithCp setScore(double score) {
        this.score = score;
        return this;
    }

    public double getRate() {
        return rate;
    }

    public TExaminationStudentWithCp setRate(double rate) {
        this.rate = rate;
        return this;
    }

    public Long getId() {
        return id;
    }

    public TExaminationStudentWithCp setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getKsid() {
        return ksid;
    }

    public TExaminationStudentWithCp setKsid(Long ksid) {
        this.ksid = ksid;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TExaminationStudentWithCp setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }

    public String getJd() {
        return jd;
    }

    public TExaminationStudentWithCp setJd(String jd) {
        this.jd = jd;
        return this;
    }
}
