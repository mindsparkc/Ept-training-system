package com.sinosoft.etp.up.examination.service;

import com.sinosoft.etp.up.examination.domain.TExamination;

import java.util.List;

/**
 * 考试Service接口
 *
 * 
 * @date 2021-12-24
 */
public interface ITExaminationService
{
    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    public TExamination selectTExaminationById(Long id);

    /**
     * 查询考试列表
     *
     * @param tExamination 考试
     * @return 考试集合
     */
    public List<TExamination> selectTExaminationList(TExamination tExamination);


    public Integer getExaminationNum(Long xyid);

    /** 获取考试历史 */
    public TExamination getTExaminationById(Long id);

}
