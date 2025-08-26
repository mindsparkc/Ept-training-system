package com.sinosoft.etp.cp.task.mapper;

import com.sinosoft.etp.cp.task.domain.TTaskStudentWithCp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TTaskStudentWithCpMapper {

    /**
     * 插入任务与学员关联关系
     * @param sysUsers 用户
     */
    Long insert(@Param("list") List<TTaskStudentWithCp> sysUsers);


    /**
     * 根据任务ID查询用户
     * @param rwid 任务ID
     * @return  List<TTaskStudent>
     */
    List<TTaskStudentWithCp> selectByKcId(@Param("rwid") Long rwid);

    /**
     * 根据任务ID删除任务与学员记录表记录
     * @param rwid 任务ID
     */
    void delRealtionByKcId(@Param("rwid") Long rwid);

    List<TTaskStudentWithCp> selectStudentByrwIdAndXyId(@Param("rwid") Long rwid, @Param("xyid") Long xyid);

}
