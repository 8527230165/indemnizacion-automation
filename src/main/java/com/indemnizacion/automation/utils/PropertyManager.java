package com.indemnizacion.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
	

    private static Properties properties = new Properties();

    private static void loadProperties() {
        try {
        	File file = new File(System.getProperty("user.dir") , "config.properties");
            properties.load(new FileInputStream(file));

        } catch (IOException e) {
            System.out.println("Configuration properties file cannot be found");
        }
    }

    public static String getConfigValueByKey(String key) {
        loadProperties();
        return properties.getProperty(key);
    }
    
  


}
