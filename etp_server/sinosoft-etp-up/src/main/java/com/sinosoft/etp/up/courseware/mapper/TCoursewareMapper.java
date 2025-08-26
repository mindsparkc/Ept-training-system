package com.sinosoft.etp.up.courseware.mapper;

import com.sinosoft.etp.up.courseware.domain.TCourseware;
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
public interface TCoursewareMapper
{
    /**
     * 查询课件管理
     *
     * @param id 课件管理ID
     * @return 课件管理
     */
    public TCourseware selectTCoursewareById(@Param("id") Long id,@Param("xyid")Long xyid);

    /**
     * 查询课件管理列表
     *
     * @param tCourseware 课件管理
     * @return 课件管理集合
     */
    public List<TCourseware> selectTCoursewareList(TCourseware tCourseware);

    /**
     * 新增课件管理
     *
     * @param tCourseware 课件管理
     * @return 结果
     */
    public int insertTCourseware(TCourseware tCourseware);

    /**
     * 修改课件管理
     *
     * @param tCourseware 课件管理
     * @return 结果
     */
    public int updateTCourseware(TCourseware tCourseware);

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
     * 获取课件时长
     * @param id
     * @param xyid
     * @return
     */
    public Map<String,Long> getStudyDuration(@Param("id") String id, @Param("xyid") String xyid);
    public int updateRate(@Param("kjid") String kjid, @Param("xyid") String xyid);
}
