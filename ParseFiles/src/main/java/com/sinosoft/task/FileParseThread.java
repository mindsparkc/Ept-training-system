package com.sinosoft.task;

import com.sinosoft.utils.PdfConvertUtils;
import org.apache.ibatis.javassist.NotFoundException;

import java.io.File;
import java.util.concurrent.Callable;

public class FileParseThread implements Runnable {
    private String filePath;
    private CallbackAndMetadata callable;

    public FileParseThread(String filePath, CallbackAndMetadata callable) {
        this.filePath = filePath;
        this.callable = callable;
    }

    @Override
    public void run() {
        System.out.println("开始处理文件" + filePath + ":" + Thread.currentThread().getId());

        String step = "9";
        String metadata = null;
        try {
            metadata = fileParse();
            System.out.println(filePath + "：Metadata：" + metadata);
        }catch (NotFoundException e) {
            step = "3"; // 未找到文件
            e.printStackTrace();
        } catch (RuntimeException e) {
            step = "8"; // 格式不正确，无需处理
            e.printStackTrace();
        } finally {
            try {
                callable.call(metadata, step);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String fileParse() throws NotFoundException {
        return PdfConvertUtils.pdfSplitAndConvertSvg(new File(filePath));
    }

    public interface CallbackAndMetadata {
        void call(String args, String step);
    }
}
