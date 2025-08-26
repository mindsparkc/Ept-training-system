package com.sinosoft.etp.up.banner.service;

import com.sinosoft.etp.up.banner.domain.TBanner;

import java.util.List;


/**
 * 轮播图Service接口
 *
 *
 * @date 2022-02-22
 */
public interface ITBannerService
{
    /**
     * 查询轮播图
     *
     * @param id 轮播图ID
     * @return 轮播图
     */
    public TBanner selectTBannerById(Long id);

    /**
     * 查询轮播图列表
     *
     * @param source 来源
     * @return 轮播图集合
     */
    public List<TBanner> selectTBannerList(String source);

}
