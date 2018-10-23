package com.itianyi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 整个类只需要创建一个对象
 * 设计成单例模式
 */
public class MyProperties extends Properties {

    private static MyProperties myProperties;

    private MyProperties() throws IOException {
        InputStream in = MyProperties.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            this.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static MyProperties getInstance() throws IOException {
        if (null == myProperties) {
            myProperties = new MyProperties();
        }
        return myProperties;
    }
}