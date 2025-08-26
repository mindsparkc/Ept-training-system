package com.sinosoft.etp.cp.sign.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.ResponseFieldUtil;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.sign.domain.TSignStudentWithCpExport;
import com.sinosoft.etp.cp.sign.domain.TSignWithCp;
import com.sinosoft.etp.cp.sign.service.ITSignWithCpService;
import com.sinosoft.etp.cp.sign.service.impl.TSignStudentWithCpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * signController
 *
 *
 * @date 2022-02-09
 */
@RestController
@RequestMapping("/etp/signWithCp")
public class TSignWithCpController extends BaseController
{
    @Autowired
    private ITSignWithCpService tSignService;
    @Resource
    private TSignStudentWithCpServiceImpl tSignStudentWithCpService;

    /**
     * 查询sign列表
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSignWithCp tSign)
    {
        String tags = tSign.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tSign.getParams().put("tags", tags.split(","));
            tSign.getParams().put("tagsCount", tags.split(",").length);
        }
        startPage();
        List<TSignWithCp> list = tSignService.selectTSignList(tSign);
        return getDataTable(list, TSignWithCp.class);
    }

    /**
     * 导出sign列表
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:export')")
    @Log(title = "sign", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TSignWithCp tSign)
    {
        List<TSignWithCp> list = tSignService.selectTSignList(tSign);
        ExcelUtil<TSignWithCp> util = new ExcelUtil<TSignWithCp>(TSignWithCp.class);
        return util.exportExcel(list, "sign数据");
    }

    /**
     * 导出活动报名学员列表
     */
    @Log(title = "sign", businessType = BusinessType.EXPORT)
    @GetMapping("/exportByStudent/{id}")
    public AjaxResult export(@PathVariable("id") Long id)
    {
        List<TSignStudentWithCpExport> list = tSignStudentWithCpService.selectBysId(id);
        ExcelUtil<TSignStudentWithCpExport> util = new ExcelUtil<>(TSignStudentWithCpExport.class);
        return util.exportExcel(list, "报名学员信息");
    }

    /**
     * 获取sign详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        ResponseFieldUtil responseFieldUtil = new ResponseFieldUtil<TSignWithCp>(TSignWithCp.class);
        return AjaxResult.success(responseFieldUtil.response(tSignService.selectTSignById(id)));
    }

    /**
     * 新增sign
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:add')")
    @Log(title = "sign", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSignWithCp tSign)
    {
        return toAjax(tSignService.insertTSign(tSign));
    }

    /**
     * 修改sign
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:edit')")
    @Log(title = "sign", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSignWithCp tSign)
    {
        return toAjax(tSignService.updateTSign(tSign));
    }

    /**
     * 删除sign
     */
    @PreAuthorize("@ss.hasPermi('etp:sign:remove')")
    @Log(title = "sign", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tSignService.deleteTSignByIds(ids));
    }
}
