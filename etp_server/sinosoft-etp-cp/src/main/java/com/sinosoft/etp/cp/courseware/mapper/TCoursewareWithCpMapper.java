package com.sinosoft.etp.cp.courseware.mapper;

import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 课件管理Mapper接口
 *
 *
 * @date 2021-11-08
 */
@Mapper
public interface TCoursewareWithCpMapper
{
    /**
     * 查询课件管理
     *
     * @param id 课件管理ID
     * @return 课件管理
     */
    public TCoursewareWithCp selectTCoursewareById(Long id);

    /**
     * 查询课件管理列表
     *
     * @param tCourseware 课件管理
     * @return 课件管理集合
     */
    public List<TCoursewareWithCp> selectTCoursewareList(TCoursewareWithCp tCourseware);


    /**
     * 新增课件管理
     *
     * @param tCourseware 课件管理
     * @return 结果
     */
    public int insertTCourseware(TCoursewareWithCp tCourseware);

    /**
     * 修改课件管理
     *
     * @param tCourseware 课件管理
     * @return 结果
     */
    public int updateTCourseware(TCoursewareWithCp tCourseware);

    /**
     * 删除课件管理
     *
     * @param id 课件管理ID
     * @return 结果
     */
    public int deleteTCoursewareById(Long id);

    /**
     * 批量删除课件管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCoursewareByIds(Long[] ids);

    /**
     * 获取课程选中的课件
     * @param kcid
     * @return
     */
    public List<TCoursewareWithCp> getSelectedKjByKc(Long kcid);

    public TCoursewareWithCp checkExistKJ(@Param("mc") String mc);

    /**
     * 根据课件id和学员id增加学习时间
     * @return
     */
    public int updateTCoursewareByStudent(Map map);
}
