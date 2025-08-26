package com.sinosoft.etp.up.task.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.etp.up.task.domain.TTask;
import com.sinosoft.etp.up.task.domain.TTaskCatalog;
import com.sinosoft.etp.up.task.service.ITTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务Controller
 *
 *
 * @date 2021-12-08
 */
@RestController
@RequestMapping("/etp/task")
public class TTaskController extends BaseController
{
    @Autowired
    private ITTaskService tTaskService;

    /**
     * 查询任务列表
     */
    @PreAuthorize("@ss.hasPermi('etp:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(TTask tTask)
    {
        String tags = tTask.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tTask.getParams().put("tags", tags.split(","));
            tTask.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TTask> list = tTaskService.selectTTaskList(tTask);
        return getDataTable(list, TTask.class);
    }

    /**
     * 获取任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TTaskCatalog>(TTaskCatalog.class);
        return AjaxResult.success(responseFieldUtil.response(tTaskService.selectTTaskById(id)));
    }
    @PreAuthorize("@ss.hasPermi('etp:task:query')")
    @GetMapping(value = "/task/{id}")
    public AjaxResult getTaskInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TTask>(TTask.class);
        return AjaxResult.success(responseFieldUtil.response(tTaskService.selectTTaskInfoById(id)));
    }

    /**
     * 更新任务进度
     * @param params
     * @return
     */
    @PostMapping("/updateTaskStatus")
    public  AjaxResult updateTaskStatus(@RequestBody Map<String,Object> params){
        Long rwid = Long.parseLong(params.get("rwid").toString());
        Long kjid = Long.parseLong(params.get("kjid").toString());
        return AjaxResult.success(tTaskService.updateTaskStatus(rwid,kjid));
    }


    @GetMapping("getTaskTags")
    public AjaxResult getTaskTags(){
        List<Map<String,Object>>list = tTaskService.getTaskTags();
        return AjaxResult.success(list);
    }

}
