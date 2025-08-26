package com.sinosoft.admin.question.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 导入题目记录的 Bean
 */
public class QuestionImportRecord {
    // 导入记录 ID
    private Long recordId;
    // 用户 ID
    private Long userId;
    // 导入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date importDate;
    // 总条数
    private Integer total;
    // 失败条数
    private Integer fail;
    // 成功条数
    private Integer success;
    // 状态 1-转换中；2-解析完毕；8-放弃导入；9-导入完毕
    private String status;
    // 导入文本文件
    private String fileName;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
