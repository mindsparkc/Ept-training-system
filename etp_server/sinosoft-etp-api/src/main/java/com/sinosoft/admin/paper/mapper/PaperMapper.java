package com.sinosoft.admin.paper.mapper;

import com.sinosoft.admin.paper.domain.PaperQuery;
import com.sinosoft.core.domain.Paper;

import java.util.List;

/**
 * 试卷基础信息Mapper接口
 *
 *
 * @date 2022-08-24
 */
public interface PaperMapper
{
    /**
     * 查询试卷基础信息
     *
     * @param paperId 试卷基础信息ID
     * @return 试卷基础信息
     */
    public Paper selectPaperById(Long paperId);

    /**
     * 查询试卷基础信息列表
     *
     * @param paper 试卷基础信息
     * @return 试卷基础信息集合
     */
    public List<Paper> selectPaperList(PaperQuery paper);

    /**
     * 批量查询试卷基础信息列表
     *
     * @param paperIds 需要查询的试卷基础信息ID
     * @return 试卷基础信息集合
     */
    public List<Paper> selectPaperByIds(Long[] paperIds);


    /**
     * 新增试卷基础信息
     *
     * @param paper 试卷基础信息
     * @return 结果
     */
    public int insertPaper(Paper paper);

    /**
     * 修改试卷基础信息
     *
     * @param paper 试卷基础信息
     * @return 结果
     */
    public int updatePaper(Paper paper);
    public int updatePaperStatus(Paper paper);


    /**
     * 删除试卷基础信息
     *
     * @param paperId 试卷基础信息ID
     * @return 结果
     */
    public int deletePaperById(Long paperId);

    /**
     * 批量删除试卷基础信息
     *
     * @param paperIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePaperByIds(Long[] paperIds);

    public List<Paper> getSelectedXtjByKc(Long kcid);
}
