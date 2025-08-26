package com.sinosoft.common.utils.doc;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class DocTractor extends AbstractDocTractor {

    public DocTractor(InputStream inputStream) {
        super(inputStream);
    }

    @Override
    public void lineByLine(Consumer<String> action) throws IOException {
        HWPFDocument hwpfDocument = new HWPFDocument(inputStream);
        Range range = hwpfDocument.getRange();
        int numParagraphs = range.numParagraphs();
        for (int i=0; i<numParagraphs; i++) {
            Paragraph paragraph = range.getParagraph(i);
            action.accept(paragraph.text());
        }
    }
}
