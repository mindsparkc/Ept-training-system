package com.sinosoft.admin.question.domain;

import com.sinosoft.core.domain.Question;

public class QuestionImportData extends Question {
    private Long recordId;

    private Long sn; // 序号
    private Long rowNum; // Excel 行号

    public Long getSn() {
        return sn;
    }

    public void setSn(Long sn) {
        this.sn = sn;
    }

    public Long getRowNum() {
        return rowNum;
    }

    public void setRowNum(Long row) {
        this.rowNum = row;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }
}
