package com.sinosoft.etp.common.progress.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 试卷学员关联Mapper接口
 *
 *
 * @date 2022-02-28
 */
public interface ProgressMapper
{
    /**
     * 获取课程下 课件的总进度
     * @param xyid
     * @param ids
     * @return
     */
    public Map<String,Object> getRateByXyid(@Param("xyid") Long xyid , @Param("ids") String[] ids);

    /**
     * 获取任务的进度
     * @param rwid
     * @return
     */
    public Map<String,Object> getTaskProgress(@Param("rwid") Long rwid,@Param("xyid") String xyid);

    /**
     * 获取课程的进度
     * @param kcid
     * @return
     */
    public Map<String,Object> getKcProgress(@Param("kcid") Long kcid,@Param("xyid") String xyid);
    /**
     * 获取包含课件的课程
     * @param xyid
     * @param kjid
     * @return
     */
    public List<Map<String,Object>> getKcContainsKj(@Param("xyid") String xyid,@Param("kjid") String kjid);

    /**
     * 获取包含某个课件的任务
     * @param xyid
     * @param kjid
     * @return
     */
    public List<Map<String,Object>> getTaskContainsKj(@Param("xyid") String xyid,@Param("kjid") String kjid);

    public int updateKcProgress( @Param("rate")Double rate,@Param("time")Long time,@Param("completed")Integer completed,
                                 @Param("kcid")Long kcid, @Param("xyid")String xyid);

    public int updateTaskProgress(@Param("rate")Double rate,@Param("time")Long time, @Param("completed")Integer completed,
                                  @Param("rwid")Long rwid, @Param("xyid")String xyid);

}
