package com.sinosoft.etp.cp.sign.service;

import com.sinosoft.etp.cp.sign.domain.TSignWithCp;

import java.util.List;

/**
 * signService接口
 *
 *
 * @date 2022-02-09
 */
public interface ITSignWithCpService
{
    /**
     * 查询sign
     *
     * @param id signID
     * @return sign
     */
    public TSignWithCp selectTSignById(Long id);

    /**
     * 查询sign列表
     *
     * @param tSign sign
     * @return sign集合
     */
    public List<TSignWithCp> selectTSignList(TSignWithCp tSign);

    /**
     * 新增sign
     *
     * @param tSign sign
     * @return 结果
     */
    public int insertTSign(TSignWithCp tSign);

    /**
     * 修改sign
     *
     * @param tSign sign
     * @return 结果
     */
    public int updateTSign(TSignWithCp tSign);

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
}
