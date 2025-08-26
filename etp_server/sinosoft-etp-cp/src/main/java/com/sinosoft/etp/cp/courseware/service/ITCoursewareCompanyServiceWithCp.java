package com.sinosoft.etp.cp.courseware.service;

import com.sinosoft.etp.cp.courseware.domain.TCoursewareCompanyWithCp;

import java.util.List;

/**
 * 课件企业关联Service接口
 * 
 * 
 * @date 2021-11-12
 */
public interface ITCoursewareCompanyServiceWithCp
{
    /**
     * 查询课件企业关联
     * 
     * @param id 课件企业关联ID
     * @return 课件企业关联
     */
    public TCoursewareCompanyWithCp selectTCoursewareCompanyById(Long id);

    /**
     * 查询课件企业关联列表
     * 
     * @param tCoursewareCompany 课件企业关联
     * @return 课件企业关联集合
     */
    public List<TCoursewareCompanyWithCp> selectTCoursewareCompanyList(TCoursewareCompanyWithCp tCoursewareCompany);

    /**
     * 新增课件企业关联
     * 
     * @param tCoursewareCompany 课件企业关联
     * @return 结果
     */
    public int insertTCoursewareCompany(TCoursewareCompanyWithCp tCoursewareCompany);

    /**
     * 修改课件企业关联
     * 
     * @param tCoursewareCompany 课件企业关联
     * @return 结果
     */
    public int updateTCoursewareCompany(TCoursewareCompanyWithCp tCoursewareCompany);

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
