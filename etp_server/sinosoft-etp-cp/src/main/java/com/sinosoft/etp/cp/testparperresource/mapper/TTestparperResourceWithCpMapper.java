package com.sinosoft.etp.cp.testparperresource.mapper;

import com.sinosoft.etp.cp.testparperresource.domain.TTestparperResourceWithCp;

import java.util.List;

/**
 * 试卷资源管理Mapper接口
 * 
 * @author wyx
 * @date 2022-02-24
 */
public interface TTestparperResourceWithCpMapper
{
    /**
     * 查询试卷资源管理
     * 
     * @param id 试卷资源管理ID
     * @return 试卷资源管理
     */
    public TTestparperResourceWithCp selectTTestparperResourceById(Long id);

    /**
     * 查询试卷资源管理列表
     * 
     * @param tTestparperResource 试卷资源管理
     * @return 试卷资源管理集合
     */
    public List<TTestparperResourceWithCp> selectTTestparperResourceList(TTestparperResourceWithCp tTestparperResource);

    /**
     * 新增试卷资源管理
     * 
     * @param tTestparperResource 试卷资源管理
     * @return 结果
     */
    public int insertTTestparperResource(TTestparperResourceWithCp tTestparperResource);

    /**
     * 修改试卷资源管理
     * 
     * @param tTestparperResource 试卷资源管理
     * @return 结果
     */
    public int updateTTestparperResource(TTestparperResourceWithCp tTestparperResource);

    /**
     * 删除试卷资源管理
     * 
     * @param id 试卷资源管理ID
     * @return 结果
     */
    public int deleteTTestparperResourceById(Long id);

    /**
     * 批量删除试卷资源管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTTestparperResourceByIds(Long[] ids);
}
