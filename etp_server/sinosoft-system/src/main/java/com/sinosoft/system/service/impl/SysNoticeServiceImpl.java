package com.sinosoft.system.service.impl;

import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.domain.SysNotice;
import com.sinosoft.system.domain.SysNoticeCompany;
import com.sinosoft.system.mapper.SysNoticeCompanyMapper;
import com.sinosoft.system.mapper.SysNoticeMapper;
import com.sinosoft.system.service.ISysNoticeService;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 公告 服务层实现
 *
 *
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Resource
    private SysNoticeMapper noticeMapper;

    @Resource
    private SysNoticeCompanyMapper sysNoticeCompanyMapper;

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId)
    {
        return noticeMapper.selectNoticeById(noticeId);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    @DataScope(deptAlias = "sd") // bug 部门关联已删除了
    @EtpDataScope(alias = "sn")
    public List<SysNotice> selectNoticeList(SysNotice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertNotice(SysNotice notice)
    {
        EtpDataScopeUtils.setDataScope(notice);// 20250520 lyd etp数据权限相关字段
        int res = noticeMapper.insertNotice(notice);
        SysNoticeCompany sysNoticeCompany = new SysNoticeCompany();
        sysNoticeCompany.setNoticeId(notice.getNoticeId());
        sysNoticeCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //res += sysNoticeCompanyMapper.insertSysNoticeCompany(sysNoticeCompany);
        return res;
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice)
    {
        notice.setUpdateBy(SecurityUtils.getUsername());
        notice.setUpdateTime(new Date());
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteNoticeById(Long noticeId)
    {
        //sysNoticeCompanyMapper.deleteSysNoticeCompanyByNoticeId(noticeId);
        return noticeMapper.deleteNoticeById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteNoticeByIds(Long[] noticeIds)
    {
        //sysNoticeCompanyMapper.deleteSysNoticeCompanyByNoticeIds(noticeIds);
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }
}
