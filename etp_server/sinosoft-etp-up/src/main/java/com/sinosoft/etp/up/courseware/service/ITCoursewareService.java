package com.sinosoft.etp.up.courseware.service;

import com.sinosoft.etp.up.courseware.domain.TCourseware;

import java.util.List;

/**
 * 课件管理Service接口
 *
 *
 * @date 2021-11-08
 */
public interface ITCoursewareService
{
    /**
     * 查询课件管理
     *
     * @param id 课件管理ID
     * @return 课件管理
     */
    public TCourseware selectTCoursewareById(Long id);

    /**
     * 查询课件管理列表
     *
     * @param tCourseware 课件管理
     * @return 课件管理集合
     */
    public List<TCourseware> selectTCoursewareList(TCourseware tCourseware);


}
