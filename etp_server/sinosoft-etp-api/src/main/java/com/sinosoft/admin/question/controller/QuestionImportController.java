package com.sinosoft.admin.question.controller;

import com.sinosoft.admin.question.domain.QuestionImport;
import com.sinosoft.admin.question.domain.QuestionImportRecord;
import com.sinosoft.admin.question.service.IQuestionImport;
import com.sinosoft.common.annotation.Log;
import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.page.TableDataInfo;
import com.sinosoft.common.enums.BusinessType;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.file.FileUtils;
import com.sinosoft.common.utils.poi.ExcelUtil;
import com.sinosoft.core.domain.Question;
import com.sinosoft.framework.web.service.TokenService;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/v1/question")
public class QuestionImportController extends BaseController {

    @Resource
    private TokenService tokenService;

    @Resource
    private IQuestionImport questionImport;

    /**
     * 批量导入题目
     * @param file
     * @return
     * @throws Exception
     */
    @Log(title = "题目管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('question:import:submit')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<QuestionImport> util = new ExcelUtil<QuestionImport>(QuestionImport.class);
        List<QuestionImport> questions = util.importExcel("题目", file.getInputStream());
        Long aLong = questionImport.importQuestion(questions, file.getOriginalFilename());
        Map<String, Long> result = new HashMap<>();
        result.put("recordId", aLong);
        return AjaxResult.success(result);
    }

    // 查询导入列表
    @PreAuthorize("@ss.hasPermi('question:import:submit')")
    @GetMapping("/importData/list")
    public TableDataInfo selectImportRecord(@RequestParam("status") String status, @RequestParam("fileName") String fileName)
            throws Exception {
        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();

        startPage();
        List<QuestionImportRecord> records = questionImport.selectImportRecord(userId, status, fileName);
        return getDataTable(records);
    }

    // 根据 ID 查询导入的题目列表
    @PreAuthorize("@ss.hasPermi('question:import:submit')")
    @GetMapping("/importData/{recordId}")
    public AjaxResult selectImportData(@PathVariable("recordId") Long recordId) throws Exception {
        return AjaxResult.success(questionImport.selectImportData(recordId));
    }

    // 提交某 ID 批次的导入，导入提交后，即变成已提交状态，不能再次提交
    @PreAuthorize("@ss.hasPermi('question:import:submit')")
    @GetMapping("/importData/submit/{recordId}")
    public AjaxResult submitImportData(@PathVariable("recordId") Long recordId) throws Exception {
        return toAjax(questionImport.submitImportRecord(recordId));
    }

    // 放弃导入题目操作，将导入记录表中的状态改为‘8’
    @PreAuthorize("@ss.hasPermi('question:import:submit')")
    @GetMapping("/importData/giveup/{recordId}")
    public AjaxResult giveupImportData(@PathVariable("recordId") Long recordId) throws Exception {
        questionImport.giveupImportRecord(recordId);
        return AjaxResult.success("放弃导入操作成功");
    }

    // 修改某 ID 导入，某题目的信息
    @PreAuthorize("@ss.hasPermi('question:import:submit')")
    @GetMapping("/importData/edit")
    public AjaxResult editImportData(Question question) throws Exception {
        return null;
    }

    /**
     * 修改忽略导入题目状态
     */
    @PreAuthorize("@ss.hasPermi('question:import:submit')")
    @Log(title = "修改导入题目状态", businessType = BusinessType.DELETE)
    @DeleteMapping("/importData/{quIds}")
    public AjaxResult deleteImportData(@PathVariable Long[] quIds){
        questionImport.deleteImportData(quIds);
        return AjaxResult.success("选中题目已忽略导入");
    }

    /**
     * 下载试题导入模版文件
     *
     * @param response
     * @param request
     */
//    @PreAuthorize("@ss.hasPermi('question:import:submit')")
    @GetMapping("/importData/template")
    public void downBatchTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        org.springframework.core.io.Resource[] resources = resolver.getResources("classpath:template/批量导入题目模板(new).xlsx");
        org.springframework.core.io.Resource resource = resources[0];
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
            FileUtils.setAttachmentResponseHeader(response, "批量导入题目模板(new).xlsx");
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
}
