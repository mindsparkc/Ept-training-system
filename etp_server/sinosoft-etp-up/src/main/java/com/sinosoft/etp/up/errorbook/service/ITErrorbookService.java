package com.sinosoft.etp.up.errorbook.service;

import com.sinosoft.etp.up.errorbook.domain.TErrorbook;

import java.util.List;

/**
 * 错题集Service接口
 *
 *
 * @date 2022-05-11
 */
public interface ITErrorbookService
{
    /**
     * 查询错题集
     *
     * @param id 错题集ID
     * @return 错题集
     */
    public TErrorbook selectTErrorbookById(Long id);

    /**
     * 查询错题集列表
     *
     * @param tErrorbook 错题集
     * @return 错题集集合
     */
    public List<TErrorbook> selectTErrorbookList(TErrorbook tErrorbook);

    /**
     * 新增错题集
     *
     * @param tErrorbook 错题集
     * @return 结果
     */
    public int insertTErrorbook(TErrorbook tErrorbook);

    /**
     * 修改错题集
     *
     * @param tErrorbook 错题集
     * @return 结果
     */
    public int updateTErrorbook(TErrorbook tErrorbook);

    /**
     * 批量删除错题集
     *
     * @param ids 需要删除的错题集ID
     * @return 结果
     */
    public int deleteTErrorbookByIds(Long[] ids);

    /**
     * 删除错题集信息
     *
     * @param id 错题集ID
     * @return 结果
     */
    public int deleteTErrorbookById(Long id);
}
