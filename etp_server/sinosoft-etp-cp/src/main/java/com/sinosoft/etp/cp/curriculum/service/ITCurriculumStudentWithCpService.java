package com.sinosoft.etp.cp.curriculum.service;

import com.sinosoft.etp.cp.curriculum.domain.TCurriculumStudentWithCp;

import java.util.List;

/**
 * 课程与学员关联接口
 */
public interface ITCurriculumStudentWithCpService {

    /**
     * 批量插入课程与学员记录表记录
     * @param sysUsers  学员
     * @param kcid      课程ID
     * @return Long
     */
    Long insert(List<TCurriculumStudentWithCp> sysUsers, Long kcid);
}
