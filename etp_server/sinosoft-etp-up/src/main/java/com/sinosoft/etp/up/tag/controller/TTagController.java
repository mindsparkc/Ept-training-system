package com.sinosoft.etp.up.tag.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.common.domain.TTag;
import com.sinosoft.etp.up.tag.service.ITTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 标签管理Controller
 *
 *
 * @date 2021-11-15
 */
@RestController
@RequestMapping("/etp/tag")
public class TTagController extends BaseController
{
    @Autowired
    private ITTagService tTagService;

    /**
     * 查询标签管理列表
     */
    @PreAuthorize("@ss.hasPermi('etp:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(TTag tTag)
    {
        startPage();
        List<TTag> list = tTagService.selectTTagList(tTag);
        return getDataTable(list);
    }
    /**
     * 导出标签管理列表
     */
    @PreAuthorize("@ss.hasPermi('etp:tag:export')")
    @Log(title = "标签管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TTag tTag)
    {
        List<TTag> list = tTagService.selectTTagList(tTag);
        ExcelUtil<TTag> util = new ExcelUtil<TTag>(TTag.class);
        return util.exportExcel(list, "标签管理数据");
    }

    /**
     * 获取标签管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:tag:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tTagService.selectTTagById(id));
    }

    /**
     * 新增标签管理
     */
    @PreAuthorize("@ss.hasPermi('etp:tag:add')")
    @Log(title = "标签管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TTag tTag)
    {
        return toAjax(tTagService.insertTTag(tTag));
    }

    /**
     * 修改标签管理
     */
    @PreAuthorize("@ss.hasPermi('etp:tag:edit')")
    @Log(title = "标签管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TTag tTag)
    {
        return toAjax(tTagService.updateTTag(tTag));
    }

    /**
     * 删除标签管理
     */
    @PreAuthorize("@ss.hasPermi('etp:tag:remove')")
    @Log(title = "标签管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tTagService.deleteTTagByIds(ids));
    }

    /**
     * 查询xx-标签关联表
     * @param param
     * @return
     */

    @GetMapping("/relateTag")
    public AjaxResult listRelateTag(@RequestParam Map<String,Object> param)
    {
        //功能类型
        String type = param.get("modelType").toString();

        List<TTag> list = tTagService.listRelateTag(type);
        return AjaxResult.success(list);
    }

}
