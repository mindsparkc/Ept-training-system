package com.sinosoft.admin.exam.mapper;

import com.sinosoft.admin.exam.domain.ExamQuery;
import com.sinosoft.core.domain.Exam;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 考试信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-09-04
 */
public interface ExamMapper
{
    /**
     * 查询考试信息
     *
     * @param examId 考试信息ID
     * @return 考试信息
     */
    public Exam selectExamById(Long examId);

    /**
     * 查询考试信息列表
     *
     * @param examQuery 考试信息
     * @return 考试信息集合
     */
    public List<Exam> selectExamList(ExamQuery examQuery);

    /**
     * 批量查询考试信息列表
     *
     * @param examIds 需要查询的考试信息ID
     * @return 考试信息集合
     */
    public List<Exam> selectExamByIds(Long[] examIds);

    /**
     * 新增考试信息
     *
     * @param exam 考试信息
     * @return 结果
     */
    public int insertExam(Exam exam);

    /**
     * 修改考试信息
     *
     * @param exam 考试信息
     * @return 结果
     */
    public int updateExam(Exam exam);
    public int updateExamStatus(Exam exam);

    /**
     * 删除考试信息
     *
     * @param examId 考试信息ID
     * @return 结果
     */
    public int deleteExamById(Long examId);

    /**
     * 批量删除考试信息
     *
     * @param examIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteExamByIds(Long[] examIds);

    /**
     * 逻辑删除试卷
     * @param examId
     * @return
     */
    public int logicalDeleteExamById(Long examId);

    /**
     * 逻辑删除试卷
     * @param examIds
     * @return
     */
    public int logicalDeleteExamByIds(Long[] examIds);

    /**
     * 校验考试名称是否存在
     * @param name 考试名称
     * @param examId 考试id
     * @return
     */
    public int checkName(@Param("name") String name, @Param("examId") Long examId);

    /**
     * 查询试卷是否被引用
     * @param paperIds
     * @return
     */
    public List<Exam> selectExamByPaperIds(Long[] paperIds);

    public Exam selectExamByPaperId(Long paperId);

}
