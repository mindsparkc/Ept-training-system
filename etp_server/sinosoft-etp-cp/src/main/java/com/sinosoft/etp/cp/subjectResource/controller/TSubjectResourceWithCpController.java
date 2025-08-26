package com.sinosoft.etp.cp.subjectResource.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.subjectResource.domain.TSubjectResourceWithCp;
import com.sinosoft.etp.cp.subjectResource.service.ITSubjectResourceWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题库资源管理Controller
 *
 * @author wyx
 * @date 2022-02-24
 */
@RestController
@RequestMapping("/etc/subjectResourceWithCp")
public class TSubjectResourceWithCpController extends BaseController
{
    @Autowired
    private ITSubjectResourceWithCpService tSubjectResourceService;

    /**
     * 查询题库资源管理列表
     */
    @PreAuthorize("@ss.hasPermi('subjectResource:subjectResource:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSubjectResourceWithCp tSubjectResource)
    {
        startPage();
        List<TSubjectResourceWithCp> list = tSubjectResourceService.selectTSubjectResourceList(tSubjectResource);
        return getDataTable(list);
    }

    /**
     * 导出题库资源管理列表
     */
    @PreAuthorize("@ss.hasPermi('subjectResource:subjectResource:export')")
    @Log(title = "题库资源管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TSubjectResourceWithCp tSubjectResource)
    {
        List<TSubjectResourceWithCp> list = tSubjectResourceService.selectTSubjectResourceList(tSubjectResource);
        ExcelUtil<TSubjectResourceWithCp> util = new ExcelUtil<TSubjectResourceWithCp>(TSubjectResourceWithCp.class);
        return util.exportExcel(list, "题库资源管理数据");
    }

    /**
     * 获取题库资源管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('subjectResource:subjectResource:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tSubjectResourceService.selectTSubjectResourceById(id));
    }

    /**
     * 新增题库资源管理
     */
    @PreAuthorize("@ss.hasPermi('subjectResource:subjectResource:add')")
    @Log(title = "题库资源管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSubjectResourceWithCp tSubjectResource)
    {
        return toAjax(tSubjectResourceService.insertTSubjectResource(tSubjectResource));
    }

    /**
     * 修改题库资源管理
     */
    @PreAuthorize("@ss.hasPermi('subjectResource:subjectResource:edit')")
    @Log(title = "题库资源管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSubjectResourceWithCp tSubjectResource)
    {
        return toAjax(tSubjectResourceService.updateTSubjectResource(tSubjectResource));
    }

    /**
     * 删除题库资源管理
     */
    @PreAuthorize("@ss.hasPermi('subjectResource:subjectResource:remove')")
    @Log(title = "题库资源管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tSubjectResourceService.deleteTSubjectResourceByIds(ids));
    }
}
