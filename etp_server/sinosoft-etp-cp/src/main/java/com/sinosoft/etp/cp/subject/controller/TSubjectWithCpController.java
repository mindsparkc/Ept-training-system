package com.sinosoft.etp.cp.subject.controller;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.config.MainConfig;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.file.FileUploadUtils;
import com.sinosoft.common.utils.file.FileUtils;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.etp.cp.subject.domain.TSubjectWithCp;
import com.sinosoft.etp.cp.subject.service.ITSubjectWithCpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 题库Controller
 *
 *
 * @date 2021-11-08
 */
@RestController
@RequestMapping("/etp/subjectWithCp")
public class TSubjectWithCpController extends BaseController
{
    @Autowired
    private ITSubjectWithCpService tSubjectService;

    /**
     * 查询题库列表
     */
    @PreAuthorize("@ss.hasPermi('etp:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSubjectWithCp tSubject)
    {
        startPage();
        String tags = tSubject.getTags();
        if(StringUtils.isNotEmpty(tags)){
            tSubject.getParams().put("tags", tags.split(","));
            tSubject.getParams().put("tagsCount", tags.split(",").length);
        }
        List<TSubjectWithCp> list = tSubjectService.selectTSubjectList(tSubject);
        return getDataTable(list);
    }

    /**
     * 导出题库列表
     */
    @PreAuthorize("@ss.hasPermi('etp:subject:export')")
    @Log(title = "题库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TSubjectWithCp tSubject)
    {
        List<TSubjectWithCp> list = tSubjectService.selectTSubjectList(tSubject);
        ExcelUtil<TSubjectWithCp> util = new ExcelUtil<TSubjectWithCp>(TSubjectWithCp.class);
        return util.exportExcel(list, "题库数据");
    }

    /**
     * 获取题库详细信息
     */
    @PreAuthorize("@ss.hasPermi('etp:subject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tSubjectService.selectTSubjectById(id));
    }

    /**
     * 新增题库
     */
    @PreAuthorize("@ss.hasPermi('etp:subject:add')")
    @Log(title = "题库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSubjectWithCp tSubject)
    {
        return toAjax(tSubjectService.insertTSubject(tSubject));
    }

    /**
     * 修改题库
     */
    @PreAuthorize("@ss.hasPermi('etp:subject:edit')")
    @Log(title = "题库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSubjectWithCp tSubject)
    {
        return toAjax(tSubjectService.updateTSubject(tSubject));
    }

    /**
     * 删除题库
     */
    @PreAuthorize("@ss.hasPermi('etp:subject:remove')")
    @Log(title = "题库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tSubjectService.deleteTSubjectByIds(ids));
    }


    /**
     * 随机获取获取题库
     */
    @GetMapping(value = "getSubjectRand")
    public AjaxResult getSubjectRand(String ids,int num)
    {
        return AjaxResult.success(tSubjectService.getSubjectRand(ids,num));
    }

    @PreAuthorize("@ss.hasPermi('etp:subject:import')")
    @Log(title = "批量导入题库", businessType = BusinessType.IMPORT)
    @PostMapping(value = "batch")
    public AjaxResult batchSubjects(MultipartFile file) {
        try {
            // 文件临时存储路径
            String filePath = MainConfig.getUploadTemporaryPath();
            // 保存临时文件，并返回路径
            String fileName = FileUploadUtils.upload0(filePath, file);
            // 处理文件
            return AjaxResult.success(tSubjectService.batchSubjects(fileName));
        }catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 下载模版文件
     *
     * @param response
     * @param request
     */
   // @PreAuthorize("@ss.hasPermi('etp:subject:template')")
    @GetMapping("template")
    public void downBatchTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:template/subjects_20220415.docx");
        Resource resource = resources[0];
        //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
        try (InputStream inputStream = resource.getInputStream()) {
            System.out.println(inputStream.available());
            // 清空response
            response.reset();
            // 设置response的Header
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            // response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("subjects_20220415.docx", "UTF-8"));
            FileUtils.setAttachmentResponseHeader(response, "subjects_20220415.docx");
            // 告知浏览器文件的大小
            // response.addHeader("Content-Length", "" + inputStream.available());
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


    @GetMapping("/selectedStByKc")
    public AjaxResult getSelectedStByKc(Long kcid)
    {
        List<TSubjectWithCp> list = tSubjectService.getSelectedStByKc(kcid);
        return AjaxResult.success(list);
    }
}
