package com.sinosoft.etp.cp.common.mapper;

import com.sinosoft.etp.cp.common.domain.TAnalysisCountWithCp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 86151
 */
@Mapper
public interface TAnalysisCountWithCpMapper {
    /**
     * 查询近七天的数据统计
     *
     * @param id 近七天的数据统计ID
     * @return 近七天的数据统计
     */
    public TAnalysisCountWithCp selectTAnalysisCountById(Long id);

    /**
     * 查询近七天的数据统计列表
     *
     * @param tAnalysisCount 近七天的数据统计
     * @return 近七天的数据统计集合
     */
    public List<TAnalysisCountWithCp> selectTAnalysisCountList(TAnalysisCountWithCp tAnalysisCount);

    /**
     * 新增近七天的数据统计
     *
     * @param tAnalysisCount 近七天的数据统计
     * @return 结果
     */
    public int insertTAnalysisCount(TAnalysisCountWithCp tAnalysisCount);

    /**
     * 修改近七天的数据统计
     *
     * @param tAnalysisCount 近七天的数据统计
     * @return 结果
     */
    public int updateTAnalysisCount(TAnalysisCountWithCp tAnalysisCount);

    /**
     * 删除近七天的数据统计
     *
     * @param id 近七天的数据统计ID
     * @return 结果
     */
    public int deleteTAnalysisCountById(Long id);

    /**
     * 批量删除近七天的数据统计
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTAnalysisCountByIds(Long[] ids);
}
