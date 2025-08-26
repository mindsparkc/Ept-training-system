package com.sinosoft.etp.up.errorbook.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.up.errorbook.domain.TErrorbook;
import com.sinosoft.etp.up.errorbook.service.ITErrorbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 错题集Controller
 *
 *
 * @date 2022-05-11
 */
@RestController
@RequestMapping("/etp/errorbook")
public class TErrorbookController extends BaseController
{
    @Autowired
    private ITErrorbookService tErrorbookService;

    /**
     * 查询错题集列表
     */
    @PreAuthorize("@ss.hasPermi('etp:errorbook:list')")
    @GetMapping("/list")
    public TableDataInfo list(TErrorbook tErrorbook)
    {
        startPage();
        List<TErrorbook> list = tErrorbookService.selectTErrorbookList(tErrorbook);
        return getDataTable(list);
    }

    /**
     * 导出错题集列表
     */
    @PreAuthorize("@ss.hasPermi('etp:errorbook:export')")
    @Log(title = "错题集", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TErrorbook tErrorbook)
    {
        List<TErrorbook> list = tErrorbookService.selectTErrorbookList(tErrorbook);
        ExcelUtil<TErrorbook> util = new ExcelUtil<TErrorbook>(TErrorbook.class);
        return util.exportExcel(list, "错题集数据");
    }

    /**
     * 获取错题集详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:errorbook:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tErrorbookService.selectTErrorbookById(id));
    }

    /**
     * 新增错题集
     */
    @PreAuthorize("@ss.hasPermi('etp:errorbook:add')")
    @Log(title = "错题集", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TErrorbook tErrorbook)
    {
        return toAjax(tErrorbookService.insertTErrorbook(tErrorbook));
    }

    /**
     * 修改错题集
     */
    @PreAuthorize("@ss.hasPermi('etp:errorbook:edit')")
    @Log(title = "错题集", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TErrorbook tErrorbook)
    {
        return toAjax(tErrorbookService.updateTErrorbook(tErrorbook));
    }

    /**
     * 删除错题集
     */
    @PreAuthorize("@ss.hasPermi('etp:errorbook:remove')")
    @Log(title = "错题集", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(tErrorbookService.deleteTErrorbookById(id));
    }
}
