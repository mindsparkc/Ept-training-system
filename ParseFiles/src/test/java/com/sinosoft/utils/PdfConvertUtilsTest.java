package com.sinosoft.utils;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageNumber;
import com.spire.pdf.graphics.PdfImageType;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

class PdfConvertUtilsTest {

    @Test
    void pdfSplitAndConvertSvg() throws NotFoundException {
        PdfConvertUtils.pdfSplitAndConvertSvg(
                new File("D:/pdf.svg.示例文件/d66d5ece-a92b-4359-b48d-18504b05c86f.pdf"));
    }

    @Test
    void pdf2png() {
        File file = new File("D:/pdf.svg.示例文件/d66d5ece-a92b-4359-b48d-18504b05c86f.pdf");
        try {
            PDDocument pdDocument = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(pdDocument);

            int pageCount = pdDocument.getNumberOfPages();
            for (int i=0; i<pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 144);
                ImageIO.write(image, "png", new File("D:/pdf.svg.示例文件/d66d5ece-a92b-4359-b48d-18504b05c86f_"+i+".png"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void word2svg() {
        Document document = new Document("D:/pdf.svg.示例文件/定制化开发.docx");

        document.saveToFile("D:/pdf.svg.示例文件/定制化开发.svg", FileFormat.SVG);
    }

    @Test
    void pdf2svg() throws IOException {
        PDDocument pdDocument = PDDocument.load(new File("D:/pdf.svg.示例文件/d66d5ece-a92b-4359-b48d-18504b05c86f.pdf"));
        Splitter splitter = new Splitter();
        List<PDDocument> pdDocuments = splitter.split(pdDocument);
        int i=0;
        for (PDDocument document :pdDocuments) {
            i++;
            document.save("D:/pdf.svg.示例文件/d66d5ece-a92b-4359-b48d-18504b05c86f/PDFBOX-" + i + ".pdf");
        }

//        PdfDocument pdfDocument = new PdfDocument("D:/pdf.svg.示例文件/d66d5ece-a92b-4359-b48d-18504b05c86f.pdf");
//        pdfDocument.saveToFile("D:/pdf.svg.示例文件/d66d5ece-a92b-4359-b48d-18504b05c86f/1.svg", com.spire.pdf.FileFormat.SVG);
    }
}