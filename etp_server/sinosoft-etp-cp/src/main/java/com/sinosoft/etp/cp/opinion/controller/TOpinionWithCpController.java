package com.sinosoft.etp.cp.opinion.controller;

import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.opinion.domain.TOpinionWithCp;
import com.sinosoft.etp.cp.opinion.service.ITOpinionWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 意见反馈Controller
 *
 *
 * @date 2022-02-17
 */
@RestController
@RequestMapping("/etp/opinionWithCp")
public class TOpinionWithCpController extends BaseController
{
    @Autowired
    private ITOpinionWithCpService tOpinionService;

    /**
     * 查询意见反馈列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TOpinionWithCp tOpinion)
    {
        startPage();
        List<TOpinionWithCp> list = tOpinionService.selectTOpinionList(tOpinion);
        return getDataTable(list, TOpinionWithCp.class);
    }

    /**
     * 导出意见反馈列表
     */
    @Log(title = "意见反馈", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TOpinionWithCp tOpinion)
    {
        List<TOpinionWithCp> list = tOpinionService.selectTOpinionList(tOpinion);
        ExcelUtil<TOpinionWithCp> util = new ExcelUtil<TOpinionWithCp>(TOpinionWithCp.class);
        return util.exportExcel(list, "意见反馈数据");
    }

    /**
     * 获取意见反馈详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TOpinionWithCp>(TOpinionWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tOpinionService.selectTOpinionById(id)));
    }

    /**
     * 新增意见反馈
     */
    @Log(title = "意见反馈", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TOpinionWithCp tOpinion)
    {
        return toAjax(tOpinionService.insertTOpinion(tOpinion));
    }

    /**
     * 修改意见反馈
     */
    @Log(title = "意见反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TOpinionWithCp tOpinion)
    {
        return toAjax(tOpinionService.updateTOpinion(tOpinion));
    }

    /**
     * 删除意见反馈
     */
    @Log(title = "意见反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tOpinionService.deleteTOpinionByIds(ids));
    }
}
