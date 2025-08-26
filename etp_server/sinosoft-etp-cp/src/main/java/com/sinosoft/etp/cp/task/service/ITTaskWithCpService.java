package com.sinosoft.etp.cp.task.service;

import com.sinosoft.etp.cp.task.domain.TTaskWithCp;

import java.util.List;

/**
 * 任务Service接口
 *
 * 
 * @date 2021-12-08
 */
public interface ITTaskWithCpService
{
    /**
     * 查询任务
     *
     * @param id 任务ID
     * @return 任务
     */
    public TTaskWithCp selectTTaskById(Long id);

    /**
     * 查询任务列表
     *
     * @param tTask 任务
     * @return 任务集合
     */
    public List<TTaskWithCp> selectTTaskList(TTaskWithCp tTask);

    /**
     * 新增任务
     *
     * @param tTask 任务
     * @return 结果
     */
    public Long insertTTask(TTaskWithCp tTask);

    /**
     * 修改任务
     *
     * @param tTask 任务
     * @return 结果
     */
    public int updateTTask(TTaskWithCp tTask);

    /**
     * 批量删除任务
     *
     * @param ids 需要删除的任务ID
     * @return 结果
     */
    public int deleteTTaskByIds(Long[] ids);

    /**
     * 删除任务信息
     *
     * @param id 任务ID
     * @return 结果
     */
    public int deleteTTaskById(Long id);

    Long completeTask(TTaskWithCp tTask);

    void pushBack(Long id);

    public boolean checkExistRW(TTaskWithCp task);
}
