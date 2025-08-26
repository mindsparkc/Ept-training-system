package com.sinosoft.admin.courseware.controller;

import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.core.TreeSelect;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 课件分类
 * 2023-5-6
 */
@RestController
@RequestMapping("/admin/v1/courseware/category")
public class CoursewareCategoryController extends BaseController {


    @Resource
    private ICategoryService categoryService;
    @Resource
    private IObjectInterCategoryService objectInterCategoryService;

    private static CategoryType.CategoryEnum categoryEnum = CategoryType.CategoryEnum.COURSEWARE_CATEGORY;

    @PreAuthorize("@ss.hasPermi('courseware:category:list')")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(Category category)
    {
        List<Category> list = categoryService.select(categoryEnum, category);
        List<TreeSelect> vos = categoryService.buildCategoryTreeSelect(list);
//        TreeSelect vo = new TreeSelect();
//        vo.setId(0L);
//        vo.setLabel("全部题目");
//        vos.add(0, vo);
        return AjaxResult.success(vos);
    }
    /**
     * 获取课件分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('courseware:category:query')")
    @GetMapping(value = "/{catId}")
    public AjaxResult getInfo(@PathVariable("catId") Long catId)
    {
        return AjaxResult.success(categoryService.selectById(categoryEnum, catId));
    }
    /**
     * 新增课件分类
     */
    @PreAuthorize("@ss.hasPermi('courseware:category:add')")
    @Log(title = "题目分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Category category) {
        // 校验是否存在同名子分类
        if (categoryService.checkNameExists(categoryEnum, null, category.getParentId(), category.getName()))
            throw new RuntimeException("该节点下已存在同名的，请检查后再创建！");

        return toAjax(categoryService.insert(categoryEnum, category));
    }

    /**
     * 修改课件分类
     */
    @PreAuthorize("@ss.hasPermi('courseware:category:edit')")
    @Log(title = "题目分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Category category) {
        if (categoryService.checkNameExists(categoryEnum, category.getCatId(), category.getParentId(), category.getName()))
            throw new RuntimeException("该节点下已存在同名的，请检查后再创建！");

        return toAjax(categoryService.updateCategory(categoryEnum, category));
    }

    /**
     * 删除课件分类
     */
    @PreAuthorize("@ss.hasPermi('courseware:category:remove')")
    @Log(title = "题目分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{catIds}")
    public AjaxResult remove(@PathVariable Long[] catIds) {
        if (categoryService.hasChild(categoryEnum, Arrays.asList(catIds))) {
            throw new RuntimeException("该分类下存在子分类，不允许删除！");
        }

        // 检查分类下是否存在题目
        if (objectInterCategoryService.hasObj(categoryEnum, Arrays.asList(catIds))) {
            throw new RuntimeException("该分类下存在课件，不允许删除！");
        }

        return toAjax(categoryService.deleteByIds(categoryEnum, Arrays.asList(catIds)));
    }
}


