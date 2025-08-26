package com.sinosoft.etp.cp.examination.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.cp.examination.domain.TExaminationCompanyWithCp;
import com.sinosoft.etp.cp.examination.mapper.TExaminationCompanyWithCpMapper;
import com.sinosoft.etp.cp.examination.service.ITExaminationCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考试企业关联Service业务层处理
 *
 *
 * @date 2021-12-28
 */
@Service
public class TExaminationCompanyWithCpServiceImpl implements ITExaminationCompanyWithCpService
{
    @Autowired
    private TExaminationCompanyWithCpMapper tExaminationCompanyMapper;

    /**
     * 查询考试企业关联
     *
     * @param id 考试企业关联ID
     * @return 考试企业关联
     */
    @Override
    public TExaminationCompanyWithCp selectTExaminationCompanyById(Long id)
    {
        return tExaminationCompanyMapper.selectTExaminationCompanyById(id);
    }

    /**
     * 查询考试企业关联列表
     *
     * @param tExaminationCompany 考试企业关联
     * @return 考试企业关联
     */
    @Override
    public List<TExaminationCompanyWithCp> selectTExaminationCompanyList(TExaminationCompanyWithCp tExaminationCompany)
    {
        return tExaminationCompanyMapper.selectTExaminationCompanyList(tExaminationCompany);
    }

    /**
     * 新增考试企业关联
     *
     * @param tExaminationCompany 考试企业关联
     * @return 结果
     */
    @Override
    public int insertTExaminationCompany(TExaminationCompanyWithCp tExaminationCompany)
    {
        tExaminationCompany.setCreateTime(DateUtils.getNowDate());
        return tExaminationCompanyMapper.insertTExaminationCompany(tExaminationCompany);
    }

    /**
     * 修改考试企业关联
     *
     * @param tExaminationCompany 考试企业关联
     * @return 结果
     */
    @Override
    public int updateTExaminationCompany(TExaminationCompanyWithCp tExaminationCompany)
    {
        tExaminationCompany.setUpdateTime(DateUtils.getNowDate());
        return tExaminationCompanyMapper.updateTExaminationCompany(tExaminationCompany);
    }

    /**
     * 批量删除考试企业关联
     *
     * @param ids 需要删除的考试企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTExaminationCompanyByIds(Long[] ids)
    {
        return tExaminationCompanyMapper.deleteTExaminationCompanyByIds(ids);
    }

    /**
     * 删除考试企业关联信息
     *
     * @param id 考试企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTExaminationCompanyById(Long id)
    {
        return tExaminationCompanyMapper.deleteTExaminationCompanyById(id);
    }
}
