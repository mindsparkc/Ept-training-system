package com.sinosoft.admin.question.mapper;

import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.question.domain.FactByLevel;
import com.sinosoft.admin.question.domain.QuestionForOld;
import com.sinosoft.admin.question.domain.QuestionForOldCategory;
import com.sinosoft.admin.question.domain.QuestionReqQuery;
import com.sinosoft.core.domain.Question;

import java.util.List;

/**
 * 题目信息Mapper接口
 *
 * @author wanglijie
 * @date 2022-08-09
 */
public interface QuestionMapper
{
    /**
     * 查询题目信息
     *
     * @param quId 题目信息ID
     * @return 题目信息
     */
    public Question selectQuestionById(Long quId);

    public List<Question> selectQuestionAndChildrenById(Long quId);

    /**
     * 查询题目信息列表
     *
     * @param questionQuery 题目信息
     * @return 题目信息集合
     */
    public List<Question> selectQuestionList(QuestionReqQuery questionQuery);

    public List<FactByLevel> factQuery(QuestionReqQuery questionQuery);

    public List<Question> randomQuery(QuestionReqQuery questionQuery);
    /**
     * 查询子题目列表
     *
     * @param parentIds
     * @return
     */
    public List<Question> selectQuestionChildrenByIds(Long[] parentIds);

    /**
     * 批量查询题目信息列表
     *
     * @param quIds 需要查询的题目信息ID
     * @return 题目信息集合
     */
    public List<Question> selectQuestionByIds(Long[] quIds);

    /**
     * 新增题目信息
     *
     * @param question 题目信息
     * @return 结果
     */
    public int insertQuestion(Question question);

    /**
     * 批量插入题目
     *
     * @param question
     * @return
     */
    public int batchInsertQuestion(List<Question> question);


    /**
     * 修改题目信息
     *
     * @param question 题目信息
     * @return 结果
     */
    public int updateQuestion(Question question);

    /**
     * 逻辑删除题目
     * @param quIds
     * @return
     */
    public int logicalDeleteQuestionByIds(Long[] quIds);

    /**
     * 删除题目信息
     *
     * @param quId 题目信息ID
     * @return 结果
     */
    public int deleteQuestionById(Long quId);

    /**
     * 批量删除题目信息
     *
     * @param quIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteQuestionByIds(Long[] quIds);

    /**
     * 仅删除子题目
     *
     * @param parentId
     * @return
     */
//    public int deleteQuestionByParentId(Long parentId);

    /**
     * 仅删除子题目
     *
     * @param parentIds
     * @return
     */
//    public int deleteQuestionByParentIds(Long[] parentIds);

    /**
     * 获取所有旧题库数据
     * @return
     */
    public List<QuestionForOld> selectAll();

    /**
     * 获取所有旧题库的分类信息
     * @return
     */
    public List<QuestionForOldCategory> selectCategoryAll();

    public int insertOldCategory(Category category);
}
