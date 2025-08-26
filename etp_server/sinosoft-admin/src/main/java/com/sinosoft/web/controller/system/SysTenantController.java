package com.sinosoft.web.controller.system;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.domain.SysNotice;
import com.sinosoft.system.domain.SysTenant;
import com.sinosoft.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户、注册信息操作处理
 *
 *
 */
@RestController
@RequestMapping("/system/tenant")
public class SysTenantController extends BaseController
{
    @Autowired
    private ISysTenantService tenantService;

    /**
     * 获取列表
     */
//    @PreAuthorize("@ss.hasPermi('system:tenant:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTenant tenant)
    {
        startPage();
        List<SysTenant> list = tenantService.selectTenantList(tenant);
        return getDataTable(list, SysTenant.class);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:query')")
    @GetMapping(value = "/{tenantId}")
    public AjaxResult getInfo(@PathVariable Long tenantId)
    {
        return AjaxResult.success(tenantService.selectTenantById(tenantId));
    }

    /**
     * 新增 (这个不用，而是采用注册方式)
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:add')")
    @Log(title = "租户信息新增", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysTenant tenant)
    {
        tenant.setCreateBy(SecurityUtils.getUsername());
        return toAjax(tenantService.insertTenant(tenant));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:edit')")
    @Log(title = "租户信息修改", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysTenant tenant)
    {
        tenant.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(tenantService.updateTenant(tenant));
    }

    /**
     * 添加客户
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:add')")
    @Log(title = "添加客户", businessType = BusinessType.INSERT)
    @PostMapping("/addCustomer")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addCustomer(@Validated @RequestBody SysTenant sysTenant)
    {
        return toAjax(this.tenantService.addCustomer(sysTenant));
    }

    /**
     * 修改客户
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:update')")
    @Log(title = "修改客户", businessType = BusinessType.UPDATE)
    @PostMapping("/updateCustomer")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateCustomer(@Validated @RequestBody SysTenant sysTenant)
    {
        return toAjax(this.tenantService.updateCustomer(sysTenant));
    }

    /**
     * 删除客户
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:remove')")
    @Log(title = "删除客户", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteCustomer/{tenantId}")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteCustomer(@PathVariable Long tenantId)
    {
        return toAjax(this.tenantService.deleteCustomer(tenantId));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:remove')")
    @Log(title = "租户信息删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tenantService.deleteTenantByIds(ids));
    }
}
