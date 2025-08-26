package com.sinosoft.etp.cp.courseware.service;

import com.sinosoft.etp.cp.courseware.domain.TCoursewareWithCp;

import java.util.List;

/**
 * 课件管理Service接口
 *
 *
 * @date 2021-11-08
 */
public interface ITCoursewareServiceWithCp
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
     * 批量删除课件管理
     *
     * @param ids 需要删除的课件管理ID
     * @return 结果
     */
    public int deleteTCoursewareByIds(Long[] ids);

    /**
     * 删除课件管理信息
     *
     * @param id 课件管理ID
     * @return 结果
     */
    public int deleteTCoursewareById(Long id);


    public List<TCoursewareWithCp> getSelectedKjByKc(Long kcid);

    public boolean checkExistKJ(TCoursewareWithCp tCourseware);
}
