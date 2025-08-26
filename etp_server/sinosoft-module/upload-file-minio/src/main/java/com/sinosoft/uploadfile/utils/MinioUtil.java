package com.sinosoft.uploadfile.utils;

import com.sinosoft.common.utils.ServletUtils;
import com.sinosoft.common.utils.spring.SpringUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Minio 操作工具类
 * @author wanglijie
 */
public class MinioUtil {
    /**
     * 上传文件，并返回文件预览地址（可指定预览地址的有效时间）
     *
     * @param bucketName
     * @param objectName
     * @param file
     */
    public static String uploadFile(String bucketName, String objectName, File file) throws IOException {
        MinioClient minioClient = SpringUtils.getBean(MinioClient.class);
        String url = "";
        try {
            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .contentType(ContentTypeUtils.getContentType(getFileExt(file.getName())))
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(new FileInputStream(file), file.length(), -1)
                    .build());

            // 获取文件预览 URL
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .method(Method.GET)
                    .build());
            url = url.substring(0, url.indexOf('?'));
            return ServletUtils.urlEncode(url);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public static String getFileExt(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        return fileName.substring(lastIndexOf);
    }
}
