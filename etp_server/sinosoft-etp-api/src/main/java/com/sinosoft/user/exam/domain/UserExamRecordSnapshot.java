package com.sinosoft.user.exam.domain;

import com.sinosoft.common.core.domain.BaseEntity;

public class UserExamRecordSnapshot extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 考试记录编号 */
    private Long recordId;

    /** 试卷快照 */
    private UserExam snapshot;

    /** 学员答题 */
    private UserExam snapshot2;

    /** 判卷 */
    private UserExam snapshot3;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }


    public UserExam getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(UserExam snapshot) {
        this.snapshot = snapshot;
    }

    public UserExam getSnapshot2() {
        return snapshot2;
    }

    public void setSnapshot2(UserExam snapshot2) {
        this.snapshot2 = snapshot2;
    }

    public UserExam getSnapshot3() {
        return snapshot3;
    }

    public void setSnapshot3(UserExam snapshot3) {
        this.snapshot3 = snapshot3;
    }
}
