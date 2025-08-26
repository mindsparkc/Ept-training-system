package com.sinosoft.etp.cp.analysis.service;

import com.sinosoft.etp.cp.analysis.domain.DeptAndExaminationWithCp;
import com.sinosoft.etp.cp.analysis.domain.SubjectAndExaminationWithCp;
import com.sinosoft.etp.cp.analysis.domain.TExaminationScoreAnalysisWithCp;

import java.util.List;

/**
 * 考试Service接口
 *
 * 
 * @date 2021-12-24
 */
public interface ITExaminationScoreAnaltsisService
{
    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    public TExaminationScoreAnalysisWithCp selectTExaminationById(Long id);

    /**
     * 查询考试列表
     *
     * @param tExamination 考试
     * @return 考试集合
     */
    public List<TExaminationScoreAnalysisWithCp> selectTExaminationList(TExaminationScoreAnalysisWithCp tExamination);

    /**
     * 查询考试部门关联列表
     *
     * @param deptAndExaminationWithCp 考试部门
     * @return 考试部门集合
     */
    public List<DeptAndExaminationWithCp> selectTExaminationByDeptList(DeptAndExaminationWithCp deptAndExaminationWithCp);

    /**
     * 根据试卷id获取试题数据
     * @param sjid 试卷id
     * @return
     */
    public List<SubjectAndExaminationWithCp> getSubjectInfo(Long sjid);

    /**
     * 根据题目id 和考试id 获取题目分析信息
     * @param id 题目id
     * @param ksid 考试id
     * @param lx 题目类型
     * @return
     */
    public SubjectAndExaminationWithCp getSubjectAnalysis(Long id,Long ksid,String lx);
    /**
     * 新增考试
     *
     * @param tExamination 考试
     * @return 结果
     */
    /*public int insertTExamination(TExamination tExamination);*/

    /**
     * 修改考试
     *
     * @param tExamination 考试
     * @return 结果
     */
    /*public int updateTExamination(TExamination tExamination);*/

   /* public int updateTExaminationFb(TExamination tExamination);*/
    /**
     * 批量删除考试
     *
     * @param ids 需要删除的考试ID
     * @return 结果
     */
   /* public int deleteTExaminationByIds(Long[] ids);*/

    /**
     * 删除考试信息
     *
     * @param id 考试ID
     * @return 结果
     */
   /* public int deleteTExaminationById(Long id);*/
}
