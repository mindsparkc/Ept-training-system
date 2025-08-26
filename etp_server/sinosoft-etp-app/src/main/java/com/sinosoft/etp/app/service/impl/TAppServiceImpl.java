package com.sinosoft.etp.app.service.impl;

import cn.jkingtools.utils.DateUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.etp.app.domain.TApp;
import com.sinosoft.etp.app.domain.TAppVersion;
import com.sinosoft.etp.app.mapper.TAppMapper;
import com.sinosoft.etp.app.mapper.TAppVersionMapper;
import com.sinosoft.etp.app.service.ITAppService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * app管理Service业务层处理
 *
 *
 * @date 2022-07-06
 */
@Service
public class TAppServiceImpl implements ITAppService
{
    @Resource
    private TAppMapper tAppMapper;

    @Resource
    private TAppVersionMapper tAppVersionMapper;

    /**
     * 查询app管理
     *
     * @param id app管理ID
     * @return app管理
     */
    @Override
    public TApp selectTAppById(Long id)
    {
        return tAppMapper.selectTAppById(id);
    }

    /**
     * 查询app管理列表
     *
     * @param tApp app管理
     * @return app管理
     */
    @Override
    public List<TApp> selectTAppList(TApp tApp)
    {
        List<TApp> before = tAppMapper.selectTAppList(tApp);
        List<TApp> after = new ArrayList<>();
        for (TApp t: before) {
            TAppVersion tAppVersion = tAppVersionMapper.selectTAppVersionByNow(t.getAppCode(),t.getPlatform());
            if(tAppVersion!=null){
                t.setVersionCode(tAppVersion.getVersionCode());
                t.setVersionDescribe(tAppVersion.getVersionDescribe());
            }
            after.add(t);
        }
        return after;
    }

    /**
     * 查询APP标识是否存在
     * @param appCode
     * @return
     */
    @Override
    public int selectAppCode(String appCode) {
        return tAppMapper.selectAppCode(appCode);
    }

    /**
     * 查询app版本号是否存在
     * @param id
     * @param versionCode
     * @return
     */
    @Override
    public int selectVersionCode(Long id, Long versionCode) {
        return tAppMapper.selectVersionCode(id,versionCode);
    }

    /**
     * 新增app管理
     *
     * @param tApp app管理
     * @return 结果
     */
    @Override
    public int insertTApp(TApp tApp)
    {
        tApp.setCreateTime(DateUtils.getNowDate());
        tApp.setCreateBy(SecurityUtils.getLoginUser().getUser().getUserName());
        return tAppMapper.insertTApp(tApp);
    }

    /**
     * 修改app管理
     *
     * @param tApp app管理
     * @return 结果
     */
    @Override
    public int updateTApp(TApp tApp)
    {
        tApp.setUpdateTime(DateUtils.getNowDate());
        return tAppMapper.updateTApp(tApp);
    }

    /**
     * 批量删除app管理
     *
     * @param ids 需要删除的app管理ID
     * @return 结果
     */
    @Override
    public int deleteTAppByIds(Long[] ids)
    {
        //删除app版本时，先删除详情信息
        for (Long id : ids) {
            tAppVersionMapper.deleteByAppId(id);
        }
        return tAppMapper.deleteTAppByIds(ids);
    }

    /**
     * 删除app管理信息
     *
     * @param id app管理ID
     * @return 结果
     */
    @Override
    public int deleteTAppById(Long id)
    {
        return tAppMapper.deleteTAppById(id);
    }
}
