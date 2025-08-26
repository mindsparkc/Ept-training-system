package com.sinosoft.system.service.impl;

import com.sinosoft.system.domain.SysNoticeCompany;
import com.sinosoft.system.mapper.SysNoticeCompanyMapper;
import com.sinosoft.system.service.ISysNoticeCompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知公告Service业务层处理
 *
 * 
 * @date 2022-02-09
 */
@Service
public class SysNoticeCompanyServiceImpl implements ISysNoticeCompanyService
{
    @Resource
    private SysNoticeCompanyMapper sysNoticeCompanyMapper;

    /**
     * 查询通知公告
     *
     * @param id 通知公告ID
     * @return 通知公告
     */
    @Override
    public SysNoticeCompany selectSysNoticeCompanyById(Long id)
    {
        return sysNoticeCompanyMapper.selectSysNoticeCompanyById(id);
    }

    /**
     * 查询通知公告列表
     *
     * @param sysNoticeCompany 通知公告
     * @return 通知公告
     */
    @Override
    public List<SysNoticeCompany> selectSysNoticeCompanyList(SysNoticeCompany sysNoticeCompany)
    {
        return sysNoticeCompanyMapper.selectSysNoticeCompanyList(sysNoticeCompany);
    }

    /**
     * 新增通知公告
     *
     * @param sysNoticeCompany 通知公告
     * @return 结果
     */
    @Override
    public int insertSysNoticeCompany(SysNoticeCompany sysNoticeCompany)
    {
        return sysNoticeCompanyMapper.insertSysNoticeCompany(sysNoticeCompany);
    }

    /**
     * 修改通知公告
     *
     * @param sysNoticeCompany 通知公告
     * @return 结果
     */
    @Override
    public int updateSysNoticeCompany(SysNoticeCompany sysNoticeCompany)
    {
        return sysNoticeCompanyMapper.updateSysNoticeCompany(sysNoticeCompany);
    }

    /**
     * 批量删除通知公告
     *
     * @param ids 需要删除的通知公告ID
     * @return 结果
     */
    @Override
    public int deleteSysNoticeCompanyByIds(Long[] ids)
    {
        return sysNoticeCompanyMapper.deleteSysNoticeCompanyByNoticeIds(ids);
    }

    /**
     * 删除通知公告信息
     *
     * @param id 通知公告ID
     * @return 结果
     */
    @Override
    public int deleteSysNoticeCompanyById(Long id)
    {
        return sysNoticeCompanyMapper.deleteSysNoticeCompanyByNoticeId(id);
    }
}
