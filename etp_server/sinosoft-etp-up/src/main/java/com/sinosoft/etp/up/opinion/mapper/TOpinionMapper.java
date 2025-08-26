package com.sinosoft.etp.up.opinion.mapper;

import com.sinosoft.etp.up.opinion.domain.TOpinion;

import java.util.List;

/**
 * 意见反馈Mapper接口
 *
 *
 * @date 2022-02-18
 */
public interface TOpinionMapper
{
    /**
     * 查询意见反馈
     *
     * @param id 意见反馈ID
     * @return 意见反馈
     */
    public TOpinion selectTOpinionById(Long id);

    /**
     * 查询意见反馈列表
     *
     * @param tOpinion 意见反馈
     * @return 意见反馈集合
     */
    public List<TOpinion> selectTOpinionList(TOpinion tOpinion);

    /**
     * 新增意见反馈
     *
     * @param tOpinion 意见反馈
     * @return 结果
     */
    public int insertTOpinion(TOpinion tOpinion);

    /**
     * 修改意见反馈
     *
     * @param tOpinion 意见反馈
     * @return 结果
     */
    public int updateTOpinion(TOpinion tOpinion);

    /**
     * 删除意见反馈
     *
     * @param id 意见反馈ID
     * @return 结果
     */
    public int deleteTOpinionById(Long id);

    /**
     * 批量删除意见反馈
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTOpinionByIds(Long[] ids);
}
