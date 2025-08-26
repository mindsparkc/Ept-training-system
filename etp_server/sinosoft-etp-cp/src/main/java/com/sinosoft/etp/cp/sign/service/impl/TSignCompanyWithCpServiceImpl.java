package com.sinosoft.etp.cp.sign.service.impl;

import com.sinosoft.etp.cp.sign.domain.TSignCompanyWithCp;
import com.sinosoft.etp.cp.sign.mapper.TSignCompanyWithCpMapper;
import com.sinosoft.etp.cp.sign.service.ITSignCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 活动报名与企业关联表Service业务层处理
 *
 *
 * @date 2022-02-10
 */
@Service
public class TSignCompanyWithCpServiceImpl implements ITSignCompanyWithCpService
{
    @Autowired
    private TSignCompanyWithCpMapper tSignCompanyMapper;

    /**
     * 查询活动报名与企业关联表
     *
     * @param id 活动报名与企业关联表ID
     * @return 活动报名与企业关联表
     */
    @Override
    public TSignCompanyWithCp selectTSignCompanyById(Long id)
    {
        return tSignCompanyMapper.selectTSignCompanyById(id);
    }

    /**
     * 查询活动报名与企业关联表列表
     *
     * @param tSignCompany 活动报名与企业关联表
     * @return 活动报名与企业关联表
     */
    @Override
    public List<TSignCompanyWithCp> selectTSignCompanyList(TSignCompanyWithCp tSignCompany)
    {
        return tSignCompanyMapper.selectTSignCompanyList(tSignCompany);
    }

    /**
     * 新增活动报名与企业关联表
     *
     * @param tSignCompany 活动报名与企业关联表
     * @return 结果
     */
    @Override
    public int insertTSignCompany(TSignCompanyWithCp tSignCompany)
    {
        return tSignCompanyMapper.insertTSignCompany(tSignCompany);
    }



    /**
     * 批量删除活动报名与企业关联表
     *
     * @param ids 需要删除的活动报名与企业关联表ID
     * @return 结果
     */
    @Override
    public int deleteTSignCompanyByIds(Long[] ids)
    {
        return tSignCompanyMapper.deleteTSignCompanyByIds(ids);
    }

    /**
     * 删除活动报名与企业关联表信息
     *
     * @param id 活动报名与企业关联表ID
     * @return 结果
     */
    @Override
    public int deleteTSignCompanyById(Long id)
    {
        return tSignCompanyMapper.deleteTSignCompanyById(id);
    }
}
