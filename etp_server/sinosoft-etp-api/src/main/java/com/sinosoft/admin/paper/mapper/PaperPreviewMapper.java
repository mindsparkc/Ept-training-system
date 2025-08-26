package com.sinosoft.admin.paper.mapper;

import com.sinosoft.admin.paper.domain.PaperPreview;

import java.util.List;

/**
 * 试卷预览Mapper接口
 *
 * @author sinosoft
 * @date 2022-10-08
 */
public interface PaperPreviewMapper
{
    /**
     * 查询试卷预览
     *
     * @param paperId 试卷预览ID
     * @return 试卷预览
     */
    public PaperPreview selectPaperPreviewById(Long paperId);

    /**
     * 查询试卷预览列表
     *
     * @param paperPreview 试卷预览
     * @return 试卷预览集合
     */
    public List<PaperPreview> selectPaperPreviewList(PaperPreview paperPreview);

    /**
     * 批量查询试卷预览列表
     *
     * @param paperIds 需要查询的试卷预览ID
     * @return 试卷预览集合
     */
    public List<PaperPreview> selectPaperPreviewByIds(Long[] paperIds);

    /**
     * 新增试卷预览
     *
     * @param paperPreview 试卷预览
     * @return 结果
     */
    public int insertPaperPreview(PaperPreview paperPreview);

    /**
     * 修改试卷预览
     *
     * @param paperPreview 试卷预览
     * @return 结果
     */
    public int updatePaperPreview(PaperPreview paperPreview);

    /**
     * 删除试卷预览
     *
     * @param paperId 试卷预览ID
     * @return 结果
     */
    public int deletePaperPreviewById(Long paperId);

    /**
     * 批量删除试卷预览
     *
     * @param paperIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePaperPreviewByIds(Long[] paperIds);
}