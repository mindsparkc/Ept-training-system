package com.sinosoft.etp.cp.subjectResource.mapper;

import com.sinosoft.etp.cp.subjectResource.domain.TSubjectResourceWithCp;

import java.util.List;

/**
 * 题库资源管理Mapper接口
 * 
 * @author wyx
 * @date 2022-02-24
 */
public interface TSubjectResourceWithCpMapper
{
    /**
     * 查询题库资源管理
     * 
     * @param id 题库资源管理ID
     * @return 题库资源管理
     */
    public TSubjectResourceWithCp selectTSubjectResourceById(Long id);

    /**
     * 查询题库资源管理列表
     * 
     * @param tSubjectResource 题库资源管理
     * @return 题库资源管理集合
     */
    public List<TSubjectResourceWithCp> selectTSubjectResourceList(TSubjectResourceWithCp tSubjectResource);

    /**
     * 新增题库资源管理
     * 
     * @param tSubjectResource 题库资源管理
     * @return 结果
     */
    public int insertTSubjectResource(TSubjectResourceWithCp tSubjectResource);

    /**
     * 修改题库资源管理
     * 
     * @param tSubjectResource 题库资源管理
     * @return 结果
     */
    public int updateTSubjectResource(TSubjectResourceWithCp tSubjectResource);

    /**
     * 删除题库资源管理
     * 
     * @param id 题库资源管理ID
     * @return 结果
     */
    public int deleteTSubjectResourceById(Long id);

    /**
     * 批量删除题库资源管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTSubjectResourceByIds(Long[] ids);
}
