package com.sinosoft.uploadfile.utils;

import com.sinosoft.common.utils.ServletUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class MinioUtilTest {

    @Test
    void uploadFile() throws IOException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
        String url = "";

        try {
            File file = new File("D:\\BaiduSyncdisk\\视频\\B站-Siri的英语星球\\【秒杀单词2】破解单词密码!像串珍珠一样记单词!零基础可学!假期一起来狂记5000词!以一抵十，轻松背单词！专升本假期突击！_哔哩哔哩_bilibili.mp4");
            // 上传文件
            System.out.println(MinioUtil.getFileExt(file.getName()));
            minioClient.putObject(PutObjectArgs.builder()
                            .contentType(MinioUtil.getFileExt(file.getName()))
                    .bucket("test")
                    .object(file.getName())
                    .stream(new FileInputStream(file), file.length(), -1)
                    .build());
            // 获取文件预览 URL
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket("test")
                    .object(file.getName())
                    .method(Method.GET)
                    .build());
            url = url.substring(0, url.indexOf('?'));
            System.out.println(url);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }
        System.out.println(ServletUtils.urlEncode(url));
    }

//    @Test
//    public void getFileExtTest() {
//        String fileExt = MinioUtil.getFileExt("【秒杀单词2】破解单词密码!像串珍珠一样记单词!零基础可学!假期一起来狂记5000词!以一抵十，轻松背单词！专升本假期突击！_哔哩哔哩_bilibili.mp4");
//        System.out.println(fileExt);
//    }
}
