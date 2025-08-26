package com.sinosoft.etp.up.examination.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.annotation.Excel;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.etp.up.testpaper.domain.TTestpaperStudent;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 考试对象 t_examination
 *
 *
 * @date 2021-12-24
 */
public class TExamination extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 考试名称
     */
    @Excel(name = "考试名称")
    private String mc;

    /**
     * 考试描述
     */
    @Excel(name = "考试描述")
    private String ms;

    /**
     * 封面
     */
    @Excel(name = "封面")
    @PreProcessField
    private String fm;

    /**
     * 发布状态：0-暂存，1-发布
     */
    @Excel(name = "发布状态：0-暂存，1-发布")
    private String fb;

    /**
     * 考试链接
     */
    @Excel(name = "考试链接")
    private String kslj;

    /**
     * 开放程度：完全公开-0
     * ，限定用户-1
     */
    @Excel(name = "开放程度：完全公开-0，限定用户-1")
    private String kfcd;

    /**
     * 试卷id
     */
    @Excel(name = "试卷id")
    private Long sjid;

    /**
     * 考试开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "考试开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date kskssj;

    /**
     * 考试结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "考试结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date ksjssj;

    /**
     * 答卷时长
     */
    @Excel(name = "答卷时长")
    private Long djsc;

    /**
     * 答题次数
     */
    @Excel(name = "答题次数")
    private String dtcs;

    /**
     * 分数等级：{key:"不及格",value:[{key:"min",value:0},{key:"max",value:60}]},
     * {key:"及格",value:[{key:"min",value:60},{key:"max",value:80}]},
     * {key:"良好",value:[{key:"min",value:80},{key:"max",value:90}]},
     * {key:"优秀",value:[{key:"min",value:90},{key:"max",value:100}]},
     * ]
     */
    private String fsdj;

    /**
     * 总分数
     */
    @Excel(name = "总分数")
    private Long zfs;

    /**
     * 删除
     */
    private String delFlag;

    private String tags;

    private Long xyid;
    private Long qyid;
    /**
     * '0' 自选题组卷'
     * '1' then '随机选题组卷'
     * 随机生成考卷'
     */
    private String zjcl;
    /**
     * 推送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "考试推送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date pushTime;

    /**
     * 考试状态
     */
    private String ksjd;

    private List<TTestpaperStudent> tTestpaperStudent;
    /**
     * 当前用户答题次数
     */
    private String yhdtcs;

    private Date showScoreTime;

    private String scoreset;
    /** 考试是否交卷 */
    private String isSubmit;
    /** 判卷状态 */
    private String pjzt;
    /** 判卷老师 */
    private String pjTeacher;
    /** 判卷时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pjTime;
    /** 切出页面次数 */
    private String turnThreshold;
    /** 页面无操作时间（分钟） */
    private String noactionTimeout;
    /** 试题个数*/
    private int stgs;
    /** 试卷名称 */
    private String sjmc;

    private String tx;

    private String userName;
    private Double totalScore;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public String getSjmc() {
        return sjmc;
    }

    public void setSjmc(String sjmc) {
        this.sjmc = sjmc;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }

    public String getTurnThreshold() {
        return turnThreshold;
    }

    public void setTurnThreshold(String turnThreshold) {
        this.turnThreshold = turnThreshold;
    }

    public String getNoactionTimeout() {
        return noactionTimeout;
    }

    public void setNoactionTimeout(String noactionTimeout) {
        this.noactionTimeout = noactionTimeout;
    }

    public String getPjzt() {
        return pjzt;
    }

    public void setPjzt(String pjzt) {
        this.pjzt = pjzt;
    }

    public String getPjTeacher() {
        return pjTeacher;
    }

    public void setPjTeacher(String pjTeacher) {
        this.pjTeacher = pjTeacher;
    }

    public Date getPjTime() {
        return pjTime;
    }

    public void setPjTime(Date pjTime) {
        this.pjTime = pjTime;
    }

    public String getIsSubmit(){
        return isSubmit;
    }

    public void setIsSubmit(String isSubmit){
        this.isSubmit = isSubmit;
    }

    public String getScoreset(){
        return scoreset;
    }

    public void setScoreset(String scoreset){
        this.scoreset = scoreset;
    }

    public Date getShowScoreTime() {
        return showScoreTime;
    }

    public void setShowScoreTime(Date showScoreTime) {
        this.showScoreTime = showScoreTime;
    }

    public String getYhdtcs() {
        return yhdtcs;
    }

    public void setYhdtcs(String yhdtcs) {
        this.yhdtcs = yhdtcs;
    }

    public List<TTestpaperStudent> gettTestpaperStudent() {
        return tTestpaperStudent;
    }

    public void settTestpaperStudent(List<TTestpaperStudent> tTestpaperStudent) {
        this.tTestpaperStudent = tTestpaperStudent;
    }

    public String getKsjd() {
        return ksjd;
    }

    public void setKsjd(String ksjd) {
        this.ksjd = ksjd;
    }

    public String getZjcl() {
        return zjcl;
    }

    public void setZjcl(String zjcl) {
        this.zjcl = zjcl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public String getFm() {
        return fm;
    }

    public void setFm(String fm) {
        this.fm = fm;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getKslj() {
        return kslj;
    }

    public void setKslj(String kslj) {
        this.kslj = kslj;
    }

    public String getKfcd() {
        return kfcd;
    }

    public void setKfcd(String kfcd) {
        this.kfcd = kfcd;
    }

    public Long getSjid() {
        return sjid;
    }

    public void setSjid(Long sjid) {
        this.sjid = sjid;
    }

    public Date getKskssj() {
        return kskssj;
    }

    public void setKskssj(Date kskssj) {
        this.kskssj = kskssj;
    }

    public Date getKsjssj() {
        return ksjssj;
    }

    public void setKsjssj(Date ksjssj) {
        this.ksjssj = ksjssj;
    }

    public Long getDjsc() {
        return djsc;
    }

    public void setDjsc(Long djsc) {
        this.djsc = djsc;
    }

    public String getDtcs() {
        return dtcs;
    }

    public void setDtcs(String dtcs) {
        this.dtcs = dtcs;
    }

    public String getFsdj() {
        return fsdj;
    }

    public void setFsdj(String fsdj) {
        this.fsdj = fsdj;
    }

    public Long getZfs() {
        return zfs;
    }

    public void setZfs(Long zfs) {
        this.zfs = zfs;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Long getXyid() {
        return xyid;
    }

    public void setXyid(Long xyid) {
        this.xyid = xyid;
    }

    public Long getQyid() {
        return qyid;
    }

    public void setQyid(Long qyid) {
        this.qyid = qyid;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public int getStgs() {
        return stgs;
    }

    public void setStgs(int stgs) {
        this.stgs = stgs;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("mc", getMc())
                .append("ms", getMs())
                .append("fm", getFm())
                .append("fb", getFb())
                .append("kslj", getKslj())
                .append("kfcd", getKfcd())
                .append("sjid", getSjid())
                .append("kskssj", getKskssj())
                .append("ksjssj", getKsjssj())
                .append("djsc", getDjsc())
                .append("dtcs", getDtcs())
                .append("fsdj", getFsdj())
                .append("zfs", getZfs())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
