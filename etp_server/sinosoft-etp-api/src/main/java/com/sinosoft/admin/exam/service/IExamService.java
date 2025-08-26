package com.sinosoft.admin.exam.service;

import com.sinosoft.admin.exam.domain.ExamQuery;
import com.sinosoft.admin.exam.domain.ExamReqDTO;
import com.sinosoft.admin.exam.domain.ExamRespDTO;
import com.sinosoft.core.domain.Exam;
import com.sinosoft.user.exam.domain.UserExam;

import java.util.List;

/**
 * 考试信息Service接口
 *
 * @author ruoyi
 * @date 2022-09-04
 */
public interface IExamService
{
    /**
     * 查询考试信息
     *
     * @param examId 考试信息ID
     * @return 考试信息
     */
    public ExamRespDTO selectExamById(Long examId);

    /**
     * 查询考试信息列表
     *
     * @param examQuery 考试信息
     * @return 考试信息集合
     */
    public List<Exam> selectExamList(ExamQuery examQuery);

    /**
     * 查询考试信息列表
     *
     * @param examIds 需要查询的考试信息ID
     * @return 考试信息集合
     */
    public List<Exam> selectExamByIds(Long[] examIds);

    /**
     * 新增考试信息
     *
     * @param examDto 考试信息
     * @return 结果
     */
    public Long insertExam(ExamReqDTO examDto);

    /**
     * 修改考试信息
     *
     * @param examDto 考试信息
     * @return 结果
     */
    public int updateExam(ExamReqDTO examDto);

    /**
     * 仅修改考试状态 status 字段
     *
     * @param examDto
     * @return
     */
    public int updateExamStatus(ExamReqDTO examDto);

    /**
     * 批量删除考试信息
     *
     * @param examIds 需要删除的考试信息ID
     * @return 结果
     */
    public int deleteExamByIds(Long[] examIds);

    /**
     * 删除考试信息信息
     *
     * @param examId 考试信息ID
     * @return 结果
     */
    public int deleteExamById(Long examId);

    /**
     * 校验考试名称是否存在
     * @param name 考试名称
     * @param examId 考试id
     * @return 结果
     */
    public int checkName(String name,Long examId);

    /**
     * 提交评卷
     * @param userExam
     */
    public void submitReview(UserExam userExam);
}
