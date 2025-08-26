package com.sinosoft.common.filter;

import com.sinosoft.common.utils.http.HttpHelper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 构建可重复读取inputStream的request
 *
 *
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper
{
    private byte[] body;

    public RepeatedlyRequestWrapper(HttpServletRequest request, ServletResponse response) throws IOException
    {
        super(request);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String bodyString = HttpHelper.getBodyString(request);
        body = bodyString.getBytes("UTF-8");
    }

    @Override
    public BufferedReader getReader() throws IOException
    {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream()
        {
            @Override
            public int read() throws IOException
            {
                return bais.read();
            }

            @Override
            public int available() throws IOException
            {
                return body.length;
            }

            @Override
            public boolean isFinished()
            {
                return false;
            }

            @Override
            public boolean isReady()
            {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener)
            {

            }
        };
    }

    public String getBody() throws UnsupportedEncodingException {
        return new String(body, "UTF-8");
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setBody(String body) throws IOException {
        this.body = body.getBytes("UTF-8");
    }
}
