package com.sinosoft.etp.up.task.mapper;

import com.sinosoft.etp.up.task.domain.TTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 任务Mapper接口
 *
 *
 * @date 2021-12-08
 */
public interface TTaskMapper {

    /**
     * 查询任务列表
     *
     * @param tTask 任务
     * @return 任务集合
     */
    public List<TTask> selectTTaskList(TTask tTask);

    /**
     * 查询任务
     *
     * @param id 任务ID
     * @return 任务
     */
    public TTask selectTTaskById(@Param("id") Long id,@Param("xyid") Long xyid);

    public int getTaskNumByXyid(Long xyid);

    /**
     * 首页获取两个任务
     * @param xyid
     * @return
     */
    public List<Map<String,Object>> getTwoTask(@Param("xyid") Long xyid,@Param("num") int num);


    /**
     * 查询任务标签
     * @param qyid
     * @return
     */
    public List<Map<String,Object>> getTaskTags();


    /**
     * 获取未完成的任务个数
     * @param xyid
     * @return
     */
    public int getRwNotCompletedNum(@Param("xyid") Long xyid);
    /**
     * 获取任务总个数
     * @param xyid
     * @return
     */
    public int getRwNum(@Param("xyid") Long xyid);
    /**
     * 将过期任务总个数
     * @param xyid
     * @return
     */
    public int getRwJgqNum(@Param("xyid") Long xyid);

}
