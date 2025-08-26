package com.sinosoft.etp.cp.testparperresource.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.testparperresource.domain.TTestparperResourceWithCp;
import com.sinosoft.etp.cp.testparperresource.service.ITTestparperResourceWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试卷资源管理Controller
 *
 * @author wyx
 * @date 2022-02-24
 */
@RestController
@RequestMapping("/etc/testparperResourceWithCp")
public class TTestparperResourceWithCpController extends BaseController
{
    @Autowired
    private ITTestparperResourceWithCpService tTestparperResourceService;

    /**
     * 查询试卷资源管理列表
     */
    @PreAuthorize("@ss.hasPermi('testparperResource:testparperResource:list')")
    @GetMapping("/list")
    public TableDataInfo list(TTestparperResourceWithCp tTestparperResource)
    {
        startPage();
        List<TTestparperResourceWithCp> list = tTestparperResourceService.selectTTestparperResourceList(tTestparperResource);
        return getDataTable(list);
    }

    /**
     * 导出试卷资源管理列表
     */
    @PreAuthorize("@ss.hasPermi('testparperResource:testparperResource:export')")
    @Log(title = "试卷资源管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TTestparperResourceWithCp tTestparperResource)
    {
        List<TTestparperResourceWithCp> list = tTestparperResourceService.selectTTestparperResourceList(tTestparperResource);
        ExcelUtil<TTestparperResourceWithCp> util = new ExcelUtil<TTestparperResourceWithCp>(TTestparperResourceWithCp.class);
        return util.exportExcel(list, "试卷资源管理数据");
    }

    /**
     * 获取试卷资源管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('testparperResource:testparperResource:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tTestparperResourceService.selectTTestparperResourceById(id));
    }

    /**
     * 新增试卷资源管理
     */
    @PreAuthorize("@ss.hasPermi('testparperResource:testparperResource:add')")
    @Log(title = "试卷资源管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTestparperResourceWithCp tTestparperResource)
    {
        return toAjax(tTestparperResourceService.insertTTestparperResource(tTestparperResource));
    }

    /**
     * 修改试卷资源管理
     */
    @PreAuthorize("@ss.hasPermi('testparperResource:testparperResource:edit')")
    @Log(title = "试卷资源管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTestparperResourceWithCp tTestparperResource)
    {
        return toAjax(tTestparperResourceService.updateTTestparperResource(tTestparperResource));
    }

    /**
     * 删除试卷资源管理
     */
    @PreAuthorize("@ss.hasPermi('testparperResource:testparperResource:remove')")
    @Log(title = "试卷资源管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tTestparperResourceService.deleteTTestparperResourceByIds(ids));
    }
}
