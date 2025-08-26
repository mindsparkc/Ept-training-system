package com.sinosoft.etp.cp.examination.service;

import com.sinosoft.etp.cp.examination.domain.TExaminationStudentWithCp;

import java.util.List;

/**
 * @type create
 * @auther cfl
 * 2021-12-31
 */
public interface ITExaminationStudentWithCpService {
    /**
     * 批量插入考试与学员记录表记录
     * @param sysUsers  学员
     * @param ksid      考试ID
     * @return Long
     */
    Long insert(List<TExaminationStudentWithCp> sysUsers, Long ksid);
}
