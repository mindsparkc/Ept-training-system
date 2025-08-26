package com.sinosoft.etp.up.banner.mapper;


import com.sinosoft.etp.up.banner.domain.TBanner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 轮播图Mapper接口
 *
 *
 * @date 2022-02-22
 */
public interface TBannerMapper
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
     * @return 轮播图集合
     */
    public List<TBanner> selectTBannerList(@Param("source") String source,@Param("qyid")Long qyid);

}
