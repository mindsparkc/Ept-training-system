package com.sinosoft.etp.up.courseware.mapper;

import com.sinosoft.etp.up.courseware.domain.TCoursewareStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 【请填写功能名称】Mapper接口
 *
 *
 * @date 2022-03-31
 */
public interface TCoursewareStudentMapper
{
    /**
     * 查询
     *
     * @param kjid
     * @return
     */
    public TCoursewareStudent selectTCoursewareStudentById(@Param("kjid") Long kjid, @Param("xyid") Long xyid);

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
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTCoursewareStudentById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCoursewareStudentByIds(Long[] ids);

}
