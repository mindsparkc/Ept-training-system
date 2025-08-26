package com.sinosoft.etp.up.opinion.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.up.opinion.domain.TOpinion;
import com.sinosoft.etp.up.opinion.mapper.TOpinionMapper;
import com.sinosoft.etp.up.opinion.service.ITOpinionService;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 意见反馈Service业务层处理
 *
 *
 * @date 2022-02-18
 */
@Service
public class TOpinionServiceImpl implements ITOpinionService
{
    @Autowired
    private TOpinionMapper tOpinionMapper;

    /**
     * 查询意见反馈
     *
     * @param id 意见反馈ID
     * @return 意见反馈
     */
    @Override
    public TOpinion selectTOpinionById(Long id)
    {
        return tOpinionMapper.selectTOpinionById(id);
    }

    /**
     * 查询意见反馈列表
     *
     * @param tOpinion 意见反馈
     * @return 意见反馈
     */
    @Override
    public List<TOpinion> selectTOpinionList(TOpinion tOpinion)
    {
        tOpinion.setXyid(SecurityUtils.getLoginUser().getUser().getUserId());
        return tOpinionMapper.selectTOpinionList(tOpinion);
    }

    /**
     * 新增意见反馈
     *
     * @param tOpinion 意见反馈
     * @return 结果
     */
    @Override
    public int insertTOpinion(TOpinion tOpinion)
    {
        EtpDataScopeUtils.setDataScope(tOpinion);// 20250520 lyd etp数据权限相关字段

        tOpinion.setCreateTime(DateUtils.getNowDate());
        tOpinion.setUpdateTime(DateUtils.getNowDate());
        return tOpinionMapper.insertTOpinion(tOpinion);
    }

    /**
     * 修改意见反馈
     *
     * @param tOpinion 意见反馈
     * @return 结果
     */
    @Override
    public int updateTOpinion(TOpinion tOpinion)
    {
        tOpinion.setUpdateTime(DateUtils.getNowDate());
        return tOpinionMapper.updateTOpinion(tOpinion);
    }

    /**
     * 批量删除意见反馈
     *
     * @param ids 需要删除的意见反馈ID
     * @return 结果
     */
    @Override
    public int deleteTOpinionByIds(Long[] ids)
    {
        return tOpinionMapper.deleteTOpinionByIds(ids);
    }

    /**
     * 删除意见反馈信息
     *
     * @param id 意见反馈ID
     * @return 结果
     */
    @Override
    public int deleteTOpinionById(Long id)
    {
        return tOpinionMapper.deleteTOpinionById(id);
    }
}
