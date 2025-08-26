package com.sinosoft.user.curriculum.mapper;

import com.sinosoft.user.curriculum.domain.CurriculumTrainStudent;

public interface CurriculumTrainStudentMapper {

    /**
     * 查查询用户课程练习集信息
     *
     * @param curriculumTrainStudent
     * @return
     */
    public CurriculumTrainStudent selectCTrainInfo(CurriculumTrainStudent curriculumTrainStudent);

    public int insertCTrainInfo(CurriculumTrainStudent curriculumTrainStudent);

    public int deleteCTrainInfo(CurriculumTrainStudent curriculumTrainStudent);
}
