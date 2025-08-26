package com.sinosoft.etp.cp.subjectResource.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.cp.subjectResource.domain.TSubjectResourceWithCp;
import com.sinosoft.etp.cp.subjectResource.mapper.TSubjectResourceWithCpMapper;
import com.sinosoft.etp.cp.subjectResource.service.ITSubjectResourceWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 题库资源管理Service业务层处理
 *
 * @author wyx
 * @date 2022-02-24
 */
@Service
public class TSubjectResourceWithCpServiceImpl implements ITSubjectResourceWithCpService
{
    @Autowired
    private TSubjectResourceWithCpMapper tSubjectResourceMapper;

    /**
     * 查询题库资源管理
     *
     * @param id 题库资源管理ID
     * @return 题库资源管理
     */
    @Override
    public TSubjectResourceWithCp selectTSubjectResourceById(Long id)
    {
        return tSubjectResourceMapper.selectTSubjectResourceById(id);
    }

    /**
     * 查询题库资源管理列表
     *
     * @param tSubjectResource 题库资源管理
     * @return 题库资源管理
     */
    @Override
    public List<TSubjectResourceWithCp> selectTSubjectResourceList(TSubjectResourceWithCp tSubjectResource)
    {
        return tSubjectResourceMapper.selectTSubjectResourceList(tSubjectResource);
    }

    /**
     * 新增题库资源管理
     *
     * @param tSubjectResource 题库资源管理
     * @return 结果
     */
    @Override
    public int insertTSubjectResource(TSubjectResourceWithCp tSubjectResource)
    {
        tSubjectResource.setCreateTime(DateUtils.getNowDate());
        return tSubjectResourceMapper.insertTSubjectResource(tSubjectResource);
    }

    /**
     * 修改题库资源管理
     *
     * @param tSubjectResource 题库资源管理
     * @return 结果
     */
    @Override
    public int updateTSubjectResource(TSubjectResourceWithCp tSubjectResource)
    {
        tSubjectResource.setUpdateTime(DateUtils.getNowDate());
        return tSubjectResourceMapper.updateTSubjectResource(tSubjectResource);
    }

    /**
     * 批量删除题库资源管理
     *
     * @param ids 需要删除的题库资源管理ID
     * @return 结果
     */
    @Override
    public int deleteTSubjectResourceByIds(Long[] ids)
    {
        return tSubjectResourceMapper.deleteTSubjectResourceByIds(ids);
    }

    /**
     * 删除题库资源管理信息
     *
     * @param id 题库资源管理ID
     * @return 结果
     */
    @Override
    public int deleteTSubjectResourceById(Long id)
    {
        return tSubjectResourceMapper.deleteTSubjectResourceById(id);
    }
}
