package com.sinosoft.etp.up.questionnaire.mapper;

import com.sinosoft.etp.up.questionnaire.domain.TQuestionnaireStudent;

import java.util.List;

/**
 * 问卷学生关联Mapper接口
 *
 *
 * @date 2022-02-16
 */
public interface TQuestionnaireStudentMapper
{
    /**
     * 查询问卷学生关联
     *
     * @param id 问卷学生关联ID
     * @return 问卷学生关联
     */
    public TQuestionnaireStudent selectTQuestionnaireStudentById(Long id);

    /**
     * 查询问卷学生关联列表
     *
     * @param tQuestionnaireStudent 问卷学生关联
     * @return 问卷学生关联集合
     */
    public List<TQuestionnaireStudent> selectTQuestionnaireStudentList(TQuestionnaireStudent tQuestionnaireStudent);

    /**
     * 新增问卷学生关联
     *
     * @param tQuestionnaireStudent 问卷学生关联
     * @return 结果
     */
    public int insertTQuestionnaireStudent(TQuestionnaireStudent tQuestionnaireStudent);

    /**
     * 修改问卷学生关联
     *
     * @param tQuestionnaireStudent 问卷学生关联
     * @return 结果
     */
    public int updateTQuestionnaireStudent(TQuestionnaireStudent tQuestionnaireStudent);

    /**
     * 删除问卷学生关联
     *
     * @param id 问卷学生关联ID
     * @return 结果
     */
    public int deleteTQuestionnaireStudentById(Long id);

    /**
     * 批量删除问卷学生关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTQuestionnaireStudentByIds(Long[] ids);
}
