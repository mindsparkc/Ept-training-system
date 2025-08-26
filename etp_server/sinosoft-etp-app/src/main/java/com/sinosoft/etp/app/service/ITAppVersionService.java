package com.sinosoft.etp.app.service;


import com.sinosoft.etp.app.domain.TAppVersion;

import java.util.List;

/**
 * app版本管理Service接口
 *
 * 
 * @date 2022-07-06
 */
public interface ITAppVersionService
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
     * @param platform app平台
     * @return app最新版本
     */
    public TAppVersion selectTAppVersionByNow(String appCode,String platform);

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
     * 批量删除app版本管理
     *
     * @param ids 需要删除的app版本管理ID
     * @return 结果
     */
    public int deleteTAppVersionByIds(Long[] ids);

    /**
     * 删除app版本管理信息
     *
     * @param id app版本管理ID
     * @return 结果
     */
    public int deleteTAppVersionById(Long id);
}
