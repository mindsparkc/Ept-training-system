package com.sinosoft.etp.cp.certificate.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.certificate.domain.TCertificateWithCp;
import com.sinosoft.etp.cp.certificate.service.ITCertificateWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 证书管理Controller
 *
 *
 * @date 2022-02-07
 */
@RestController
@RequestMapping("/etp/certificateWithCp")
public class TCertificateWithCpController extends BaseController
{
    @Autowired
    private ITCertificateWithCpService tCertificateService;

    /**
     * 查询证书管理列表
     */
    @PreAuthorize("@ss.hasPermi('etp:certificate:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCertificateWithCp tCertificate)
    {
        startPage();
        String tags = tCertificate.getTags();
        if(StringUtils.isNotBlank(tags)){
            tCertificate.getParams().put("tags",tags.split(","));
            tCertificate.getParams().put("tagsCount",tags.split(",").length);
        }
        List<TCertificateWithCp> list = tCertificateService.selectTCertificateList(tCertificate);
        return getDataTable(list, TCertificateWithCp.class);
    }

    /**
     * 导出证书管理列表
     */
    @PreAuthorize("@ss.hasPermi('etp:certificate:export')")
    @Log(title = "证书管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TCertificateWithCp tCertificate)
    {
        List<TCertificateWithCp> list = tCertificateService.selectTCertificateList(tCertificate);
        ExcelUtil<TCertificateWithCp> util = new ExcelUtil<TCertificateWithCp>(TCertificateWithCp.class);
        return util.exportExcel(list, "证书管理数据");
    }

    /**
     * 获取证书管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:certificate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tCertificateService.selectTCertificateById(id));
    }

    /**
     * 新增证书管理
     */
    @PreAuthorize("@ss.hasPermi('etp:certificate:add')")
    @Log(title = "证书管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCertificateWithCp tCertificate)
    {
        return toAjax(tCertificateService.insertTCertificate(tCertificate));
    }

    /**
     * 修改证书管理
     */
    @PreAuthorize("@ss.hasPermi('etp:certificate:edit')")
    @Log(title = "证书管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCertificateWithCp tCertificate)
    {
        return toAjax(tCertificateService.updateTCertificate(tCertificate));
    }

    /**
     * 删除证书管理
     */
    @PreAuthorize("@ss.hasPermi('etp:certificate:remove')")
    @Log(title = "证书管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCertificateService.deleteTCertificateByIds(ids));
    }
}
