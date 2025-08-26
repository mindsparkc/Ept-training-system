package com.sinosoft.etp.cp.analysis.domain;

public class PeopleAndExaminationWithCp {
    private Long ksid;

    private String ksmc;

    private Long xyid;

    private double score;

    private String nickName;

    public Long getKsid() {
        return ksid;
    }

    public void setKsid(Long ksid) {
        this.ksid = ksid;
    }

    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }

    public Long getXyid() {
        return xyid;
    }

    public void setXyid(Long xyid) {
        this.xyid = xyid;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
