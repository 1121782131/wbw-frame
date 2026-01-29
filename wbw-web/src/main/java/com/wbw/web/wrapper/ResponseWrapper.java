package com.wbw.web.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 响应包装器，用于记录响应体
 */
public class ResponseWrapper extends HttpServletResponseWrapper {
    
    private final ByteArrayOutputStream outputStream;
    private PrintWriter writer;
    
    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        this.outputStream = new ByteArrayOutputStream();
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }
            
            @Override
            public void setWriteListener(WriteListener writeListener) {
                // do nothing
            }
            
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
            
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                outputStream.write(b, off, len);
            }
            
            @Override
            public void write(byte[] b) throws IOException {
                outputStream.write(b);
            }
        };
    }
    
    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(getOutputStream(), true);
        }
        return writer;
    }
    
    public byte[] getContentAsBytes() {
        if (writer != null) {
            writer.flush();
        }
        return outputStream.toByteArray();
    }
    
    public String getContentAsString() throws UnsupportedEncodingException {
        return new String(getContentAsBytes(), getCharacterEncoding());
    }
}