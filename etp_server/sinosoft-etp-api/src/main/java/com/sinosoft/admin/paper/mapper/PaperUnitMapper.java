package com.sinosoft.admin.paper.mapper;

import com.sinosoft.core.domain.PaperUnit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷组成单元Mapper接口
 *
 *
 * @date 2022-08-24
 */
public interface PaperUnitMapper
{
    /**
     * 查询试卷组成单元
     *
     * @param paperId 试卷ID
     * @return 试卷组成单元
     */
    public List<PaperUnit> selectPaperUnitByPaperId(Long paperId);

    /**
     * 批量插入试卷组成单元列表
     *
     * @param paperUnits
     * @return
     */
    public int batchInsertPaperUnit(@Param("paperUnits") List<PaperUnit> paperUnits);

    /**
     * 删除试卷组成单元
     *
     * @param paperId 试卷组成单元ID
     * @return 结果
     */
    public int deletePaperUnitByPaperId(Long paperId);

    /**
     * 批量删除试卷组成单元
     *
     * @param paperIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePaperUnitByPaperIds(Long[] paperIds);
}
