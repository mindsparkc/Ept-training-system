package com.sinosoft.etp.cp.examination.mapper;

import com.sinosoft.etp.cp.examination.domain.TExaminationInfoWithCp;
import com.sinosoft.etp.cp.examination.domain.TExaminationStudentWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @type create
 * @auther cfl
 * 2021-12-31
 * Mapper
 */
public interface TExaminationStudentWithCpMapper {

    /**
     * 插入考试与学员关联关系
     * @param sysUsers 学员
     */
    Long insert(@Param("list") List<TExaminationStudentWithCp> sysUsers);

    /**
     * 根据考试ID查询用户
     * @param ksid 考试ID
     * @return  List<TExaminationStudent>
     */
    List<TExaminationStudentWithCp> selectByKsId(@Param("ksid") Long ksid);

    /**
     * 根据考试ID查询缺考用户
     * @param ksid 考试ID
     * @return  List<TExaminationStudent>
     */
    List<TExaminationStudentWithCp> selectMissExamByKsId(@Param("ksid") Long ksid,@Param("deptId")Long deptId);

    /**
     * 根据考试ID和判卷状态查询用户
     * @param ksid 考试ID
     * @param pjzt 判卷状态
     * @return  List<TExaminationStudent>
     */
    List<TExaminationInfoWithCp> selectInfo(@Param("ksid") Long ksid, @Param("pjzt") String pjzt);

    /**
     * 根据考试id和部门id导出用户考试成绩信息
     * @param ksid 考试id
     * @param deptId 部门id
     * @return List<TExaminationStudent>
     */
    List<TExaminationInfoWithCp> selectExamInfo(@Param("ksid") Long ksid,@Param("deptId")Long deptId);

    /**
     * 根据课程ID删除任务与学员记录表记录
     * @param ksid 考试ID
     */
    void delRealtionByKsId(@Param("ksid") Long ksid);

    Integer selectKsCount(@Param("ksid")Long ksid);
}
