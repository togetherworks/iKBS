package com.ncs.ikbs.util;

import java.io.InputStream;

public class NoImageStream {
    
    private static final String IMAGE_PATH = "noImage.gif";

    public static InputStream getNoImageStream() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    InputStream inputStream = classLoader.getResourceAsStream(IMAGE_PATH);
	    
	    return inputStream;
    }
}
