package com.sinosoft.etp.up.sign.service;

import com.sinosoft.etp.up.sign.domain.TSign;

import java.util.List;
import java.util.Map;

/**
 * signService接口
 *
 *
 * @date 2022-02-09
 */
public interface ITSignService
{
    /**
     * 查询sign
     *
     * @param id signID
     * @return sign
     */
    public TSign selectTSignById(Long id);

    /**
     * 查询sign列表
     *
     * @param tSign sign
     * @return sign集合
     */
    public List<TSign> selectTSignList(TSign tSign);

    /**
     * 新增sign
     *
     * @param tSign sign
     * @return 结果
     */
    public int insertTSign(TSign tSign);

    /**
     * 修改活动报名状态
     *
     * @param tSign sign
     * @return 结果
     */
    public int updateTSign(TSign tSign);

    /**
     * 批量删除sign
     *
     * @param ids 需要删除的signID
     * @return 结果
     */
    public int deleteTSignByIds(Long[] ids);

    /**
     * 删除sign信息
     *
     * @param id signID
     * @return 结果
     */
    public int deleteTSignById(Long id);

    /**
     * 获取首页活动
     * @return
     */
    public Map<String,Object> getHomeActivite();
}
