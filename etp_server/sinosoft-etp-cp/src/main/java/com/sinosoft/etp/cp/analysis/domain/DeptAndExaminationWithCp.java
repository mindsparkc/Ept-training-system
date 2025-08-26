package com.sinosoft.etp.cp.analysis.domain;

import com.sinosoft.common.core.domain.entity.SysDept;

public class DeptAndExaminationWithCp extends SysDept {

    private static final long serialVersionUID = 1L;

    /** 考试id */
    private Long ksid;
    /** 应考人数 */
    private Integer peopleNum;

    /** 实考人数 */
    private Integer practiceNum;

    /** 参考率 */
    private Double ratio;

    /** 最高分 */
    private Integer topScore;

    /** 最低分 */
    private Integer lastScore;

    /** 平均分 */
    private Double averageScore;

    /** 平均考试时长 */
    private String averageTime;

    /** 分数等级 */
    private String fsdj;


    public String getFsdj() {
        return fsdj;
    }

    public void setFsdj(String fsdj) {
        this.fsdj = fsdj;
    }

    public Long getKsid() {
        return ksid;
    }

    public void setKsid(Long ksid) {
        this.ksid = ksid;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Integer getPracticeNum() {
        return practiceNum;
    }

    public void setPracticeNum(Integer practiceNum) {
        this.practiceNum = practiceNum;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Integer getTopScore() {
        return topScore;
    }

    public void setTopScore(Integer topScore) {
        this.topScore = topScore;
    }

    public Integer getLastScore() {
        return lastScore;
    }

    public void setLastScore(Integer lastScore) {
        this.lastScore = lastScore;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public String getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(String averageTime) {
        this.averageTime = averageTime;
    }
}
