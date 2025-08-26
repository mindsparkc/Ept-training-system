package com.sinosoft.system.service.impl;

import java.util.List;

import com.sinosoft.common.utils.IdUtil;
import cn.jkingtools.utils.DateUtils;
import org.springframework.stereotype.Service;
import com.sinosoft.system.mapper.SysTenantVipMapper;
import com.sinosoft.system.domain.SysTenantVip;
import com.sinosoft.system.service.ISysTenantVipService;

import javax.annotation.Resource;

/**
 * 租户vip资源控制Service业务层处理
 *
 * @author sinosoft
 * @date 2025-05-29
 */
@Service
public class SysTenantVipServiceImpl implements ISysTenantVipService {
    @Resource
    private SysTenantVipMapper sysTenantVipMapper;

    /**
     * 查询租户vip资源控制
     *
     * @param id 租户vip资源控制ID
     * @return 租户vip资源控制
     */
    @Override
    public SysTenantVip selectSysTenantVipById(Long id) {
        return sysTenantVipMapper.selectSysTenantVipById(id);
    }

    @Override
    public SysTenantVip selectSysTenantVipByCode(String vipCode) {
        return sysTenantVipMapper.selectSysTenantVipByCode(vipCode);
    }

    /**
     * 查询租户vip资源控制列表
     *
     * @param sysTenantVip 租户vip资源控制
     * @return 租户vip资源控制
     */
    @Override
    public List<SysTenantVip> selectSysTenantVipList(SysTenantVip sysTenantVip) {
        return sysTenantVipMapper.selectSysTenantVipList(sysTenantVip);
    }

    /**
     * 查询租户vip资源控制列表
     *
     * @param ids 需要查询的租户vip资源控制ID
     * @return 租户vip资源控制集合
     */
    public List<SysTenantVip> selectSysTenantVipByIds(Long[] ids) {
        return sysTenantVipMapper.selectSysTenantVipByIds(ids);
    }

    /**
     * 新增租户vip资源控制
     *
     * @param sysTenantVip 租户vip资源控制
     * @return 结果
     */
    @Override
    public int insertSysTenantVip(SysTenantVip sysTenantVip) {
        sysTenantVip.setId(IdUtil.nextId());
        sysTenantVip.setCreateTime(DateUtils.getNowDate());
        return sysTenantVipMapper.insertSysTenantVip(sysTenantVip);
    }

    /**
     * 修改租户vip资源控制
     *
     * @param sysTenantVip 租户vip资源控制
     * @return 结果
     */
    @Override
    public int updateSysTenantVip(SysTenantVip sysTenantVip) {
        sysTenantVip.setUpdateTime(DateUtils.getNowDate());
        return sysTenantVipMapper.updateSysTenantVip(sysTenantVip);
    }

    /**
     * 批量删除租户vip资源控制
     *
     * @param ids 需要删除的租户vip资源控制ID
     * @return 结果
     */
    @Override
    public int deleteSysTenantVipByIds(Long[] ids) {
        return sysTenantVipMapper.deleteSysTenantVipByIds(ids);
    }

    /**
     * 删除租户vip资源控制信息
     *
     * @param id 租户vip资源控制ID
     * @return 结果
     */
    @Override
    public int deleteSysTenantVipById(Long id) {
        return sysTenantVipMapper.deleteSysTenantVipById(id);
    }

    @Override
    public Integer getCount(String sql) {
        return sysTenantVipMapper.getCount(sql);
    }
}
