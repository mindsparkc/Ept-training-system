package com.sinosoft.etp.app.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.app.domain.TApp;
import com.sinosoft.etp.app.service.ITAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app管理Controller
 *
 * 
 * @date 2022-07-06
 */
@RestController
@RequestMapping("/app/app")
public class TAppController extends BaseController
{
    @Autowired
    private ITAppService tAppService;

    /**
     * 查询app管理列表
     */
    @PreAuthorize("@ss.hasPermi('app:app:list')")
    @GetMapping("/list")
    public TableDataInfo list(TApp tApp)
    {
        startPage();
        List<TApp> list = tAppService.selectTAppList(tApp);
        return getDataTable(list);
    }

    /**
     * 查询APP标识是否存在
     * @param appCode
     * @return
     */
    @GetMapping("/selectAppCode/{appCode}")
    public AjaxResult selectAppName(@PathVariable("appCode") String appCode)
    {
        return AjaxResult.success(tAppService.selectAppCode(appCode));
    }

    /**
     * 判断app版本号是否存在
     * @param versionCode
     * @return
     */
    @GetMapping("/selectVersionCode/{id}&{versionCode}")
    public AjaxResult selectAppName(@PathVariable("id") Long id, @PathVariable("versionCode") Long versionCode)
    {
        return AjaxResult.success(tAppService.selectVersionCode(id,versionCode));
    }

    /**
     * 导出app管理列表
     */
    @PreAuthorize("@ss.hasPermi('app:app:export')")
    @Log(title = "app管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TApp tApp)
    {
        List<TApp> list = tAppService.selectTAppList(tApp);
        ExcelUtil<TApp> util = new ExcelUtil<TApp>(TApp.class);
        return util.exportExcel(list, "app管理数据");
    }

    /**
     * 获取app管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('app:app:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tAppService.selectTAppById(id));
    }

    /**
     * 新增app管理
     */
    @PreAuthorize("@ss.hasPermi('app:app:add')")
    @Log(title = "app管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TApp tApp)
    {
        return toAjax(tAppService.insertTApp(tApp));
    }

    /**
     * 修改app管理
     */
    @PreAuthorize("@ss.hasPermi('app:app:edit')")
    @Log(title = "app管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TApp tApp)
    {
        return toAjax(tAppService.updateTApp(tApp));
    }

    /**
     * 删除app管理
     */
    @PreAuthorize("@ss.hasPermi('app:app:remove')")
    @Log(title = "app管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tAppService.deleteTAppByIds(ids));
    }
}
