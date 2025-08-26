package com.sinosoft.etp.cp.task.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.cp.task.domain.TTaskCompanyWithCp;
import com.sinosoft.etp.cp.task.mapper.TTaskCompanyWithCpMapper;
import com.sinosoft.etp.cp.task.service.ITTaskCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【任务与企业关联】Service业务层处理
 *
 *
 * @date 2021-12-08
 */
@Service
public class TTaskCompanyWithCpServiceImpl implements ITTaskCompanyWithCpService
{
    @Autowired
    private TTaskCompanyWithCpMapper tTaskCompanyMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TTaskCompanyWithCp selectTTaskCompanyById(Long id)
    {
        return tTaskCompanyMapper.selectTTaskCompanyById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param tTaskCompany 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TTaskCompanyWithCp> selectTTaskCompanyList(TTaskCompanyWithCp tTaskCompany)
    {
        return tTaskCompanyMapper.selectTTaskCompanyList(tTaskCompany);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param tTaskCompany 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTTaskCompany(TTaskCompanyWithCp tTaskCompany)
    {
        tTaskCompany.setCreateTime(DateUtils.getNowDate());
        return tTaskCompanyMapper.insertTTaskCompany(tTaskCompany);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param tTaskCompany 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTTaskCompany(TTaskCompanyWithCp tTaskCompany)
    {
        tTaskCompany.setUpdateTime(DateUtils.getNowDate());
        return tTaskCompanyMapper.updateTTaskCompany(tTaskCompany);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTTaskCompanyByIds(Long[] ids)
    {
        return tTaskCompanyMapper.deleteTTaskCompanyByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTTaskCompanyById(Long id)
    {
        return tTaskCompanyMapper.deleteTTaskCompanyById(id);
    }
}
