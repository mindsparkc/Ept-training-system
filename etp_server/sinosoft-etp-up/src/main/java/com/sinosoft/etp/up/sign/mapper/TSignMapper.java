package com.sinosoft.etp.up.sign.mapper;

import com.sinosoft.etp.up.sign.domain.TSign;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * signMapper接口
 *
 *
 * @date 2022-02-09
 */
public interface TSignMapper
{
    /**
     * 查询sign
     *
     * @param id signID
     * @return sign
     */
    public TSign selectTSignById(@Param("id") Long id,@Param("xyid") Long xyid);

    /**
     * 查询sign列表
     *
     * @param ids sign
     * @return sign集合
     */
    public List<TSign> selectTSignByIds(Long[] ids);

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
     * 修改sign
     *
     * @param tSign sign
     * @return 结果
     */
    public int updateTSign(TSign tSign);

    /**
     * 删除sign
     *
     * @param id signID
     * @return 结果
     */
    public int deleteTSignById(Long id);

    /**
     * 批量删除sign
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTSignByIds(Long[] ids);


    public int updateSignStatus(TSign tSign);

    /**
     * 统计报名人数
     * @param id
     * @return
     */
    public int countJoinSignById(Long id);

    /**
     * 获取首页活动
     * @param xyid
     * @return
     */
    public Map<String,Object> getHomeActivite(Long xyid);

}
