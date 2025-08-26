package com.sinosoft.web.controller.system;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.system.domain.SysTenantVip;
import com.sinosoft.system.service.ISysTenantVipService;
    import com.sinosoft.common.core.page.TableDataInfo;

import javax.annotation.Resource;

/**
 * 租户vip资源控制Controller
 *
 * @author sinosoft
 * @date 2025-05-29
 */
@RestController
@RequestMapping("/system/vip")
public class SysTenantVipController extends BaseController
{
    @Resource
    private ISysTenantVipService sysTenantVipService;

    /**
     * 查询租户vip资源控制列表
     */
    @PreAuthorize("@ss.hasPermi('system:vip:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTenantVip sysTenantVip)
    {
        startPage();
        List<SysTenantVip> list = sysTenantVipService.selectSysTenantVipList(sysTenantVip);
        return getDataTable(list);
    }

    /**
     * 获取租户vip资源控制详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:vip:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysTenantVipService.selectSysTenantVipById(id));
    }

    /**
     * 新增租户vip资源控制
     */
    @PreAuthorize("@ss.hasPermi('system:vip:add')")
    @Log(title = "vip等级管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysTenantVip sysTenantVip)
    {
        return toAjax(sysTenantVipService.insertSysTenantVip(sysTenantVip));
    }

    /**
     * 修改租户vip资源控制
     */
    @PreAuthorize("@ss.hasPermi('system:vip:edit')")
    @Log(title = "vip等级管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTenantVip sysTenantVip)
    {
        return toAjax(sysTenantVipService.updateSysTenantVip(sysTenantVip));
    }

    /**
     * 删除租户vip资源控制
     */
    @PreAuthorize("@ss.hasPermi('system:vip:remove')")
    @Log(title = "vip等级管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysTenantVipService.deleteSysTenantVipByIds(ids));
    }
}
