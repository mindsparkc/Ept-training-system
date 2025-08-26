package com.sinosoft.system.mapper;

import com.sinosoft.system.domain.SysNoticeCompany;

import java.util.List;

/**
 * 通知公告Mapper接口
 *
 *
 * @date 2022-02-09
 */
public interface SysNoticeCompanyMapper
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
     * 删除通知公告
     *
     * @param noticeId 通知公告ID
     * @return 结果
     */
    public int deleteSysNoticeCompanyByNoticeId(Long noticeId);

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysNoticeCompanyByNoticeIds(Long[] noticeIds);
}
