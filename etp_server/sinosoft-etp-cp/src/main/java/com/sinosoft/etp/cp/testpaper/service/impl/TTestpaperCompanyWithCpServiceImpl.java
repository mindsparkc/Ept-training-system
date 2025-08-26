package com.sinosoft.etp.cp.testpaper.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.etp.cp.testpaper.domain.TTestpaperCompanyWithCp;
import com.sinosoft.etp.cp.testpaper.mapper.TTestpaperCompanyWithCpMapper;
import com.sinosoft.etp.cp.testpaper.service.ITTestpaperCompanyWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试卷企业关联Service业务层处理
 *
 *
 * @date 2021-11-22
 */
@Service
public class TTestpaperCompanyWithCpServiceImpl implements ITTestpaperCompanyWithCpService
{
    @Autowired
    private TTestpaperCompanyWithCpMapper tTestpaperCompanyMapper;

    /**
     * 查询试卷企业关联
     *
     * @param id 试卷企业关联ID
     * @return 试卷企业关联
     */
    @Override
    public TTestpaperCompanyWithCp selectTTestpaperCompanyById(Long id)
    {
        return tTestpaperCompanyMapper.selectTTestpaperCompanyById(id);
    }

    /**
     * 查询试卷企业关联列表
     *
     * @param tTestpaperCompany 试卷企业关联
     * @return 试卷企业关联
     */
    @Override
    public List<TTestpaperCompanyWithCp> selectTTestpaperCompanyList(TTestpaperCompanyWithCp tTestpaperCompany)
    {
        return tTestpaperCompanyMapper.selectTTestpaperCompanyList(tTestpaperCompany);
    }

    /**
     * 新增试卷企业关联
     *
     * @param tTestpaperCompany 试卷企业关联
     * @return 结果
     */
    @Override
    public int insertTTestpaperCompany(TTestpaperCompanyWithCp tTestpaperCompany)
    {
        tTestpaperCompany.setCreateTime(DateUtils.getNowDate());
        return tTestpaperCompanyMapper.insertTTestpaperCompany(tTestpaperCompany);
    }

    /**
     * 修改试卷企业关联
     *
     * @param tTestpaperCompany 试卷企业关联
     * @return 结果
     */
    @Override
    public int updateTTestpaperCompany(TTestpaperCompanyWithCp tTestpaperCompany)
    {
        tTestpaperCompany.setUpdateTime(DateUtils.getNowDate());
        return tTestpaperCompanyMapper.updateTTestpaperCompany(tTestpaperCompany);
    }

    /**
     * 批量删除试卷企业关联
     *
     * @param ids 需要删除的试卷企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTTestpaperCompanyByIds(Long[] ids)
    {
        return tTestpaperCompanyMapper.deleteTTestpaperCompanyByIds(ids);
    }

    /**
     * 删除试卷企业关联信息
     *
     * @param id 试卷企业关联ID
     * @return 结果
     */
    @Override
    public int deleteTTestpaperCompanyById(Long id)
    {
        return tTestpaperCompanyMapper.deleteTTestpaperCompanyById(id);
    }
}
