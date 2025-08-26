package com.sinosoft.user.exam.domain;

/**
 * 开始考试
 *
 * @author wanglijie
 */
public class UserExamStart {
    private static final long serialVersionUID = 1L;

    private Long examId;
    private String password;
    /** 考试记录 ID，若继续之前的考试，需要填写该字段 */
    private Long recordId;

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
}
