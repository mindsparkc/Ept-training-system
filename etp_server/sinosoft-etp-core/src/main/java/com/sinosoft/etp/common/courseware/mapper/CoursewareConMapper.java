package com.sinosoft.etp.common.courseware.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 课件管理Mapper接口
 *
 *
 * @date 2021-11-08
 */
@Mapper
public interface CoursewareConMapper
{

    /**
     * 获取课件时长
     * @param id
     * @param xyid
     * @return
     */
    public Map<String,Object> getStudyDuration(@Param("id") String id, @Param("xyid") String xyid);

    public int updateRate(@Param("kjid") String kjid, @Param("xyid") String xyid);
}
