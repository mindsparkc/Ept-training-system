package com.sinosoft.etp.cp.questionnaire.mapper;

import com.sinosoft.etp.cp.questionnaire.domain.QuestionnaireTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 问卷调查Mapper接口
 *
 *
 * @date 2022-02-14
 */
public interface TQuestionnaireTaskMapper
{

    /**
     * 选择题汇总
     * @return
     */
    List<Map<String,Object>> getChoiceSummary();

    /**
     * 问答题汇总
     * @return
     */
    List<QuestionnaireTask> getWdSummany();

    int addQuestionnaireSum(List<QuestionnaireTask> list);


    /**
     * 问卷各个题目分析
     * @return
     */
    List<QuestionnaireTask>getChoseAnalysis(@Param("wjid")Long wjid);

    List<QuestionnaireTask>getWdAnalysis(@Param("wjid")Long wjid,@Param("tmid")String tmid);
}
