package com.sinosoft.etp.up.courseware.service;

import com.sinosoft.etp.up.courseware.domain.TCoursewareStudent;

import java.util.List;


/**
 * 【请填写功能名称】Service接口
 *
 *
 * @date 2022-03-31
 */
public interface ITCoursewareStudentService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TCoursewareStudent selectTCoursewareStudentById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tCoursewareStudent 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TCoursewareStudent> selectTCoursewareStudentList(TCoursewareStudent tCoursewareStudent);

    /**
     * 新增【请填写功能名称】
     *
     * @param tCoursewareStudent 【请填写功能名称】
     * @return 结果
     */
    public int insertTCoursewareStudent(TCoursewareStudent tCoursewareStudent);

    /**
     * 修改【请填写功能名称】
     *
     * @param tCoursewareStudent 【请填写功能名称】
     * @return 结果
     */
    public int updateTCoursewareStudent(TCoursewareStudent tCoursewareStudent);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTCoursewareStudentByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTCoursewareStudentById(Long id);
}
