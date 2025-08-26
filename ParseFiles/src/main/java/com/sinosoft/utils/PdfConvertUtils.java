package com.sinosoft.utils;

import com.alibaba.fastjson.JSONObject;
import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class PdfConvertUtils {
    private static final String SVG_METEDATA_FILE_NAME = "mainSvgList.json";
    /**
     * 负责将 PDF 拆分成单页的 PDF 文件，并存储到以pdf文件命名的文件夹中
     *
     * @param file 要切分的 PDF 文件
     * @return 页码
     */
    public static String pdfSplitAndConvertSvg(File file) throws NotFoundException {
        System.out.println(file.getName().toLowerCase());
        System.out.println(file.getName().toLowerCase().lastIndexOf(".pdf"));
        if (file.getName().toLowerCase().lastIndexOf(".pdf") == -1) {
            throw new RuntimeException("要处理的文件必须是 Pdf 文件，" + file.getAbsolutePath() + "不符合要求！");
        }
        if (!file.isFile()) {
            throw new NotFoundException("文件不存在，" + file.getAbsolutePath() + "不符合要求！");
        }
        String dist = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-4);
        FileSystemUtils.noExistMkdir(dist);

        PDDocument document = null;
        PdfDocument pdfDocument = null;
        List<String> files = new ArrayList<>();
        try {
            document = PDDocument.load(file);
            Splitter splitter = new Splitter();
            List<PDDocument> pdDocuments = splitter.split(document);

            Metadata svgMetadata = new Metadata();
            Metadata pdfMetadata = new Metadata();
            svgMetadata.setSize(pdDocuments.size());
            pdfMetadata.setSize(pdDocuments.size());

            int i=0;
            for (PDDocument pdDocument : pdDocuments) {
                String filename = GenerateShortCodeUtils.generateShortCode(dist + "/" + (i++), 6) + ".pdf";
                // 若文件名重复，则重新生成
                while (pdfMetadata.list.indexOf(filename) >= 0) {
                    filename = GenerateShortCodeUtils.generateShortCode(dist + "/" + (i++), 6) + ".pdf";
                }
                // String filename =  i + ".pdf";
                String distFile = dist + "/" + filename;
                pdDocument.save(distFile);
                pdfMetadata.add(filename);

                COSDocument cosDocument = pdDocument.getDocument();
                cosDocument.close();
                pdDocument.close();

                // 使用 Free Spire 将 PDF 转换为 SVG
                pdfDocument = new PdfDocument();
                pdfDocument.loadFromFile(distFile);
                pdfDocument.saveToFile(distFile+".svg", FileFormat.SVG);
                files.add(distFile+".svg");
                svgMetadata.add(filename+".svg");
                System.out.println("生成文件：" + distFile+".svg");
                pdfDocument.close();

            }

            // 保存元数据信息
            FileWriter writer = new FileWriter(dist + "/" + SVG_METEDATA_FILE_NAME);
            writer.write(JSONObject.toJSONString(svgMetadata));
            writer.close();

            // 清理不在 mainSvgList.json 中的 .pdf 和 .svg 文件
            cleanOthers(svgMetadata, pdfMetadata, dist);

            return JSONObject.toJSONString(svgMetadata);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (document != null) {
                try {
                    // pdfBox 使用以下代码进行 close 释放
                    // 不然会报错 org.apache.pdfbox.cos.COSDocument        : Warning: You did not close a PDF Document
                    COSDocument cos = document.getDocument();
                    cos.close();
                    pdfDocument.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (pdfDocument != null) {
                pdfDocument.close();
            }
        }
    }

    /**
     * 清理不在 mainSvgList.json 中的 .pdf 和 .svg 文件
     */
    private static void cleanOthers(Metadata svgMetadata, Metadata pdfMetadata, String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.walkFileTree(path,
                    new SimpleFileVisitor<Path>(){
                        // 先去遍历删除⽂件
                        @Override
                        public FileVisitResult visitFile(Path file,
                                                         BasicFileAttributes attrs)throws IOException {
                            if (file.getFileName().toString().lastIndexOf(".svg") > 0
                                    ||file.getFileName().toString().lastIndexOf(".pdf") > 0 ) {
                                if (svgMetadata.list.indexOf(file.getFileName().toString()) >= 0
                                        || pdfMetadata.list.indexOf(file.getFileName().toString()) >= 0) {
                                }else {
                                    Files.delete(file);
                                }
                            }
                            return FileVisitResult.CONTINUE;
                        }
//
//                        // 再去遍历删除⽬录
//                        @Override
//                        public FileVisitResult postVisitDirectory(Path dir,
//                                                                  IOException exc)throws IOException {
//                            // Files.delete(dir);
//                            System.out.printf("⽂件夹被删除: %s%n", dir);
//                            return FileVisitResult.CONTINUE;
//                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分页之后的文件的元数据信息
     */
    private static class Metadata {
        // 文件的总页码
        Integer size;
        //
        List<String> list = new ArrayList<>();

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public void add(String arg) {
            if (this.list == null) {
                this.list = new ArrayList<>();
            }

            this.list.add(arg);
        }
    }
}
