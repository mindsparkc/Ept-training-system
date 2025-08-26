package com.sinosoft.etp.app.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.app.domain.TAppVersion;
import com.sinosoft.etp.app.service.ITAppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app版本管理Controller
 *
 *
 * @date 2022-07-06
 */
@RestController
@RequestMapping("/app/appVersion")
public class TAppVersionController extends BaseController
{
    @Autowired
    private ITAppVersionService tAppVersionService;

    /**
     * 查询app版本管理列表
     */
    @PreAuthorize("@ss.hasPermi('app:appVersion:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAppVersion tAppVersion)
    {
        startPage();
        List<TAppVersion> list = tAppVersionService.selectTAppVersionList(tAppVersion);
        return getDataTable(list, TAppVersion.class);
    }

    /**
     * 导出app版本管理列表
     */
    @PreAuthorize("@ss.hasPermi('app:appVersion:export')")
    @Log(title = "app版本管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TAppVersion tAppVersion)
    {
        List<TAppVersion> list = tAppVersionService.selectTAppVersionList(tAppVersion);
        ExcelUtil<TAppVersion> util = new ExcelUtil<TAppVersion>(TAppVersion.class);
        return util.exportExcel(list, "app版本管理数据");
    }

    /**
     * 获取app版本管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('app:appVersion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tAppVersionService.selectTAppVersionById(id));
    }


    /**
     * 获取app最新版本信息
     */
    @Log(title = "app版本管理", businessType = BusinessType.EXPORT)
    @GetMapping(value = "/getNewApp/{appCode}&{platform}")
    public AjaxResult getNewApp(@PathVariable("appCode") String appCode,@PathVariable("platform")String platform)
    {
        return AjaxResult.success(tAppVersionService.selectTAppVersionByNow(appCode,platform));
    }

    /**
     * 获取app最新版本信息
     */
    @Log(title = "app版本管理", businessType = BusinessType.EXPORT)
    @GetMapping(value = "/getAppByStudent")
    public AjaxResult getAppByStudent(String appCode,String platform)
    {
        return AjaxResult.success(tAppVersionService.selectTAppVersionByNow(appCode,platform));
    }

    /**
     * 新增app版本管理
     */
    @PreAuthorize("@ss.hasPermi('app:appVersion:add')")
    @Log(title = "app版本管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAppVersion tAppVersion)
    {
        return toAjax(tAppVersionService.insertTAppVersion(tAppVersion));
    }

    /**
     * 修改app版本管理
     */
    @PreAuthorize("@ss.hasPermi('app:appVersion:edit')")
    @Log(title = "app版本管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAppVersion tAppVersion)
    {
        return toAjax(tAppVersionService.updateTAppVersion(tAppVersion));
    }

    /**
     * 删除app版本管理
     */
    @PreAuthorize("@ss.hasPermi('app:appVersion:remove')")
    @Log(title = "app版本管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tAppVersionService.deleteTAppVersionByIds(ids));
    }
}
