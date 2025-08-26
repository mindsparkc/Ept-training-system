package com.sinosoft.admin.knowledge.controller;

import com.sinosoft.admin.category.core.CategoryType;
import com.sinosoft.admin.category.domain.Category;
import com.sinosoft.admin.category.service.ICategoryService;
import com.sinosoft.admin.category.service.IObjectInterCategoryService;
import com.sinosoft.admin.knowledge.domain.KnowledgeCategory;
import com.sinosoft.admin.knowledge.service.IKnowledgeCategoryService;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.core.TreeSelect;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 知识点分类树Controller
 *
 *
 * @date 2022-08-10
 */
@RestController
@RequestMapping("/admin/v1/knowledge/knowledgeCategory")
public class KnowledgeCategoryController extends BaseController
{
    @Resource
    private IKnowledgeCategoryService knowledgeCategoryService;
    @Resource
    private ICategoryService categoryService;
    @Resource
    private IObjectInterCategoryService objectInterCategoryService;

    private static CategoryType.CategoryEnum categoryEnum = CategoryType.CategoryEnum.KNOWLEDGE_CATEGORY;
    /**
     * 查询知识点分类树列表
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledgeCategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(KnowledgeCategory knowledgeCategory)
    {
        startPage();
        List<KnowledgeCategory> list = knowledgeCategoryService.selectKnowledgeCategoryList(knowledgeCategory);
        return getDataTable(list);
    }

    /**
     * 导出知识点分类树列表
     *
    @PreAuthorize("@ss.hasPermi('knowledge:knowledgeCategory:export')")
    @Log(title = "知识点分类树", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(KnowledgeCategory knowledgeCategory)
    {
        List<KnowledgeCategory> list = knowledgeCategoryService.selectKnowledgeCategoryList(knowledgeCategory);
        ExcelUtil<KnowledgeCategory> util = new ExcelUtil<KnowledgeCategory>(KnowledgeCategory.class);
        return util.exportExcel(list, "知识点分类树数据");
    }
    */

    /**
     * 获取知识点分类树详细信息
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledgeCategory:query')")
    @GetMapping(value = "/{catId}")
    public AjaxResult getInfo(@PathVariable("catId") Long catId)
    {
//        return AjaxResult.success(knowledgeCategoryService.selectKnowledgeCategoryById(catId));
        return AjaxResult.success(categoryService.selectById(categoryEnum, catId));
    }

    /**
     * 新增知识点分类树
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledgeCategory:add')")
    @Log(title = "知识点分类树", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody  Category category)
    {
//        return toAjax(knowledgeCategoryService.insertKnowledgeCategory(knowledgeCategory));
        // 校验是否存在同名子分类
        if (categoryService.checkNameExists(categoryEnum, null, category.getParentId(), category.getName()))
            throw new RuntimeException("该节点下已存在同名的，请检查后再创建！");

        return toAjax(categoryService.insert(categoryEnum, category));
    }

    /**
     * 修改知识点分类树
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledgeCategory:edit')")
    @Log(title = "知识点分类树", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Category category)
    {
//        return toAjax(knowledgeCategoryService.updateKnowledgeCategory(knowledgeCategory));
        if (categoryService.checkNameExists(categoryEnum, category.getCatId(), category.getParentId(), category.getName()))
            throw new RuntimeException("该节点下已存在同名的，请检查后再创建！");

        return toAjax(categoryService.updateCategory(categoryEnum, category));
    }

    /**
     * 删除知识点分类树
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledgeCategory:remove')")
    @Log(title = "知识点分类树", businessType = BusinessType.DELETE)
	@DeleteMapping("/{catIds}")
    public AjaxResult remove(@PathVariable Long[] catIds)
    {
//        return toAjax(knowledgeCategoryService.deleteKnowledgeCategoryByIds(catIds));
        if (categoryService.hasChild(categoryEnum, Arrays.asList(catIds))) {
            throw new RuntimeException("该分类下存在子分类，不允许删除！");
        }

        // 检查分类下是否存在题目
        if (objectInterCategoryService.hasObj(categoryEnum, Arrays.asList(catIds))) {
            throw new RuntimeException("该分类下存在课程，不允许删除！");
        }

        return toAjax(categoryService.deleteByIds(categoryEnum, Arrays.asList(catIds)));
    }

    /**
     * 查询分类树列表
     */
    @Log(title = "查询数列表",businessType = BusinessType.OTHER)
    @GetMapping("/treeSelect")
    public AjaxResult treeSelect(Category category){
//        List<KnowledgeCategory> list = knowledgeCategoryService.selectKnowledgeCategoryList(knowledgeCategory);
//        return AjaxResult.success(knowledgeCategoryService.buildCategoryTreeSelect(list));
        List<Category> list = categoryService.select(categoryEnum, category);
        List<TreeSelect> vos = categoryService.buildCategoryTreeSelect(list);
        return AjaxResult.success(vos);
    }

    /**
     * 检验分类名称在父类中是否存在
     */
    @GetMapping("/checkName")
    public AjaxResult checkName(Long parentId,String name){
        int num = knowledgeCategoryService.checkName(parentId,name);
        if(num>0){
            return AjaxResult.error("该分类中已存在相同名称！");
        }else {
            return AjaxResult.success();
        }
    }
}
