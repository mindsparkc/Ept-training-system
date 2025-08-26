package com.sinosoft.web.controller.common;

import cn.jkingtools.utils.StringUtils;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.sinosoft.common.annotation.PreProcessField;
import com.sinosoft.common.config.MainConfig;
import com.sinosoft.common.constant.Constants;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.utils.file.FileUploadUtils;
import com.sinosoft.common.utils.file.FileUtils;
import com.sinosoft.framework.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用请求处理
 *
 *
 */
@RestController
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private DefaultFastFileStorageClient defaultFastFileStorageClient;

    //@Autowired
    //private FastDFSWithCpProperties fastDFSProperties;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = MainConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用删除 fastdfs
     * @param url 请求地址
     * @return AjaxResult
     * @auther cfl
     */
    @RequestMapping("/common/delete")
    public AjaxResult deleteFileByUrl(String url){
        // defaultFastFileStorageClient.deleteFile(url);
        return AjaxResult.success();
    }


    /**
     * 通用文件上传至 fastdfs文件系统
     * @param file file
     * @return AjaxResult
     * @auther cfl
     */
    @PreProcessField(field = "url")
    @PostMapping("/commom/upload/fastdfs")
    public AjaxResult uploadFileByFastDFS(MultipartFile file) throws Exception {
        AjaxResult ajaxResult = uploadFile(file);
        return ajaxResult;
    }

    /**
     * 通用上传请求
     */
    @PreProcessField(field = "url")
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = MainConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = /*serverConfig.getUrl()*/ "{storage.path.url}" + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = MainConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }
}
