package com.sinosoft.admin.question.service;

import com.sinosoft.admin.question.domain.FactByLevel;
import com.sinosoft.admin.question.domain.QuestionReqDTO;
import com.sinosoft.admin.question.domain.QuestionReqQuery;
import com.sinosoft.core.domain.Question;

import java.util.List;

/**
 * 题目信息Service接口
 *
 * @author wanglijie
 * @date 2022-08-09
 */
public interface IQuestionService
{
    /**
     * 查询题目信息
     *
     * @param quId 题目信息ID
     * @return 题目信息
     */
    public QuestionReqDTO selectQuestionById(Long quId);

    /**
     * 查询题目信息列表
     *
     * @param questionQuery 题目信息
     * @return 题目信息集合
     */
    public List<Question> selectQuestionList(QuestionReqQuery questionQuery);

    /**
     * 查询题目信息列表
     *
     * @param quIds 需要查询的题目信息ID
     * @return 题目信息集合
     */
    public List<Question> selectQuestionByIds(Long[] quIds);

    public List<FactByLevel> factQuery(QuestionReqQuery questionQuery);

    public List<Question> randomQuery(QuestionReqQuery questionQuery);

    /**
     * 新增题目信息
     *
     * @param questionDto 题目信息
     * @return 结果
     */
    public Long insertQuestion(QuestionReqDTO questionDto);

    /**
     * 修改题目信息
     *
     * @param questionDto 题目信息
     * @return 结果
     */
    public int updateQuestion(QuestionReqDTO questionDto);

    /**
     * 批量删除题目信息
     *
     * @param quIds 需要删除的题目信息ID
     * @return 结果
     */
    public int deleteQuestionByIds(Long[] quIds);

    /**
     * 删除题目信息信息
     *
     * @param quId 题目信息ID
     * @return 结果
     */
    public int deleteQuestionById(Long quId);
}
