package com.sinosoft.admin.knowledge.controller;

import com.sinosoft.admin.knowledge.domain.Knowledge;
import com.sinosoft.admin.knowledge.service.IKnowledgeService;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 知识点Controller
 *
 *
 * @date 2022-08-10
 */
@RestController
@RequestMapping("/admin/v1/knowledge/knowledge")
public class KnowledgeController extends BaseController
{
    @Resource
    private IKnowledgeService knowledgeService;

    /**
     * 查询知识点列表
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledge:list')")
    @GetMapping("/list")
    public TableDataInfo list(Knowledge knowledge)
    {
        startPage();
        List<Knowledge> list = knowledgeService.selectKnowledgeList(knowledge);
        return getDataTable(list);
    }

    /**
     * 导出知识点列表
     *
    @PreAuthorize("@ss.hasPermi('knowledge:knowledge:export')")
    @Log(title = "知识点", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Knowledge knowledge)
    {
        List<Knowledge> list = knowledgeService.selectKnowledgeList(knowledge);
        ExcelUtil<Knowledge> util = new ExcelUtil<Knowledge>(Knowledge.class);
        return util.exportExcel(list, "知识点数据");
    }
    */

    /**
     * 获取知识点详细信息
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledge:query')")
    @GetMapping(value = "/{knoledgeId}")
    public AjaxResult getInfo(@PathVariable("knoledgeId") Long knoledgeId)
    {
        return AjaxResult.success(knowledgeService.selectKnowledgeById(knoledgeId));
    }

    /**
     * 新增知识点
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledge:add')")
    @Log(title = "知识点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Knowledge knowledge)
    {
        return toAjax(knowledgeService.insertKnowledge(knowledge));
    }

    /**
     * 修改知识点
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledge:edit')")
    @Log(title = "知识点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Knowledge knowledge)
    {
        return toAjax(knowledgeService.updateKnowledge(knowledge));
    }

    /**
     * 删除知识点
     */
    @PreAuthorize("@ss.hasPermi('knowledge:knowledge:remove')")
    @Log(title = "知识点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{knoledgeIds}")
    public AjaxResult remove(@PathVariable Long[] knoledgeIds)
    {
        return toAjax(knowledgeService.deleteKnowledgeByIds(knoledgeIds));
    }

    /**
     * 获取下级知识点
     */
    @GetMapping(value = "/getChildren/{parentId}")
    public AjaxResult getChildList(@PathVariable Long parentId)
    {
        return AjaxResult.success(knowledgeService.getChildList(parentId));
    }
}
