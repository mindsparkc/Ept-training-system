package com.sinosoft.etp.cp.examination.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.core.domain.BaseEntity;

import java.util.Date;


/**
 * @author 86151
 * @type create
 * @auther cfl
 * 2021-12-31
 * 考试与学员的关联实体类
 */
public class TExaminationInfoWithCp extends BaseEntity {

    /** 主键 */
    private Long id;
    /** 学习进度*/
    private String jd;
    /** 考试ID*/
    private Long ksid;
    /** 学员ID*/
    private Long xyid;
    /** 正确率*/
    private double rate;
    /** 用户名称 */
    @Excel(name="考生账号")
    private String userName;
    /** 用户昵称 */
    @Excel(name="考生昵称")
    private String nickName;
    /** 考试开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name="考试开始时间",width = 30,dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /** 考试结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name="交卷时间",width = 30,dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handTime;
    /** 考试时长 */
    @Excel(name="考试时长（分钟）")
    private String kssc;
    /** 是否强制交卷 */
    @Excel(name="是否强制交卷")
    private String handType;
    /** 判卷状态 */
    @Excel(name="是否判卷")
    private String pjzt;
    /** 判卷时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name="判卷时间",width = 30,dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date pjTime;
    /** 成绩*/
    @Excel(name="考生成绩")
    private double score;
    /** 用户id */
    private Long userId;
    /** 邮箱 */
    private String email;
    /** 手机号 */
    private String phonenumber;
    /** 性别 */
    private String sex;
    /** 判卷老师 */
    private String pjTeacher;
    /** 学员答案快照 */
    private String da;

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getPjTeacher() {
        return pjTeacher;
    }

    public void setPjTeacher(String pjTeacher) {
        this.pjTeacher = pjTeacher;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getKssc() {
        return kssc;
    }

    public void setKssc(String kssc) {
        this.kssc = kssc;
    }

    public String getHandType() {
        return handType;
    }

    public void setHandType(String handType) {
        this.handType = handType;
    }

    public Date getPjTime() {
        return pjTime;
    }

    public void setPjTime(Date pjTime) {
        this.pjTime = pjTime;
    }

    public Long getUserId() {
        return userId;
    }

    public TExaminationInfoWithCp setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public TExaminationInfoWithCp setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public TExaminationInfoWithCp setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public TExaminationInfoWithCp setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public TExaminationInfoWithCp setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public TExaminationInfoWithCp setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public Date getHandTime(){
        return handTime;
    }

    public TExaminationInfoWithCp setHandTime(Date handTime){
        this.handTime = handTime;
        return this;
    }

    public String getPjzt(){
        return pjzt;
    }

    public TExaminationInfoWithCp setPjzt(String pjzt){
        this.pjzt = pjzt;
        return this;
    }

    public double getScore() {
        return score;
    }

    public TExaminationInfoWithCp setScore(double score) {
        this.score = score;
        return this;
    }

    public double getRate() {
        return rate;
    }

    public TExaminationInfoWithCp setRate(double rate) {
        this.rate = rate;
        return this;
    }

    public Long getId() {
        return id;
    }

    public TExaminationInfoWithCp setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getKsid() {
        return ksid;
    }

    public TExaminationInfoWithCp setKsid(Long ksid) {
        this.ksid = ksid;
        return this;
    }

    public Long getXyid() {
        return xyid;
    }

    public TExaminationInfoWithCp setXyid(Long xyid) {
        this.xyid = xyid;
        return this;
    }

    public String getJd() {
        return jd;
    }

    public TExaminationInfoWithCp setJd(String jd) {
        this.jd = jd;
        return this;
    }

}
