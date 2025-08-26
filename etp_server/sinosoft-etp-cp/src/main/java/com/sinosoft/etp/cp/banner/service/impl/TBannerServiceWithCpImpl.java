package com.sinosoft.etp.cp.banner.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.annotation.DataScope;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.cp.banner.domain.TBannerWithCp;
import com.sinosoft.etp.cp.banner.mapper.TBannerCompanyWithCpMapper;
import com.sinosoft.etp.cp.banner.mapper.TBannerWithCpMapper;
import com.sinosoft.etp.cp.banner.service.ITBannerWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图Service业务层处理
 *
 *
 * @date 2022-03-09
 */
@Service
public class TBannerServiceWithCpImpl implements ITBannerWithCpService
{
    @Autowired
    private TBannerWithCpMapper tBannerMapper;

    @Autowired
    private TBannerCompanyWithCpMapper tBannerCompanyMapper;

    /**
     * 查询轮播图
     *
     * @param id 轮播图ID
     * @return 轮播图
     */
    @Override
    public TBannerWithCp selectTBannerById(Long id)
    {
        return tBannerMapper.selectTBannerById(id);
    }

    /**
     * 查询轮播图列表
     *
     * @param tBanner 轮播图
     * @return 轮播图
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<TBannerWithCp> selectTBannerList(TBannerWithCp tBanner)
    {
        return tBannerMapper.selectTBannerList(tBanner);
    }

    /**
     * 新增轮播图
     *
     * @param tBanner 轮播图
     * @return 结果
     */
    @Override
    public int insertTBanner(TBannerWithCp tBanner)
    {
        tBanner.setCreateTime(DateUtils.getNowDate());
        tBanner.setUpdateTime(DateUtils.getNowDate());
        tBanner.setCreateBy(SecurityUtils.getUsername());
        tBanner.setUpdateBy(SecurityUtils.getUsername());
        int i = tBannerMapper.insertTBanner(tBanner);
        //TBannerCompanyWithCp tBannerCompany = new TBannerCompanyWithCp();
        //tBannerCompany.setQyid(SecurityUtils.getLoginUser().getUser().getDeptId());
        //tBannerCompany.setBid(tBanner.getId());
        //tBannerCompanyMapper.insertTBannerCompany(tBannerCompany);

        return i;
    }


}
