package com.sinosoft.etp.up.task.service;

import com.sinosoft.etp.up.task.domain.TTask;
import com.sinosoft.etp.up.task.domain.TTaskCatalog;

import java.util.List;
import java.util.Map;

/**
 * 任务Service接口
 *
 *
 * @date 2021-12-08
 */
public interface ITTaskService
{


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
    public TTaskCatalog selectTTaskById(Long id);
    public TTask selectTTaskInfoById(Long id);

    public int getTaskNumByXyid(Long xyid);

    public Double updateTaskStatus(Long rwid,Long kjid);


    List<Map<String,Object>> getTaskTags();
}
