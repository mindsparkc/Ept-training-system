package com.sinosoft.etp.up.courseware.mapper;

import com.sinosoft.etp.up.courseware.domain.TCoursewareCompany;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 课件企业关联Mapper接口
 *
 *
 * @date 2021-11-12
 */
@Mapper
public interface TCoursewareCompanyMapper
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
