package com.sinosoft.etp.cp.courseware.mapper;

import com.sinosoft.etp.cp.courseware.domain.TCoursewareCompanyWithCp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 课件企业关联Mapper接口
 *
 *
 * @date 2021-11-12
 */
@Mapper
public interface TCoursewareCompanyWithCpMapper
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
     * 删除课件企业关联
     *
     * @param id 课件企业关联ID
     * @return 结果
     */
    public int deleteTCoursewareCompanyById(Long id);

    /**
     * 批量删除课件企业关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTCoursewareCompanyByIds(Long[] ids);

    void deleteTCoursewareCompanyByKjId(Long id);

}
