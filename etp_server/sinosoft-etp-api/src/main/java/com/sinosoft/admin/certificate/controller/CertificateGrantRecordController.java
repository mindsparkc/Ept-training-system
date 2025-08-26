package com.sinosoft.admin.certificate.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.certificate.domain.CertificateGrantRecord;
import com.sinosoft.admin.certificate.service.ICertificateGrantRecordService;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.framework.config.ServerConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 电子证书颁发记录Controller
 *
 *
 * @date 2022-07-20
 */
@RestController
@RequestMapping("/api/v1/record")
public class CertificateGrantRecordController extends BaseController
{
    @Resource
    private ICertificateGrantRecordService certificateGrantRecordService;
    @Resource
    private ServerConfig serverConfig;
    /**
     * 查询电子证书颁发记录列表
     */
    @PreAuthorize("@ss.hasPermi('api:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(CertificateGrantRecord certificateGrantRecord)
    {
        startPage();
        List<CertificateGrantRecord> list = certificateGrantRecordService.selectCertificateGrantRecordList(certificateGrantRecord);
        for (CertificateGrantRecord l : list) {
            if(StringUtils.isNotEmpty(l.getPreviewFile())){
                l.setPreviewFile( l.getPreviewFile().replace("{storage.path.url}",serverConfig.getUrl()));
            }
        }
        return getDataTable(list);
    }

    /**
     * 学员端
     * @param certificateGrantRecord
     * @return
     */
    @GetMapping("/certList")
    public TableDataInfo certList(CertificateGrantRecord certificateGrantRecord)
    {
        startPage();
        certificateGrantRecord.setUserId(SecurityUtils.getLoginUser().getUser().getUserId());
        certificateGrantRecord.setStatus("99");
        List<CertificateGrantRecord> list = certificateGrantRecordService.selectCertificateGrantRecordList(certificateGrantRecord);
        for (CertificateGrantRecord l : list) {
            if(null!=l.getPreviewFile()){
                l.setPreviewFile( l.getPreviewFile().replace("{storage.path.url}",serverConfig.getUrl()));
            }
        }
        return getDataTable(list);
    }

    /**
     * 获取电子证书颁发记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:record:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return AjaxResult.success(certificateGrantRecordService.selectCertificateGrantRecordById(recordId));
    }

    /**
     * 新增电子证书颁发记录
     */
    @PreAuthorize("@ss.hasPermi('api:record:add')")
    @Log(title = "电子证书颁发记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CertificateGrantRecord certificateGrantRecord)
    {
        return toAjax(certificateGrantRecordService.insertCertificateGrantRecord(certificateGrantRecord));
    }

    /**
     * 修改电子证书颁发记录
     */
    @PreAuthorize("@ss.hasPermi('api:record:edit')")
    @Log(title = "电子证书颁发记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CertificateGrantRecord certificateGrantRecord)
    {
        return toAjax(certificateGrantRecordService.updateCertificateGrantRecord(certificateGrantRecord));
    }

    /**
     * 删除电子证书颁发记录
     */
    @PreAuthorize("@ss.hasPermi('api:record:remove')")
    @Log(title = "电子证书颁发记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(certificateGrantRecordService.deleteCertificateGrantRecordByIds(recordIds));
    }

}
