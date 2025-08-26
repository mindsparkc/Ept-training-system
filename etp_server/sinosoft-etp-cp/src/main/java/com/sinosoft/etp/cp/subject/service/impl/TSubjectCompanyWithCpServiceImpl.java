package com.sinosoft.etp.cp.subject.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.cp.subject.domain.TSubjectCompanyWithCp;
import com.sinosoft.etp.cp.subject.mapper.TSubjectCompanyWithCpMapper;
import com.sinosoft.etp.cp.subject.service.ITSubjectCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 题库企业关联Service业务层处理
 *
 *
 * @date 2021-11-12
 */
@Service
public class TSubjectCompanyWithCpServiceImpl implements ITSubjectCompanyWithCpService
{
    @Autowired
    private TSubjectCompanyWithCpMapper tSubjectCompanyMapper;

    /**
     * 查询题库企业关联
     *
     * @param id 题库企业关联ID
     * @return 题库企业关联
     */
    @Override
    public TSubjectCompanyWithCp selectTSubjectCompanyById(Long id)
    {
        return tSubjectCompanyMapper.selectTSubjectCompanyById(id);
    }

    /**
     * 查询题库企业关联列表
     *
     * @param tSubjectCompany 题库企业关联
     * @return 题库企业关联
     */
    @Override
    public List<TSubjectCompanyWithCp> selectTSubjectCompanyList(TSubjectCompanyWithCp tSubjectCompany)
    {
        return tSubjectCompanyMapper.selectTSubjectCompanyList(tSubjectCompany);
    }

    /**
     * 新增题库企业关联
     *
     * @param tSubjectCompany 题库企业关联
     * @return 结果
     */
    @Override
    public int insertTSubjectCompany(TSubjectCompanyWithCp tSubjectCompany)
    {
        tSubjectCompany.setCreateTime(DateUtils.getNowDate());
        return tSubjectCompanyMapper.insertTSubjectCompany(tSubjectCompany);
    }

    /**
     * 修改题库企业关联
     *
     * @param tSubjectCompany 题库企业关联
     * @return 结果
     */
    @Override
    public int updateTSubjectCompany(TSubjectCompanyWithCp tSubjectCompany)
    {
        tSubjectCompany.setUpdateTime(DateUtils.getNowDate());
        return tSubjectCompanyMapper.updateTSubjectCompany(tSubjectCompany);
    }

    /**
     * 批量删除题库企业关联
     *
     * @param ids 需要删除的题库企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTSubjectCompanyByIds(Long[] ids)
    {
        return tSubjectCompanyMapper.deleteTSubjectCompanyByIds(ids);
    }

    /**
     * 删除题库企业关联信息
     *
     * @param id 题库企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTSubjectCompanyById(Long id)
    {
        return tSubjectCompanyMapper.deleteTSubjectCompanyById(id);
    }
}
