package com.sinosoft.etp.up.questionnaire.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaireTag;
import com.sinosoft.etp.up.questionnaire.mapper.TQuestionnaireTagMapper;
import com.sinosoft.etp.up.questionnaire.service.ITQuestionnaireTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问卷调查和标签关联表Service业务层处理
 *
 *
 * @date 2022-02-16
 */
@Service
public class TQuestionnaireTagServiceImpl implements ITQuestionnaireTagService
{
    @Autowired
    private TQuestionnaireTagMapper tQuestionnaireTagMapper;

    /**
     * 查询问卷调查和标签关联表
     *
     * @param id 问卷调查和标签关联表ID
     * @return 问卷调查和标签关联表
     */
    @Override
    public TQuestionnaireTag selectTQuestionnaireTagById(Long id)
    {
        return tQuestionnaireTagMapper.selectTQuestionnaireTagById(id);
    }

    /**
     * 查询问卷调查和标签关联表列表
     *
     * @param tQuestionnaireTag 问卷调查和标签关联表
     * @return 问卷调查和标签关联表
     */
    @Override
    public List<TQuestionnaireTag> selectTQuestionnaireTagList(TQuestionnaireTag tQuestionnaireTag)
    {
        return tQuestionnaireTagMapper.selectTQuestionnaireTagList(tQuestionnaireTag);
    }

    /**
     * 新增问卷调查和标签关联表
     *
     * @param tQuestionnaireTag 问卷调查和标签关联表
     * @return 结果
     */
    @Override
    public int insertTQuestionnaireTag(TQuestionnaireTag tQuestionnaireTag)
    {
        tQuestionnaireTag.setCreateTime(DateUtils.getNowDate());
        return tQuestionnaireTagMapper.insertTQuestionnaireTag(tQuestionnaireTag);
    }

    /**
     * 修改问卷调查和标签关联表
     *
     * @param tQuestionnaireTag 问卷调查和标签关联表
     * @return 结果
     */
    @Override
    public int updateTQuestionnaireTag(TQuestionnaireTag tQuestionnaireTag)
    {
        tQuestionnaireTag.setUpdateTime(DateUtils.getNowDate());
        return tQuestionnaireTagMapper.updateTQuestionnaireTag(tQuestionnaireTag);
    }

    /**
     * 批量删除问卷调查和标签关联表
     *
     * @param ids 需要删除的问卷调查和标签关联表ID
     * @return 结果
     */
    @Override
    public int deleteTQuestionnaireTagByIds(Long[] ids)
    {
        return tQuestionnaireTagMapper.deleteTQuestionnaireTagByIds(ids);
    }

    /**
     * 删除问卷调查和标签关联表信息
     *
     * @param id 问卷调查和标签关联表ID
     * @return 结果
     */
    @Override
    public int deleteTQuestionnaireTagById(Long id)
    {
        return tQuestionnaireTagMapper.deleteTQuestionnaireTagById(id);
    }
}
