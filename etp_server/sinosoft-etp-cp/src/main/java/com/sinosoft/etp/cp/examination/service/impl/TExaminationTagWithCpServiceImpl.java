package com.sinosoft.etp.cp.examination.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.cp.examination.domain.TExaminationTagWithCp;
import com.sinosoft.etp.cp.examination.mapper.TExaminationTagWithCpMapper;
import com.sinosoft.etp.cp.examination.service.ITExaminationTagWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考试标签关联Service业务层处理
 *
 *
 * @date 2021-12-28
 */
@Service
public class TExaminationTagWithCpServiceImpl implements ITExaminationTagWithCpService
{
    @Autowired
    private TExaminationTagWithCpMapper tExaminationTagMapper;

    /**
     * 查询考试标签关联
     *
     * @param id 考试标签关联ID
     * @return 考试标签关联
     */
    @Override
    public TExaminationTagWithCp selectTExaminationTagById(Long id)
    {
        return tExaminationTagMapper.selectTExaminationTagById(id);
    }

    /**
     * 查询考试标签关联列表
     *
     * @param tExaminationTag 考试标签关联
     * @return 考试标签关联
     */
    @Override
    public List<TExaminationTagWithCp> selectTExaminationTagList(TExaminationTagWithCp tExaminationTag)
    {
        return tExaminationTagMapper.selectTExaminationTagList(tExaminationTag);
    }

    /**
     * 新增考试标签关联
     *
     * @param tExaminationTag 考试标签关联
     * @return 结果
     */
    @Override
    public int insertTExaminationTag(TExaminationTagWithCp tExaminationTag)
    {
        tExaminationTag.setCreateTime(DateUtils.getNowDate());
        return tExaminationTagMapper.insertTExaminationTag(tExaminationTag);
    }

    /**
     * 修改考试标签关联
     *
     * @param tExaminationTag 考试标签关联
     * @return 结果
     */
    @Override
    public int updateTExaminationTag(TExaminationTagWithCp tExaminationTag)
    {
        tExaminationTag.setUpdateTime(DateUtils.getNowDate());
        return tExaminationTagMapper.updateTExaminationTag(tExaminationTag);
    }

    /**
     * 批量删除考试标签关联
     *
     * @param ids 需要删除的考试标签关联ID
     * @return 结果
     */
    @Override
    public int deleteTExaminationTagByIds(Long[] ids)
    {
        return tExaminationTagMapper.deleteTExaminationTagByIds(ids);
    }

    /**
     * 删除考试标签关联信息
     *
     * @param id 考试标签关联ID
     * @return 结果
     */
    @Override
    public int deleteTExaminationTagById(Long id)
    {
        return tExaminationTagMapper.deleteTExaminationTagById(id);
    }
}
