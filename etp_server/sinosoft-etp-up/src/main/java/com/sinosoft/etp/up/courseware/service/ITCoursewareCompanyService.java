package com.sinosoft.etp.up.courseware.service;

import com.sinosoft.etp.up.courseware.domain.TCoursewareCompany;

import java.util.List;

/**
 * 课件企业关联Service接口
 *
 * 
 * @date 2021-11-12
 */
public interface ITCoursewareCompanyService
{
    /**
     * 查询课件企业关联
     *
     * @param id 课件企业关联ID
     * @return 课件企业关联
     */
    public TCoursewareCompany selectTCoursewareCompanyById(Long id);

    /**
     * 查询课件企业关联列表
     *
     * @param tCoursewareCompany 课件企业关联
     * @return 课件企业关联集合
     */
    public List<TCoursewareCompany> selectTCoursewareCompanyList(TCoursewareCompany tCoursewareCompany);

    /**
     * 新增课件企业关联
     *
     * @param tCoursewareCompany 课件企业关联
     * @return 结果
     */
    public int insertTCoursewareCompany(TCoursewareCompany tCoursewareCompany);

    /**
     * 修改课件企业关联
     *
     * @param tCoursewareCompany 课件企业关联
     * @return 结果
     */
    public int updateTCoursewareCompany(TCoursewareCompany tCoursewareCompany);

    /**
     * 批量删除课件企业关联
     *
     * @param ids 需要删除的课件企业关联ID
     * @return 结果
     */
    public int deleteTCoursewareCompanyByIds(Long[] ids);

    /**
     * 删除课件企业关联信息
     *
     * @param id 课件企业关联ID
     * @return 结果
     */
    public int deleteTCoursewareCompanyById(Long id);
}
