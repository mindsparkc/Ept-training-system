package com.sinosoft.admin.exam.mapper;

import com.sinosoft.core.domain.ExamOpenRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试开放规则Mapper接口
 *
 * @author ruoyi
 * @date 2022-09-04
 */
public interface ExamOpenRuleMapper
{
    /**
     * 查询考试开放规则
     *
     * @param examId 考试开放规则ID
     * @return 考试开放规则
     */
    public List<ExamOpenRule> selectExamOpenRuleById(Long examId);

    /**
     * 查询考试开放规则列表
     *
     * @param examOpenRule 考试开放规则
     * @return 考试开放规则集合
     */
    public List<ExamOpenRule> selectExamOpenRuleList(ExamOpenRule examOpenRule);

    /**
     * 批量查询考试开放规则列表
     *
     * @param examIds 需要查询的考试开放规则ID
     * @return 考试开放规则集合
     */
//    public List<ExamOpenRule> selectExamOpenRuleByIds(Long[] examIds);

    /**
     * 新增考试开放规则
     *
     * @param examOpenRule 考试开放规则
     * @return 结果
     */
    public int insertExamOpenRule(ExamOpenRule examOpenRule);

    /**
     * 批量插入考试规则
     *
     * @param examOpenRules
     * @return
     */
    public int batchInsertExamOpenRule(@Param("examOpenRules") List<ExamOpenRule> examOpenRules);
    /**
     * 修改考试开放规则
     *
     * @param examOpenRule 考试开放规则
     * @return 结果
     */
    public int updateExamOpenRule(ExamOpenRule examOpenRule);

    /**
     * 删除考试开放规则
     *
     * @param examId 考试开放规则ID
     * @return 结果
     */
    public int deleteExamOpenRuleByExamId(Long examId);

    /**
     * 批量删除考试开放规则
     *
     * @param examIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteExamOpenRuleByExamIds(Long[] examIds);
}
