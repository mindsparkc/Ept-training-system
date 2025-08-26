package com.sinosoft.etp.cp.task.service;

import com.sinosoft.etp.cp.task.domain.TTaskStudentWithCp;

import java.util.List;

/**
 * 任务与学员关联接口
 */
public interface ITaskStudentWithCpService {

    /**
     * 批量插入任务与学员记录表记录
     * @param sysUsers  学员
     * @param rwid      任务ID
     * @return Long
     */
    Long insert(List<TTaskStudentWithCp> sysUsers, Long rwid);
}
