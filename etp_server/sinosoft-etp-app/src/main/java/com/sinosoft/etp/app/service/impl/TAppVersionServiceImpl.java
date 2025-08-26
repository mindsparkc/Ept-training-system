package com.sinosoft.etp.app.service.impl;


import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.app.domain.TAppVersion;
import com.sinosoft.etp.app.mapper.TAppVersionMapper;
import com.sinosoft.etp.app.service.ITAppVersionService;
import com.sinosoft.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * app版本管理Service业务层处理
 *
 *
 * @date 2022-07-06
 */
@Service
public class TAppVersionServiceImpl implements ITAppVersionService
{
    @Autowired
    private TAppVersionMapper tAppVersionMapper;

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询app版本管理
     *
     * @param id app版本管理ID
     * @return app版本管理
     */
    @Override
    public TAppVersion selectTAppVersionById(Long id)
    {
        return tAppVersionMapper.selectTAppVersionById(id);
    }

    /**
     * 获取app最新版本
     * @param appCode app标识
     * @param platform app平台
     * @return
     */
    @Override
    public TAppVersion selectTAppVersionByNow(String appCode, String platform) {
        TAppVersion tAppVersion = tAppVersionMapper.selectTAppVersionByNow(appCode,platform);
        if(tAppVersion != null && tAppVersion.getDownload() instanceof String){
            tAppVersion.setDownload(tAppVersion.getDownload().replace("{storage.path.url}",serverConfig.getUrl()));
        }
        return tAppVersion;
    }

    /**
     * 查询app版本管理列表
     *
     * @param tAppVersion app版本管理
     * @return app版本管理
     */
    @Override
    public List<TAppVersion> selectTAppVersionList(TAppVersion tAppVersion)
    {
        return tAppVersionMapper.selectTAppVersionList(tAppVersion);
    }

    /**
     * 新增app版本管理
     *
     * @param tAppVersion app版本管理
     * @return 结果
     */
    @Override
    public int insertTAppVersion(TAppVersion tAppVersion)
    {
        tAppVersion.setCreateTime(DateUtils.getNowDate());
        tAppVersion.setUpdateTime(DateUtils.getNowDate());
        tAppVersion.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserName());
        return tAppVersionMapper.insertTAppVersion(tAppVersion);
    }

    /**
     * 修改app版本管理
     *
     * @param tAppVersion app版本管理
     * @return 结果
     */
    @Override
    public int updateTAppVersion(TAppVersion tAppVersion)
    {
        tAppVersion.setUpdateTime(DateUtils.getNowDate());
        tAppVersion.setUpdateBy(SecurityUtils.getLoginUser().getUser().getUserName());
        return tAppVersionMapper.updateTAppVersion(tAppVersion);
    }

    /**
     * 批量删除app版本管理
     *
     * @param ids 需要删除的app版本管理ID
     * @return 结果
     */
    @Override
    public int deleteTAppVersionByIds(Long[] ids)
    {
        return tAppVersionMapper.deleteTAppVersionByIds(ids);
    }

    /**
     * 删除app版本管理信息
     *
     * @param id app版本管理ID
     * @return 结果
     */
    @Override
    public int deleteTAppVersionById(Long id)
    {
        return tAppVersionMapper.deleteTAppVersionById(id);
    }
}
