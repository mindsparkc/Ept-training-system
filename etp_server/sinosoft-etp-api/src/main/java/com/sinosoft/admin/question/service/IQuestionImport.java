package com.sinosoft.admin.question.service;

import com.sinosoft.admin.question.domain.QuestionImport;
import com.sinosoft.admin.question.domain.QuestionImportRecord;
import com.sinosoft.core.domain.Question;

import java.util.List;

public interface IQuestionImport {
    public Long importQuestion(List<QuestionImport> questions, String fileName);

    // 查询导入列表
    public List<QuestionImportRecord> selectImportRecord(Long userId, String status, String fileName);

    // 根据 ID 查询导入的题目列表
    public List<Question> selectImportData(Long recordId);

    // 提交某 ID 批次的导入，导入提交后，即变成已提交状态，不能再次提交
    public int submitImportRecord(Long recordId);

    // 放弃某次导入
    public void giveupImportRecord(Long recordId);

    // 修改某 ID 导入，某题目的信息
    public void editImportData(Question question);

    // 将导入的某条题目标记为删除，这样这条题目在导入时就不会导入到题库中了
    public void deleteImportData(Long[] quIds);
}
