package com.sinosoft.uploadfile;

import com.sinosoft.uploadfile.utils.ContentTypeUtil;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class MinioTest {

    private static String url;
    private static String accessKey;
    private static String secretKey;

    @BeforeAll
    public static void init() {
        url = "http://localhost:9000";
        accessKey = "minioadmin";
        secretKey = "minioadmin";
    }

    @Test
    public void minio() throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        File file = new File("E:\\迅雷下载\\刺杀小说家.2021.HD.1080P.国语中英双字.mkv\\刺杀小说家.2021.HD.1080P.国语中英双字.mp4");
        InputStream inputStream = new FileInputStream(file);

        System.out.println(file.length());

        System.out.println(inputStream.available());
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket("test")
                .object("filename.mp4")
                .stream(inputStream, file.length(), -1)
                .contentType(ContentTypeUtil.getContentType("mp4")).build();
        minioClient.putObject(putObjectArgs);
    }
}
