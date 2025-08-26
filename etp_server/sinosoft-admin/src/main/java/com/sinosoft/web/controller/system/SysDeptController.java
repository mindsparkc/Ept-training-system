package com.sinosoft.web.controller.system;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.constant.UserConstants;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.domain.entity.SysDept;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.system.domain.SysTenant;
import com.sinosoft.system.service.ISysDeptService;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * 部门信息
 *
 *
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表 excludeDeptId需要排除的部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept,Long excludeDeptId)
    {
        //20250528 lyd 增加权限控制
        String deptDataScope="";
        if("tenant".equals(dept.getRootNodeType())) {
            deptDataScope = EtpDataScopeUtils.getDeptIdDataScopeSqlByTenant("d");
        }
        if("dept".equals(dept.getRootNodeType())) {
            deptDataScope = EtpDataScopeUtils.getDeptIdDataScopeSqlByDept("d");
        }
        dept.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE,deptDataScope);

        List<SysDept> depts = deptService.selectDeptList(dept);
        if(excludeDeptId!=null){
            Iterator<SysDept> it = depts.iterator();
            while (it.hasNext())
            {
                SysDept d = (SysDept) it.next();
                if (d.getDeptId().intValue() == excludeDeptId
                        || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), excludeDeptId + ""))
                {
                    it.remove();
                }
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 获取二级部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/querySecondDept")
    public TableDataInfo querySecondDept(SysDept dept)
    {
        // 查询顶级部门
        SysDept topDept = queryTopDept();
        // 开始查询二级部门 （父级部门为顶级部门的id即为二级部门）
        dept.setParentId(topDept.getDeptId());
        startPage();
        List<SysDept> secondDeptList = deptService.selectDeptList(dept);
        return getDataTable(secondDeptList);
    }

    // 查询顶级部门
    private SysDept queryTopDept() {
        SysDept topDept = deptService.queryTopDept();
        if (ObjectUtils.isEmpty(topDept)) {
            throw new RuntimeException("顶级部门查询错误");
        }
        return topDept;
    }

    /**
     * 查询部门列表（排除节点）   由于不能接收其他参数，不好确定根节点类型，控制部门数据权限不好实现(如list方法)
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext())
        {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId)
    {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(dept.getDeptId()))
        {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0)
        {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }
}
