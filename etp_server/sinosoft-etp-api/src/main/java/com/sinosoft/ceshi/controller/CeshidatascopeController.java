package com.sinosoft.ceshi.controller;

import java.util.List;

import com.sinosoft.common.utils.ServletUtils;
import org.apache.tomcat.util.http.fileupload.RequestContext;
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
import com.sinosoft.ceshi.domain.Ceshidatascope;
import com.sinosoft.ceshi.service.ICeshidatascopeService;
    import com.sinosoft.common.core.page.TableDataInfo;

import javax.annotation.Resource;

/**
 * 数据权限校验Controller
 *
 * @author sinosoft
 * @date 2022-10-25
 */
@RestController
@RequestMapping("/etp/ceshidatascope")
public class CeshidatascopeController extends BaseController
{
    @Resource
    private ICeshidatascopeService ceshidatascopeService;

    /**
     * 查询数据权限校验列表
     */
    @PreAuthorize("@ss.hasPermi('etp:ceshidatascope:list')")
    @GetMapping("/list")
    public TableDataInfo list(Ceshidatascope ceshidatascope)
    {
        startPage();
        List<Ceshidatascope> list = ceshidatascopeService.selectCeshidatascopeList(ceshidatascope);
        return getDataTable(list);
    }

    /**
     * 导出数据权限校验列表
     *
    @PreAuthorize("@ss.hasPermi('etp:ceshidatascope:export')")
    @Log(title = "数据权限校验", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Ceshidatascope ceshidatascope)
    {
        List<Ceshidatascope> list = ceshidatascopeService.selectCeshidatascopeList(ceshidatascope);
        ExcelUtil<Ceshidatascope> util = new ExcelUtil<Ceshidatascope>(Ceshidatascope.class);
        return util.exportExcel(list, "数据权限校验数据");
    }
    */

    /**
     * 获取数据权限校验详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:ceshidatascope:query')")
    @GetMapping(value = "/{name}")
    public AjaxResult getInfo(@PathVariable("name") String name)
    {
        return AjaxResult.success(ceshidatascopeService.selectCeshidatascopeById(name));
    }

    /**
     * 新增数据权限校验
     */
    @PreAuthorize("@ss.hasPermi('etp:ceshidatascope:add')")
    @Log(title = "数据权限校验", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Ceshidatascope ceshidatascope)
    {
        return toAjax(ceshidatascopeService.insertCeshidatascope(ceshidatascope));
    }

    /**
     * 修改数据权限校验
     */
    @PreAuthorize("@ss.hasPermi('etp:ceshidatascope:edit')")
    @Log(title = "数据权限校验", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Ceshidatascope ceshidatascope)
    {
        return toAjax(ceshidatascopeService.updateCeshidatascope(ceshidatascope));
    }

    /**
     * 删除数据权限校验
     */
    @PreAuthorize("@ss.hasPermi('etp:ceshidatascope:remove')")
    @Log(title = "数据权限校验", businessType = BusinessType.DELETE)
	@DeleteMapping("/{names}")
    public AjaxResult remove(@PathVariable String[] names)
    {
        return toAjax(ceshidatascopeService.deleteCeshidatascopeByIds(names));
    }
}
