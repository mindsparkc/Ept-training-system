package com.sinosoft.user.curriculum.domain;


/**
 * 学员课程习题集答案记录
 *
 * @author bgl
 */
public class CurriculumTrainStudent {
    /**  ID */
    private Long id;
    /**  课程ID */
    private Long cid;
    /**  习题集ID */
    private Long trainId;
    /**  学员ID */
    private Long xyid;
    /**  答案 */
    private String da;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public Long getXyid() {
        return xyid;
    }

    public void setXyid(Long xyid) {
        this.xyid = xyid;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }
}
