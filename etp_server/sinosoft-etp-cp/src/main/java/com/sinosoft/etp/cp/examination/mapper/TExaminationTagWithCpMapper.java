package com.sinosoft.etp.cp.examination.mapper;

import com.sinosoft.etp.cp.examination.domain.TExaminationTagWithCp;

import java.util.List;

/**
 * 考试标签关联Mapper接口
 *
 * 
 * @date 2021-12-28
 */
public interface TExaminationTagWithCpMapper
{
    /**
     * 查询考试标签关联
     *
     * @param id 考试标签关联ID
     * @return 考试标签关联
     */
    public TExaminationTagWithCp selectTExaminationTagById(Long id);

    /**
     * 查询考试标签关联列表
     *
     * @param tExaminationTag 考试标签关联
     * @return 考试标签关联集合
     */
    public List<TExaminationTagWithCp> selectTExaminationTagList(TExaminationTagWithCp tExaminationTag);

    /**
     * 新增考试标签关联
     *
     * @param tExaminationTag 考试标签关联
     * @return 结果
     */
    public int insertTExaminationTag(TExaminationTagWithCp tExaminationTag);

    /**
     * 修改考试标签关联
     *
     * @param tExaminationTag 考试标签关联
     * @return 结果
     */
    public int updateTExaminationTag(TExaminationTagWithCp tExaminationTag);

    /**
     * 删除考试标签关联
     *
     * @param id 考试标签关联ID
     * @return 结果
     */
    public int deleteTExaminationTagById(Long id);

    /**
     * 批量删除考试标签关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTExaminationTagByIds(Long[] ids);
}
