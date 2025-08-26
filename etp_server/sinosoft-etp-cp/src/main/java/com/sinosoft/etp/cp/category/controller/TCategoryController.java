package com.sinosoft.etp.cp.category.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.category.domain.TCategory;
import com.sinosoft.etp.cp.category.service.ITCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类树Controller
 *
 * 
 * @date 2022-05-11
 */
@RestController
@RequestMapping("/etp/category")
public class TCategoryController extends BaseController
{
    @Autowired
    private ITCategoryService tCategoryService;

    /**
     * 查询分类树列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TCategory tCategory)
    {
        startPage();
        List<TCategory> list = tCategoryService.selectTCategoryList(tCategory);
        return getDataTable(list);
    }

    /**
     * 导出分类树列表
     */
    @PreAuthorize("@ss.hasPermi('etp:category:export')")
    @Log(title = "分类树", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TCategory tCategory)
    {
        List<TCategory> list = tCategoryService.selectTCategoryList(tCategory);
        ExcelUtil<TCategory> util = new ExcelUtil<TCategory>(TCategory.class);
        return util.exportExcel(list, "分类树数据");
    }

    /**
     * 获取分类树详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tCategoryService.selectTCategoryById(id));
    }

    /**
     * 新增分类树
     */
    @PreAuthorize("@ss.hasPermi('etp:category:add')")
    @Log(title = "分类树", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCategory tCategory)
    {
        if (tCategory.getParentId() == 1) {
            tCategory.setParentId(0L);
        }
        if ("1".equals(tCategoryService.checkCategoryNameUnique(tCategory)))
        {
            return AjaxResult.error("新增分类'" + tCategory.getName() + "'失败，分类名称已存在");
        }
        return toAjax(tCategoryService.insertTCategory(tCategory));
    }

    /**
     * 修改分类树
     */
    @PreAuthorize("@ss.hasPermi('etp:category:edit')")
    @Log(title = "分类树", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCategory tCategory)
    {
        return toAjax(tCategoryService.updateTCategory(tCategory));
    }

    /**
     * 删除分类树
     */
    @PreAuthorize("@ss.hasPermi('etp:category:remove')")
    @Log(title = "分类树", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        if (tCategoryService.hasChildByCategoryId(id))
        {
            return AjaxResult.error("存在下级分类,不允许删除");
        }
        if (tCategoryService.checkCategoryExistKJ(id))
        {
            return AjaxResult.error("此分类有课件数据,不允许删除");
        }
        if (tCategoryService.checkCategoryExistKC(id))
        {
            return AjaxResult.error("此分类有课程数据,不允许删除");
        }
        if (tCategoryService.checkCategoryExistSJ(id))
        {
            return AjaxResult.error("此分类有试卷数据,不允许删除");
        }
        if (tCategoryService.checkCategoryExistTM(id))
        {
            return AjaxResult.error("此分类有试题数据,不允许删除");
        }
        if (tCategoryService.checkCategoryExistKS(id))
        {
            return AjaxResult.error("此分类有考试数据,不允许删除");
        }
        return toAjax(tCategoryService.deleteTCategoryById(id));
    }

    /**
     * 获取分类树下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(TCategory tCategory)
    {
        List<TCategory> tCategorys = tCategoryService.selectTCategoryList(tCategory);
        return AjaxResult.success(tCategoryService.buildCategoryTreeSelect(tCategorys));
    }
}
