package com.sinosoft.user.exam.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sinosoft.common.core.domain.BaseEntity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

public class UserExamRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 考试记录编号 */
    private Long recordId;

    /** 考试编号 */
    private Long examId;

    /** 考试名称 */
    private String name;

    /** 试卷总分 */
    private BigDecimal totalScore;

    /** 学员得分 */
    private BigDecimal userScore;

    /** 题目数量 */
    private Integer quCount;

    /** 答对题目数量 */
    private Integer quCorrect;

    /** 正确率 */
    private String rate;

    /** 学员开始考试时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 学员实际交卷时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 逻辑上应交卷的时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logicEndTime;

    /** 交卷方式（0未交卷 1人工交卷 2强制交卷 3放弃考试） */
    private String handType;

    /** 答题耗时（秒） */
    private Long doTime;

    /** 试卷状态（0未交卷 1未判卷 4未发布 8放弃考试 9已完成） */
    private String status;

    /** 考试人员ID */
    private Long userId;

    /** 用户昵称 */
    private String nickName;

    /** 删除标记（0正常 2删除） */
    private String delFlag;

    public Date getLogicEndTime() {
        return logicEndTime;
    }

    public void setLogicEndTime(Date logicEndTime) {
        this.logicEndTime = logicEndTime;
    }

    public String getRate() {
        return rate==null?getPercent(quCorrect, quCount==0?1:quCount):rate;
    }

    public static String getPercent(int x, int y) {
        double d1 = x * 1.0;
        double d2 = y * 1.0;
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(2);
        return percentInstance.format(d1 / d2);
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public UserExamRecord(Long recordId, Long examId, BigDecimal totalScore, Integer quCount) {
//        this.recordId = recordId;
//        this.examId = examId;
//        this.totalScore = totalScore;
//        this.quCount = quCount;
//        this.startTime = DateUtils.getNowDate();
//    }
//
//    public UserExamRecord() {
//    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public BigDecimal getUserScore() {
        return userScore;
    }

    public void setUserScore(BigDecimal userScore) {
        this.userScore = userScore;
    }

    public Integer getQuCount() {
        return quCount;
    }

    public void setQuCount(Integer quCount) {
        this.quCount = quCount;
    }

    public Integer getQuCorrect() {
        return quCorrect;
    }

    public void setQuCorrect(Integer quCorrect) {
        this.quCorrect = quCorrect;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getHandType() {
        return handType;
    }

    public void setHandType(String handType) {
        this.handType = handType;
    }

    public Long getDoTime() {
        return doTime;
    }

    public void setDoTime(Long doTime) {
        this.doTime = doTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
