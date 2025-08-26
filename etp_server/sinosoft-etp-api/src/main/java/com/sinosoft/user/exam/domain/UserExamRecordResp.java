package com.sinosoft.user.exam.domain;

/**
 * 查询考试记录，返回
 */
public class UserExamRecordResp extends UserExamRecord{
//    public UserExamRecordResp(Long recordId, Long examId, BigDecimal totalScore, Integer quCount) {
//        super(recordId, examId, totalScore, quCount);
//    }

//    public UserExamRecordResp() {
//        super(null, null, null, null);
//    }

    UserExam exam;

    public UserExam getExam() {
        return exam;
    }

    public void setExam(UserExam exam) {
        this.exam = exam;
    }
}
