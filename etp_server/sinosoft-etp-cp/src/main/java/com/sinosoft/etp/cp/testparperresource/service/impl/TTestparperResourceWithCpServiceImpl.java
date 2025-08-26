package com.sinosoft.etp.cp.testparperresource.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.cp.testparperresource.domain.TTestparperResourceWithCp;
import com.sinosoft.etp.cp.testparperresource.mapper.TTestparperResourceWithCpMapper;
import com.sinosoft.etp.cp.testparperresource.service.ITTestparperResourceWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试卷资源管理Service业务层处理
 *
 * @author wyx
 * @date 2022-02-24
 */
@Service
public class TTestparperResourceWithCpServiceImpl implements ITTestparperResourceWithCpService
{
    @Autowired
    private TTestparperResourceWithCpMapper tTestparperResourceMapper;

    /**
     * 查询试卷资源管理
     *
     * @param id 试卷资源管理ID
     * @return 试卷资源管理
     */
    @Override
    public TTestparperResourceWithCp selectTTestparperResourceById(Long id)
    {
        return tTestparperResourceMapper.selectTTestparperResourceById(id);
    }

    /**
     * 查询试卷资源管理列表
     *
     * @param tTestparperResource 试卷资源管理
     * @return 试卷资源管理
     */
    @Override
    public List<TTestparperResourceWithCp> selectTTestparperResourceList(TTestparperResourceWithCp tTestparperResource)
    {
        return tTestparperResourceMapper.selectTTestparperResourceList(tTestparperResource);
    }

    /**
     * 新增试卷资源管理
     *
     * @param tTestparperResource 试卷资源管理
     * @return 结果
     */
    @Override
    public int insertTTestparperResource(TTestparperResourceWithCp tTestparperResource)
    {
        tTestparperResource.setCreateTime(DateUtils.getNowDate());
        return tTestparperResourceMapper.insertTTestparperResource(tTestparperResource);
    }

    /**
     * 修改试卷资源管理
     *
     * @param tTestparperResource 试卷资源管理
     * @return 结果
     */
    @Override
    public int updateTTestparperResource(TTestparperResourceWithCp tTestparperResource)
    {
        tTestparperResource.setUpdateTime(DateUtils.getNowDate());
        return tTestparperResourceMapper.updateTTestparperResource(tTestparperResource);
    }

    /**
     * 批量删除试卷资源管理
     *
     * @param ids 需要删除的试卷资源管理ID
     * @return 结果
     */
    @Override
    public int deleteTTestparperResourceByIds(Long[] ids)
    {
        return tTestparperResourceMapper.deleteTTestparperResourceByIds(ids);
    }

    /**
     * 删除试卷资源管理信息
     *
     * @param id 试卷资源管理ID
     * @return 结果
     */
    @Override
    public int deleteTTestparperResourceById(Long id)
    {
        return tTestparperResourceMapper.deleteTTestparperResourceById(id);
    }
}
