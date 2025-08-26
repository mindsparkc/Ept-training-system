package com.sinosoft.etp.up.banner.service.impl;

import com.sinosoft.etp.up.banner.domain.TBanner;
import com.sinosoft.etp.up.banner.mapper.TBannerMapper;
import com.sinosoft.etp.up.banner.service.ITBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图Service业务层处理
 *
 *
 * @date 2022-02-22
 */
@Service
public class TBannerServiceImpl implements ITBannerService
{
    @Autowired
    private TBannerMapper tBannerMapper;

    /**
     * 查询轮播图
     *
     * @param id 轮播图ID
     * @return 轮播图
     */
    @Override
    public TBanner selectTBannerById(Long id)
    {
        return tBannerMapper.selectTBannerById(id);
    }

    /**
     * 查询轮播图列表
     *
     * @return 轮播图
     */
    @Override
    public List<TBanner> selectTBannerList(String source) {
        return tBannerMapper.selectTBannerList(source,0L);
    }


}
