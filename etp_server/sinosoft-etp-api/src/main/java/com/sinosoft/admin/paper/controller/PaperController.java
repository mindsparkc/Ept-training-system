package com.sinosoft.admin.paper.controller;

import com.sinosoft.admin.paper.domain.PaperQuery;
import com.sinosoft.admin.paper.domain.PaperReqDTO;
import com.sinosoft.admin.paper.service.IPaperService;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.core.domain.Paper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 试卷基础信息Controller
 *
 *
 * @date 2022-08-24
 */
@RestController
@RequestMapping("/admin/v1/paper")
public class PaperController extends BaseController
{
    @Resource
    private IPaperService paperService;

    /**
     * 查询试卷基础信息列表
     */
    @PreAuthorize("@ss.hasPermi('paper:paper:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaperQuery paper)
    {
        startPage();
        List<Paper> list = paperService.selectPaperList(paper);
        return getDataTable(list);
    }

    /**
     * 获取试卷基础信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('paper:paper:query')")
    @GetMapping(value = "/{paperId}")
    public AjaxResult getInfo(@PathVariable("paperId") Long paperId)
    {
        return AjaxResult.success(paperService.selectPaperById(paperId));
    }

    /**
     * 预览试卷
     */
    @PreAuthorize("@ss.hasPermi('paper:paper:preview')")
    @GetMapping(value = "/preview/{paperId}")
    public AjaxResult preview(@PathVariable("paperId") Long paperId) {
        return AjaxResult.success(paperService.previewPaperById(paperId));
    }

    /**
     * 新增试卷基础信息
     */
    @PreAuthorize("@ss.hasPermi('paper:paper:add')")
    @Log(title = "试卷基础信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaperReqDTO paperDTO) throws InvocationTargetException, IllegalAccessException {
        Long paperId = paperService.insertPaper(paperDTO);
        Map<String, Long> paper = new HashMap<>();
        paper.put("paperId", paperId);
        return AjaxResult.success(paper);
    }

    /**
     * 修改试卷基础信息
     */
    @PreAuthorize("@ss.hasPermi('paper:paper:edit')")
    @Log(title = "试卷基础信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaperReqDTO paperDTO)
    {
        return toAjax(paperService.updatePaper(paperDTO));
    }

    /**
     * 修改试卷状态
     */
    @PreAuthorize("@ss.hasPermi('paper:paper:edit')")
    @Log(title = "修改试卷状态", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult editStatus(@RequestBody PaperReqDTO paperDTO) {
        return toAjax(paperService.updatePaperStatus(paperDTO));
    }

    /**
     * 删除试卷基础信息
     */
    @PreAuthorize("@ss.hasPermi('paper:paper:remove')")
    @Log(title = "试卷基础信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{paperIds}")
    public AjaxResult remove(@PathVariable Long[] paperIds)
    {
        return toAjax(paperService.deletePaperByIds(paperIds));
    }


    /**
     * 获取课程中包含的习题集
     * @param kcid
     * @return
     */
    @GetMapping("/selectedXtjByKc")
    public AjaxResult getSelectedXtjByKc(Long kcid)
    {
        List<Paper> list = paperService.getSelectedXtjByKc(kcid);
        return AjaxResult.success(list);
    }
}
