package com.sinosoft.etp.app.mapper;


import com.sinosoft.etp.app.domain.TApp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * app管理Mapper接口
 *
 *
 * @date 2022-07-06
 */
public interface TAppMapper
{
    /**
     * 查询app管理
     *
     * @param id app管理ID
     * @return app管理
     */
    public TApp selectTAppById(Long id);

    /**
     * 查询app管理列表
     *
     * @param tApp app管理
     * @return app管理集合
     */
    public List<TApp> selectTAppList(TApp tApp);

    /**
     * 新增app管理
     *
     * @param tApp app管理
     * @return 结果
     */
    public int insertTApp(TApp tApp);

    /**
     * 修改app管理
     *
     * @param tApp app管理
     * @return 结果
     */
    public int updateTApp(TApp tApp);

    /**
     * 删除app管理
     *
     * @param id app管理ID
     * @return 结果
     */
    public int deleteTAppById(Long id);

    /**
     * 批量删除app管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTAppByIds(Long[] ids);

    /**
     * 查询app标识是否存在
     * @param appCode
     * @return
     */
    public int selectAppCode(@Param("appCode") String appCode);

    /**
     * 查询app版本号是否存在
     * @param id
     * @param versionCode
     * @return
     */
    public int selectVersionCode(@Param("id")Long id,@Param("versionCode") Long versionCode);
}
