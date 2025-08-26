package com.sinosoft.etp.cp.opinion.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import com.sinosoft.etp.cp.opinion.domain.TOpinionWithCp;
import com.sinosoft.etp.cp.opinion.mapper.TOpinionWithCpMapper;
import com.sinosoft.etp.cp.opinion.service.ITOpinionWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 意见反馈Service业务层处理
 *
 *
 * @date 2022-02-17
 */
@Service
public class TOpinionWithCpServiceImpl implements ITOpinionWithCpService
{
    @Autowired
    private TOpinionWithCpMapper tOpinionMapper;

    /**
     * 查询意见反馈
     *
     * @param id 意见反馈ID
     * @return 意见反馈
     */
    @Override
    public TOpinionWithCp selectTOpinionById(Long id)
    {
        return tOpinionMapper.selectTOpinionById(id);
    }

    /**
     * 查询意见反馈列表
     *
     * @param tOpinion 意见反馈
     * @return 意见反馈
     */
    @EtpDataScope(alias = "o")
    @Override
    public List<TOpinionWithCp> selectTOpinionList(TOpinionWithCp tOpinion)
    {
        tOpinion.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
        return tOpinionMapper.selectTOpinionList(tOpinion);
    }

    /**
     * 新增意见反馈
     *
     * @param tOpinion 意见反馈
     * @return 结果
     */
    @Override
    public int insertTOpinion(TOpinionWithCp tOpinion)
    {
        EtpDataScopeUtils.setDataScope(tOpinion);// 20250520 lyd etp数据权限相关字段
        tOpinion.setCreateTime(DateUtils.getNowDate());
        return tOpinionMapper.insertTOpinion(tOpinion);
    }

    /**
     * 修改意见反馈
     *
     * @param tOpinion 意见反馈
     * @return 结果
     */
    @Override
    public int updateTOpinion(TOpinionWithCp tOpinion)
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
