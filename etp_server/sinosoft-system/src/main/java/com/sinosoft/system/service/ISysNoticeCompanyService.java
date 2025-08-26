package com.sinosoft.system.service;

import com.sinosoft.system.domain.SysNoticeCompany;

import java.util.List;

/**
 * 通知公告Service接口
 *
 *
 * @date 2022-02-09
 */
public interface ISysNoticeCompanyService
{
    /**
     * 查询通知公告
     *
     * @param id 通知公告ID
     * @return 通知公告
     */
    public SysNoticeCompany selectSysNoticeCompanyById(Long id);

    /**
     * 查询通知公告列表
     *
     * @param sysNoticeCompany 通知公告
     * @return 通知公告集合
     */
    public List<SysNoticeCompany> selectSysNoticeCompanyList(SysNoticeCompany sysNoticeCompany);

    /**
     * 新增通知公告
     *
     * @param sysNoticeCompany 通知公告
     * @return 结果
     */
    public int insertSysNoticeCompany(SysNoticeCompany sysNoticeCompany);

    /**
     * 修改通知公告
     *
     * @param sysNoticeCompany 通知公告
     * @return 结果
     */
    public int updateSysNoticeCompany(SysNoticeCompany sysNoticeCompany);

    /**
     * 批量删除通知公告
     *
     * @param ids 需要删除的通知公告ID
     * @return 结果
     */
    public int deleteSysNoticeCompanyByIds(Long[] ids);

    /**
     * 删除通知公告信息
     *
     * @param id 通知公告ID
     * @return 结果
     */
    public int deleteSysNoticeCompanyById(Long id);
}
