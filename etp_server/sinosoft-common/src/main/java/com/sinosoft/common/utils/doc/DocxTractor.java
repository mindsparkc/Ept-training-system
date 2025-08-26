package com.sinosoft.common.utils.doc;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.function.Consumer;

public class DocxTractor extends AbstractDocTractor {

    public DocxTractor(InputStream filePath) {
        super(filePath);
    }

    @Override
    public void lineByLine(Consumer<String> action) throws IOException {
        XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
        xwpfDocument.getParagraphs().forEach(xwpfParagraph -> {
            String text = xwpfParagraph.getText();
            BigInteger numID = xwpfParagraph.getNumID();
            String numLevelText = xwpfParagraph.getNumLevelText();
            if (numID!=null && numLevelText != null) {
                action.accept(numID + "." + text);
            }else {
                action.accept(text);
            }

        });
    }
}
