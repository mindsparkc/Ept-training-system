package com.sinosoft.admin.question.mapper;

import com.sinosoft.admin.question.domain.QuestionImportData;
import com.sinosoft.core.domain.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionImportDataMapper {
    // 批量导入题目
    public Long insertQuestion(Question question);
    // 根据 ID 查询导入的题目列表
    public List<Question> selectImportData(@Param("recordId") Long recordId);
    // 修改某 ID 导入，某题目的信息
    public void editImportData(QuestionImportData question);
    // 将导入的某条题目标记为删除，这样这条题目在导入时就不会导入到题库中了
    public int deleteImportData(Long[] quIds);
}
