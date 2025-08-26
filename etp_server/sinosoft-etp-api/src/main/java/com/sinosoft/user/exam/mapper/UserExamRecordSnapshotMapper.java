package com.sinosoft.user.exam.mapper;

import com.sinosoft.user.exam.domain.UserExamRecordSnapshot;

/**
 * 用户考试记录快照表操作
 *
 * @author wanglijie
 */
public interface UserExamRecordSnapshotMapper {
    /**
     * 查询考试记录快照
     *
     * @param recordId 考试记录快照ID
     * @return 考试记录快照
     */
    public UserExamRecordSnapshot selectExamRecordSnapshotById(Long recordId);

    /**
     * 新增考试记录快照
     *
     * @param examRecordSnapshot 考试记录快照
     * @return 结果
     */
    public int insertExamRecordSnapshot(UserExamRecordSnapshot examRecordSnapshot);

    /**
     * 修改考试记录快照
     *
     * @param examRecordSnapshot 考试记录快照
     * @return 结果
     */
    public int updateExamRecordSnapshot(UserExamRecordSnapshot examRecordSnapshot);
}
