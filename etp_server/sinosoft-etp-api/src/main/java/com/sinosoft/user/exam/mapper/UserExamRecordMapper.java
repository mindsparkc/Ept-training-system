package com.sinosoft.user.exam.mapper;

import com.sinosoft.user.exam.domain.UserExamRecord;
import com.sinosoft.user.exam.domain.UserExamRecordReqQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户考试记录表
 *
 * @author wanglijie
 */
public interface UserExamRecordMapper {
    /**
     * 查询考试记录
     *
     * @param recordId 考试记录ID
     * @return 考试记录
     */
    public UserExamRecord selectExamRecordById(@Param("recordId") Long recordId, @Param("userId")  Long userId);

    /**
     * 获取用户考试次数
     */
//    public Integer selectUserExamRecordCount(UserExamRecordReqQuery query);

    /**
     * 获取用户的考试列表
     */
    public List<UserExamRecord> selectUserExamRecord(UserExamRecordReqQuery query);

    /**
     * 新增考试记录
     *
     * @param examRecord 考试记录
     * @return 结果
     */
    public int insertExamRecord(UserExamRecord examRecord);

    /**
     * 修改考试记录
     *
     * @param examRecord 考试记录
     * @return 结果
     */
    public int updateExamRecord(UserExamRecord examRecord);

    /**
     *
     * @param examIds
     * @return
     */
    public int selectUserExamRecordCountByExamIds(Long[] examIds);

}
