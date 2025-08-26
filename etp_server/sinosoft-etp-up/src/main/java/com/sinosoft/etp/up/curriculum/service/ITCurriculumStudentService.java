package com.sinosoft.etp.up.curriculum.service;

import com.sinosoft.etp.up.curriculum.domain.TCurriculumStudent;

import java.util.List;

/**
 * 课程与学员关联接口
 */
public interface ITCurriculumStudentService {

    /**
     * 批量插入课程与学员记录表记录
     * @param tCurriculumStudent
     * @return Long
     */
    public int insertTCurriculumStudent(TCurriculumStudent tCurriculumStudent);
    public int countJoinCurriculumById(Long id);

    /**
     * 获取包含cid的任务
     * @param cid
     * @return
     */
    public List<Long> getTaskContainCurriculum(Long cid);
}
