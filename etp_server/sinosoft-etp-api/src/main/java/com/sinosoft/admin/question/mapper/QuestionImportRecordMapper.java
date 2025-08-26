package com.sinosoft.admin.question.mapper;

import com.sinosoft.admin.question.domain.QuestionImportRecord;
import com.sinosoft.core.domain.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionImportRecordMapper {
    // 插入导入记录
    public void insertImportRecord(QuestionImportRecord record);
    // 查询导入列表
    public List<QuestionImportRecord> selectImportRecord(Long userId, String status, String fileName);
    // 提交某 ID 批次的导入，导入提交后，即变成已提交状态，不能再次提交
    public void updateRecordStatus(@Param("recordId")Long recordId, @Param("status")String status);
}
