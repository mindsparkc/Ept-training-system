package com.sinosoft.etp.cp.analysis.mapper;

import com.sinosoft.etp.cp.analysis.domain.DeptAndExaminationWithCp;
import com.sinosoft.etp.cp.analysis.domain.SubjectAndExaminationWithCp;
import com.sinosoft.etp.cp.analysis.domain.TExaminationScoreAnalysisWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试Mapper接口
 *
 *
 * @date 2021-12-24
 */
public interface TExaminationScoreAnalysisWithCpMapper
{
    /**
     * 查询考试
     *
     * @param id 考试ID
     * @return 考试
     */
    public TExaminationScoreAnalysisWithCp selectTExaminationScoreAnalysisById(Long id);

    /**
     * 查询考试列表
     *
     * @param tExaminationScoreAnalysis 考试
     * @return 考试集合
     */
    public List<TExaminationScoreAnalysisWithCp> selectTExaminationScoreAnalysisList(TExaminationScoreAnalysisWithCp tExaminationScoreAnalysis);

    /**
     * 查询应考人数
     * @param id 考试id
     */
    public Integer selectExamNum(@Param("id")Long id,@Param("deptId")Long deptId);

    /**
     * 查询考试部门关联列表
     *
     * @param  deptAndExaminationWithCp 考试部门关联列表
     * @return 考试集合
     */
    public List<DeptAndExaminationWithCp> selectDeptAndExaminationWithCpList(DeptAndExaminationWithCp deptAndExaminationWithCp);

    /**
     * 查询应考人数
     * @param id 考试id
     */
    public Integer selectPracticeNum(@Param("id")Long id,@Param("deptId")Long deptId);

    //public List<SubjectAndExaminationWithCp> getSubjectInfo(@Param("id")Long id);

    /**
     * 根据试卷id获取试卷题号
     * @param sjid
     * @return
     */
    public List<Long> getTkid(@Param("sjid")Long sjid);

    /**
     * 根据题库id获取题目信息
     * @param tkid
     * @return
     */
    public SubjectAndExaminationWithCp getSubjectInfo(@Param("tkid")Long tkid);

    /**
     * 根据题库id和考试id获取用户提交答题信息
     * @param daPath,ksid
     * @return
     */
    public List<String> getYhda(@Param("daPath")String daPath,@Param("ksid")Long ksid);
}
