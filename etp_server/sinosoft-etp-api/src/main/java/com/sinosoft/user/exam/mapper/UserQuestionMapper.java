package com.sinosoft.user.exam.mapper;

import com.sinosoft.core.domain.PaperUnit;
import com.sinosoft.core.domain.Question;
import com.sinosoft.user.exam.domain.UserPaperUnitItemQuestion;

import java.util.List;

public interface UserQuestionMapper {

    /**
     * 执行 SQL，获取题目信息
     *
     * @param sql
     * @return
     */
    public List<UserPaperUnitItemQuestion> selectPaperUnitItemQuestionResult(String sql);
    /**
     * 获取单条题目信息
     *
     * @param quId
     * @return
     */
    public Question selectQuestionById(Long quId);

    /**
     * 批量查询题目信息列表
     *
     * @param quIds 需要查询的题目信息ID
     * @return 题目信息集合
     */
    public List<Question> selectQuestionByIds(Long[] quIds);

    /**
     * 根据规则批量获取题目列表
     *
     * @param units
     * @return
     */
    public List<Question> selectQuestionByRule(List<PaperUnit> units);
}
