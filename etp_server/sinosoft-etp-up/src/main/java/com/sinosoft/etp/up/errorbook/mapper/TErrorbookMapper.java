package com.sinosoft.etp.up.errorbook.mapper;

import com.sinosoft.etp.up.errorbook.domain.TErrorbook;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 错题集Mapper接口
 *
 *
 * @date 2022-05-11
 */
public interface TErrorbookMapper
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
     * 删除错题集
     *
     * @param id 错题集ID
     * @return 结果
     */
    public int deleteTErrorbookById(Long id);

    /**
     * 批量删除错题集
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTErrorbookByIds(Long[] ids);

    /**
     * 考试错题判重逻辑
     *
     * @param ksid 考试ID
     * @param stid 试题id
     * @param xyid 学员id
     * @return 错题集
     */
    public Long selectTErrorbookByKs(@Param("ksid") Long ksid,@Param("kcid")Long kcid,@Param("stid")Long stid,@Param("xyid")Long xyid);

}
