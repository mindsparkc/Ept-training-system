package com.sinosoft.admin.certificate.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.admin.certificate.domain.CertificateTemplate;
import com.sinosoft.admin.certificate.service.ICertificateTemplateService;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.file.FileUtils;
import com.sinosoft.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 电子证书模板Controller
 *
 *
 * @date 2022-07-15
 */
@RestController
@RequestMapping("/api/v1/certificate")
public class CertificateTemplateController extends BaseController
{
    @Autowired
    private ICertificateTemplateService certificateTemplateService;
    @Autowired
    private ServerConfig serverConfig;

    /**
     * 查询电子证书模板列表
     */
    @PreAuthorize("@ss.hasPermi('api:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(CertificateTemplate certificateTemplate)
    {
        startPage();
        List<CertificateTemplate> list = certificateTemplateService.selectCertificateTemplateList(certificateTemplate);
        for (CertificateTemplate l : list) {
            if(StringUtils.isNotEmpty(l.getPreviewFile())){
                l.setPreviewFile( l.getPreviewFile().replace("{storage.path.url}",serverConfig.getUrl()));
            }
        }

        return getDataTable(list);
    }

    /**
     * 导出电子证书模板列表
     *
     @PreAuthorize("@ss.hasPermi('etp:template:export')")
     @Log(title = "电子证书模板", businessType = BusinessType.EXPORT)
     @GetMapping("/export")
     public AjaxResult export(CertificateTemplate certificateTemplate)
     {
     List<CertificateTemplate> list = certificateTemplateService.selectCertificateTemplateList(certificateTemplate);
     ExcelUtil<CertificateTemplate> util = new ExcelUtil<CertificateTemplate>(CertificateTemplate.class);
     return util.exportExcel(list, "电子证书模板数据");
     }
     */

    /**
     * 获取电子证书模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('api:template:query')")
    @GetMapping(value = "/{tempId}")
    public AjaxResult getInfo(@PathVariable("tempId") Long tempId)
    {
        return AjaxResult.success(certificateTemplateService.selectCertificateTemplateById(tempId));
    }

    /**
     * 新增电子证书模板
     */
    @PreAuthorize("@ss.hasPermi('api:template:add')")
    @Log(title = "电子证书模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CertificateTemplate certificateTemplate)
    {
        return toAjax(certificateTemplateService.insertCertificateTemplate(certificateTemplate));
    }

    /**
     * 修改电子证书模板
     */
    @PreAuthorize("@ss.hasPermi('api:template:edit')")
    @Log(title = "电子证书模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CertificateTemplate certificateTemplate)
    {
        return toAjax(certificateTemplateService.updateCertificateTemplate(certificateTemplate));
    }

    /**
     * 删除电子证书模板
     */
    @PreAuthorize("@ss.hasPermi('api:template:remove')")
    @Log(title = "电子证书模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tempIds}")
    public AjaxResult remove(@PathVariable Long[] tempIds)
    {
        return toAjax(certificateTemplateService.deleteCertificateTemplateByIds(tempIds));
    }

    /**
     * 模板下载
     * @param response
     * @param request
     * @throws IOException
     */
    @GetMapping("template")
    public void downBatchTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:template/cret_template.docx");
        Resource resource = resources[0];
        //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
        try (InputStream inputStream = resource.getInputStream()) {
            System.out.println(inputStream.available());
            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            FileUtils.setAttachmentResponseHeader(response, "cret_template.docx");
            response.setContentType("application/octet-stream");
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0)
            {
                response.getOutputStream().write(b, 0, length);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发布证书 提交
     * @return
     */
    @PostMapping("submitPublish")
    public AjaxResult submitPublish(@RequestBody Map<String,Object> data){
        //用户ID
        String userIds[] = data.get("userIds").toString().split(",");
        //证书ID
        Long templateId = Long.parseLong(data.get("templateId").toString());

        return toAjax(certificateTemplateService.submitPublish(templateId,userIds));
    }
}

