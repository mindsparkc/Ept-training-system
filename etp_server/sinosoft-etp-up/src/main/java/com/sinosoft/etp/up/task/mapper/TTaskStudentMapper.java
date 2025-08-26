package com.sinosoft.etp.up.task.mapper;


import com.sinosoft.etp.up.task.domain.TTaskStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TTaskStudentMapper {

    /**
     * 修改
     *
     * @param tTaskStudent
     * @return 结果
     */
    public int updateTTaskStudent(TTaskStudent tTaskStudent);

    public int updateTaskProgress(@Param("completed")Integer completed,
                                  @Param("rate")Double rate,@Param("rwid")Long rwid,
                                  @Param("xyid")Long xyid);

    public Long getTaskCurrentId (@Param("rwid")Long rwid,@Param("xyid")Long xyid,@Param("kjids")List<Long> kjids);

    public int updateTaskJd(@Param("xyid") Long xyid,@Param("rwid") Long rwid,@Param("current") Long current);
}
