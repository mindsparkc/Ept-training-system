package com.sinosoft.common.utils;

import com.sinosoft.common.utils.doc.AbstractDocTractor;
import com.sinosoft.common.utils.doc.DocTractor;
import com.sinosoft.common.utils.doc.DocxTractor;
import org.apache.poi.poifs.filesystem.FileMagic;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Word 文档解析的工具类
 * 函数列表：
 *  -
 */
public class DocTractorUtils {

    /**
     * 逐行处理 Word 文件
     */
    public static void lineByLine(String filePath, Consumer<String> action)
            throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            // 获取 doc、docx 的处理类
            AbstractDocTractor tractor = docTractor(inputStream);
            if (null == tractor) {
                throw new RuntimeException("文件格式错误，请检查后重新输入！");
            }
            tractor.lineByLine(action);
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 根据传入的文件路径的格式，返回文件的处理类
     * @param inputStream doc 或 docx 文件
     * @return
     */
    private static AbstractDocTractor docTractor(InputStream inputStream) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedInputStream.mark(bufferedInputStream.available()+1);
            FileMagic fileMagic = FileMagic.valueOf(bufferedInputStream);
            bufferedInputStream.reset();
            if (fileMagic == FileMagic.OLE2) {
                return new DocTractor(bufferedInputStream);
            }else if (fileMagic == FileMagic.OOXML) {
                return new DocxTractor(bufferedInputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
