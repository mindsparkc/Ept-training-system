package com.sinosoft.admin.paper.service;

import com.sinosoft.admin.paper.domain.PaperQuery;
import com.sinosoft.admin.paper.domain.PaperReqDTO;
import com.sinosoft.admin.paper.domain.PaperRespDTO;
import com.sinosoft.core.domain.Paper;
import com.sinosoft.user.exam.domain.UserExam;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 试卷基础信息Service接口
 *
 *
 * @date 2022-08-24
 */
public interface IPaperService
{
    /**
     * 查询试卷基础信息
     *
     * @param paperId 试卷基础信息ID
     * @return 试卷基础信息
     */
    public PaperRespDTO selectPaperById(Long paperId);

    /**
     * 查询试卷基础信息列表
     *
     * @param paper 试卷基础信息
     * @return 试卷基础信息集合
     */
    public List<Paper> selectPaperList(PaperQuery paper);

    /**
     * 预览试卷
     * @param paperId
     */
    public UserExam previewPaperById(Long paperId);

    /**
     * 新增试卷基础信息
     *
     * @param paperDTO 试卷基础信息
     * @return 结果
     */
    public Long insertPaper(PaperReqDTO paperDTO) throws InvocationTargetException, IllegalAccessException;

    /**
     * 修改试卷基础信息
     *
     * @param paperDTO 试卷基础信息
     * @return 结果
     */
    public int updatePaper(PaperReqDTO paperDTO);

    /**
     * 修改考试状态
     *
     * @param paperDTO
     * @return
     */
    public int updatePaperStatus(PaperReqDTO paperDTO);

    /**
     * 批量删除试卷基础信息
     *
     * @param paperIds 需要删除的试卷基础信息ID
     * @return 结果
     */
    public int deletePaperByIds(Long[] paperIds);

    /**
     * 删除试卷基础信息信息
     *
     * @param paperId 试卷基础信息ID
     * @return 结果
     */
    public int deletePaperById(Long paperId);

    public List<Paper> getSelectedXtjByKc(Long kcid);
}
