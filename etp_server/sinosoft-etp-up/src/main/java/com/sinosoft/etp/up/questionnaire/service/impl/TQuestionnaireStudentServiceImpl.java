package com.sinosoft.etp.up.questionnaire.service.impl;

import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaireStudent;
import com.sinosoft.etp.up.questionnaire.mapper.TQuestionnaireStudentMapper;
import com.sinosoft.etp.up.questionnaire.service.ITQuestionnaireStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问卷学生关联Service业务层处理
 *
 *
 * @date 2022-02-16
 */
@Service
public class TQuestionnaireStudentServiceImpl implements ITQuestionnaireStudentService
{
    @Autowired
    private TQuestionnaireStudentMapper tQuestionnaireStudentMapper;

    /**
     * 查询问卷学生关联
     *
     * @param id 问卷学生关联ID
     * @return 问卷学生关联
     */
    @Override
    public TQuestionnaireStudent selectTQuestionnaireStudentById(Long id)
    {
        return tQuestionnaireStudentMapper.selectTQuestionnaireStudentById(id);
    }

    /**
     * 查询问卷学生关联列表
     *
     * @param tQuestionnaireStudent 问卷学生关联
     * @return 问卷学生关联
     */
    @Override
    public List<TQuestionnaireStudent> selectTQuestionnaireStudentList(TQuestionnaireStudent tQuestionnaireStudent)
    {
        return tQuestionnaireStudentMapper.selectTQuestionnaireStudentList(tQuestionnaireStudent);
    }

    /**
     * 新增问卷学生关联
     *
     * @param tQuestionnaireStudent 问卷学生关联
     * @return 结果
     */
    @Override
    public int insertTQuestionnaireStudent(TQuestionnaireStudent tQuestionnaireStudent)
    {
        return tQuestionnaireStudentMapper.insertTQuestionnaireStudent(tQuestionnaireStudent);
    }

    /**
     * 修改问卷学生关联
     *
     * @param tQuestionnaireStudent 问卷学生关联
     * @return 结果
     */
    @Override
    public int updateTQuestionnaireStudent(TQuestionnaireStudent tQuestionnaireStudent)
    {
        return tQuestionnaireStudentMapper.updateTQuestionnaireStudent(tQuestionnaireStudent);
    }

    /**
     * 批量删除问卷学生关联
     *
     * @param ids 需要删除的问卷学生关联ID
     * @return 结果
     */
    @Override
    public int deleteTQuestionnaireStudentByIds(Long[] ids)
    {
        return tQuestionnaireStudentMapper.deleteTQuestionnaireStudentByIds(ids);
    }

    /**
     * 删除问卷学生关联信息
     *
     * @param id 问卷学生关联ID
     * @return 结果
     */
    @Override
    public int deleteTQuestionnaireStudentById(Long id)
    {
        return tQuestionnaireStudentMapper.deleteTQuestionnaireStudentById(id);
    }
}
