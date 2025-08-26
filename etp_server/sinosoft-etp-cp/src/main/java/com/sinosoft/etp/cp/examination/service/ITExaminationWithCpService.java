package com.sinosoft.etp.cp.examination.service;

import com.sinosoft.etp.cp.examination.domain.TExaminationInfoWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationWithCp;

import java.util.List;

/**
 * 考试Service接口
 *
 *
 * @date 2021-12-24
 */
public interface ITExaminationWithCpService
{
    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    public TExaminationWithCp selectTExaminationById(Long id);

    /**
     * 查询缺考人员
     *
     * @param id 考试ID
     * @return 考试
     */
    public TExaminationWithCp selectMissExamById(Long id);

    /**
     * 根据判卷状态查询考试
     *
     * @param id 考试ID,pjzt 判卷状态
     * @return 考试
     */
    public TExaminationWithCp selectTExaminationInfo(Long id,String pjzt);

    /**
     * 根据考试id获取学员成绩信息
     * @param ksid
     * @return
     */
    public List<TExaminationInfoWithCp> selectTExaminationList(Long ksid);

    /**
     * 查询考试列表
     *
     * @param tExamination 考试
     * @return 考试集合
     */
    public List<TExaminationWithCp> selectTExaminationList(TExaminationWithCp tExamination);

    /**
     * 新增考试
     *
     * @param tExamination 考试
     * @return 结果
     */
    public Long insertTExamination(TExaminationWithCp tExamination);

    /**
     * 修改考试
     *
     * @param tExamination 考试
     * @return 结果
     */
    public int updateTExamination(TExaminationWithCp tExamination);

    public int updateTExaminationFb(TExaminationWithCp tExamination);
    /**
     * 批量删除考试
     *
     * @param ids 需要删除的考试ID
     * @return 结果
     */
    public int deleteTExaminationByIds(Long[] ids);

    /**
     * 删除考试信息
     *
     * @param id 考试ID
     * @return 结果
     */
    public int deleteTExaminationById(Long id);


    public List<Long> checkExaminations(Long[] ids);

    public boolean checkExistKS(TExaminationWithCp tExamination);

}
