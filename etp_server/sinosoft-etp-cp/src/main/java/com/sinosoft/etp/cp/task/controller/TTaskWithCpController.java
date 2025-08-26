package com.sinosoft.etp.cp.task.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.task.domain.TTaskWithCp;
import com.sinosoft.etp.cp.task.service.ITTaskWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务Controller
 *
 *
 * @date 2021-12-08
 */
@RestController
@RequestMapping("/etp/taskWithCp")
public class TTaskWithCpController extends BaseController
{
    @Autowired
    private ITTaskWithCpService tTaskService;

    /**
     * 查询任务列表
     */
    @PreAuthorize("@ss.hasPermi('etp:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(TTaskWithCp tTask)
    {
        String tags = tTask.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tTask.getParams().put("tags", tags.split(","));
            tTask.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TTaskWithCp> list = tTaskService.selectTTaskList(tTask);
        return getDataTable(list, TTaskWithCp.class);
    }

    /**
     * 导出任务列表
     */
    @PreAuthorize("@ss.hasPermi('etp:task:export')")
    @Log(title = "任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TTaskWithCp tTask)
    {
        List<TTaskWithCp> list = tTaskService.selectTTaskList(tTask);
        ExcelUtil<TTaskWithCp> util = new ExcelUtil<TTaskWithCp>(TTaskWithCp.class);
        return util.exportExcel(list, "任务数据");
    }

    /**
     * 获取任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        TTaskWithCp tTaskWithCp = tTaskService.selectTTaskById(id);
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TTaskWithCp>(TTaskWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tTaskWithCp));
    }

    /**
     * 新增任务
     */
    @PreAuthorize("@ss.hasPermi('etp:task:add')")
    @Log(title = "任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTaskWithCp tTask)
    {
        if(tTaskService.checkExistRW(tTask))
        {
            return AjaxResult.error("已存在名称为【"+tTask.getMc()+"】的任务,不允许保存");
        }
        return AjaxResult.success(tTaskService.insertTTask(tTask));
    }

    /**
     * 新增任务,第二步提交
     */
    @PreAuthorize("@ss.hasPermi('etp:task:add')")
    @Log(title = "分步骤完善任务", businessType = BusinessType.INSERT)
    @PostMapping("/completeTask")
    public AjaxResult completeTask(@RequestBody TTaskWithCp tTask)
    {
        Long id = tTaskService.completeTask(tTask);
        return toAjaxHavaId(id);
    }

    /**
     * 任务取消发布
     */
    @GetMapping("/back/{id}")
    public AjaxResult pushBack(@PathVariable("id") Long id){
        tTaskService.pushBack(id);
        return AjaxResult.success();
    }



    /**
     * 修改任务
     */
    @PreAuthorize("@ss.hasPermi('etp:task:edit')")
    @Log(title = "任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTaskWithCp tTask)
    {
        if(tTaskService.checkExistRW(tTask))
        {
            return AjaxResult.error("已存在名称为【"+tTask.getMc()+"】的任务,不允许保存");
        }
        return toAjax(tTaskService.updateTTask(tTask));
    }

    /**
     * 删除任务
     */
    @PreAuthorize("@ss.hasPermi('etp:task:remove')")
    @Log(title = "任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tTaskService.deleteTTaskByIds(ids));
    }

    @GetMapping("/checkExistRW")
    public AjaxResult checkExistRW(TTaskWithCp tTask){
        boolean flag = tTaskService.checkExistRW(tTask);
        return AjaxResult.success(flag);
    }
}
