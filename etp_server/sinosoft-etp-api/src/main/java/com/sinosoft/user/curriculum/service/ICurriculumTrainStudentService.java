package com.sinosoft.user.curriculum.service;

import com.sinosoft.core.domain.PaperUnitItem;
import com.sinosoft.user.curriculum.domain.CurriculumTrainStudent;
import com.sinosoft.user.exam.domain.*;

import java.util.List;

/**
 * 学员用户查询课程习题集信息 Service层接口
 *
 */
public interface ICurriculumTrainStudentService {
    /**
     * 查查询用户课程练习集信息
     *
     * @param curriculumTrainStudent
     * @return
     */
    public CurriculumTrainStudent selectCTrainInfo(CurriculumTrainStudent curriculumTrainStudent);
    /**
     * 新增用户课程练习集信息
     *
     * @param curriculumTrainStudent
     * @return
     */
    public int insertCTrainInfo(CurriculumTrainStudent curriculumTrainStudent);
    /**
     * 删除用户课程练习集信息
     *
     * @param curriculumTrainStudent
     * @return
     */
    public int deleteCTrainInfo(CurriculumTrainStudent curriculumTrainStudent);
}
