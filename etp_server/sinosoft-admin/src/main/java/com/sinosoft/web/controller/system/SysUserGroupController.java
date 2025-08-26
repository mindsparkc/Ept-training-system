package com.sinosoft.web.controller.system;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.system.domain.SysUserGroup;
import com.sinosoft.system.service.ISysUserGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户组Controller
 *
 *
 * @date 2022-02-08
 */
@RestController
@RequestMapping("/system/group")
public class SysUserGroupController extends BaseController
{
    @Resource
    private ISysUserGroupService sysUserGroupService;

    /**
     * 查询用户组列表
     */
    @PreAuthorize("@ss.hasPermi('system:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUserGroup sysUserGroup)
    {
        startPage();
        List<SysUserGroup> list = sysUserGroupService.selectSysUserGroupList(sysUserGroup);
        return getDataTable(list);
    }

    /**
     * 导出用户组列表
     */
    @PreAuthorize("@ss.hasPermi('system:group:export')")
    @Log(title = "用户组", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysUserGroup sysUserGroup)
    {
        List<SysUserGroup> list = sysUserGroupService.selectSysUserGroupList(sysUserGroup);
        ExcelUtil<SysUserGroup> util = new ExcelUtil<SysUserGroup>(SysUserGroup.class);
        return util.exportExcel(list, "用户组数据");
    }

    /**
     * 获取用户组详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:group:query')")
    @GetMapping(value = "/{groupId}")
    public AjaxResult getInfo(@PathVariable("groupId") Long groupId)
    {
        return AjaxResult.success(sysUserGroupService.selectSysUserGroupById(groupId));
    }

    /**
     * 新增用户组
     */
    @PreAuthorize("@ss.hasPermi('system:group:add')")
    @Log(title = "用户组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUserGroup sysUserGroup)
    {
        return toAjax(sysUserGroupService.insertSysUserGroup(sysUserGroup));
    }

    /**
     * 修改用户组
     */
    @PreAuthorize("@ss.hasPermi('system:group:edit')")
    @Log(title = "用户组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUserGroup sysUserGroup)
    {
        return toAjax(sysUserGroupService.updateSysUserGroup(sysUserGroup));
    }

    /**
     * 删除用户组
     */
    @PreAuthorize("@ss.hasPermi('system:group:remove')")
    @Log(title = "用户组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{groupIds}")
    public AjaxResult remove(@PathVariable Long[] groupIds)
    {
        return toAjax(sysUserGroupService.deleteSysUserGroupByIds(groupIds));
    }
}
