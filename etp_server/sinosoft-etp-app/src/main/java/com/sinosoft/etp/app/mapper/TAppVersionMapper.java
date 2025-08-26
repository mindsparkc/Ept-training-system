package com.sinosoft.etp.app.mapper;


import com.sinosoft.etp.app.domain.TAppVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * app版本管理Mapper接口
 *
 *
 * @date 2022-07-06
 */
public interface TAppVersionMapper
{
    /**
     * 查询app版本管理
     *
     * @param id app版本管理ID
     * @return app版本管理
     */
    public TAppVersion selectTAppVersionById(Long id);


    /**
     * 查询app最新版本
     *
     * @param appCode app标识
     * @param platform app平台信息
     * @return app最新版本
     */
    public TAppVersion selectTAppVersionByNow(@Param("appCode") String appCode, @Param("platform") String platform);

    /**
     * 查询app版本管理列表
     *
     * @param tAppVersion app版本管理
     * @return app版本管理集合
     */
    public List<TAppVersion> selectTAppVersionList(TAppVersion tAppVersion);

    /**
     * 新增app版本管理
     *
     * @param tAppVersion app版本管理
     * @return 结果
     */
    public int insertTAppVersion(TAppVersion tAppVersion);

    /**
     * 修改app版本管理
     *
     * @param tAppVersion app版本管理
     * @return 结果
     */
    public int updateTAppVersion(TAppVersion tAppVersion);

    /**
     * 删除app版本管理
     *
     * @param id app版本管理ID
     * @return 结果
     */
    public int deleteTAppVersionById(Long id);

    /**
     * 批量删除app版本管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTAppVersionByIds(Long[] ids);

    /**
     * 根据appId批量删除数据
     * @param appId
     * @return
     */
    public int deleteByAppId(Long appId);
}
