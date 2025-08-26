package com.sinosoft.common.utils.doc;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class AbstractDocTractor {
    protected InputStream inputStream;

    public AbstractDocTractor(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void lineByLine(Consumer<String> action) throws IOException {

    }
}
