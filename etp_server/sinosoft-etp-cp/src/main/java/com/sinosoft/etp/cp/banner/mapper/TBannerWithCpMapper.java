package com.sinosoft.etp.cp.banner.mapper;

import com.sinosoft.etp.cp.banner.domain.TBannerWithCp;

import java.util.List;

/**
 * 轮播图Mapper接口
 *
 *
 * @date 2022-03-09
 */
public interface TBannerWithCpMapper
{
    /**
     * 查询轮播图
     *
     * @param id 轮播图ID
     * @return 轮播图
     */
    public TBannerWithCp selectTBannerById(Long id);

    /**
     * 查询轮播图列表
     *
     * @param tBanner 轮播图
     * @return 轮播图集合
     */
    public List<TBannerWithCp> selectTBannerList(TBannerWithCp tBanner);

    /**
     * 新增轮播图
     *
     * @param tBanner 轮播图
     * @return 结果
     */
    public int insertTBanner(TBannerWithCp tBanner);


}
