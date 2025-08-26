package com.sinosoft.etp.up.questionnaire.mapper;

import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaire;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 问卷调查Mapper接口
 *
 *
 * @date 2022-02-16
 */
public interface TQuestionnaireMapper
{
    /**
     * 查询问卷调查
     *
     * @param id 问卷调查ID
     * @return 问卷调查
     */
    public TQuestionnaire selectTQuestionnaireById(@Param("id") Long id,@Param("xyid") Long xyid);

    /**
     * 查询问卷调查列表
     *
     * @param tQuestionnaire 问卷调查
     * @return 问卷调查集合
     */
    public List<TQuestionnaire> selectTQuestionnaireList(TQuestionnaire tQuestionnaire);

    /**
     * 新增问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    public int insertTQuestionnaire(TQuestionnaire tQuestionnaire);

    /**
     * 修改问卷调查
     *
     * @param tQuestionnaire 问卷调查
     * @return 结果
     */
    public int updateTQuestionnaire(TQuestionnaire tQuestionnaire);

    /**
     * 删除问卷调查
     *
     * @param id 问卷调查ID
     * @return 结果
     */
    public int deleteTQuestionnaireById(Long id);

    /**
     * 批量删除问卷调查
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTQuestionnaireByIds(Long[] ids);

    /**
     * 获取首页问卷
     * @param xyid
     * @return
     */
    public Map<String,Object> getHomeQuestionnaire(Long xyid);
}
