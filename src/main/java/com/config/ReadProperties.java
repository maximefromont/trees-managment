package com.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

    public static String getConfig(String key) {

        try (InputStream input = new FileInputStream("src/main/java/com/config/config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Le fichier config.properties est introuvable.");
                return null;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            return prop.getProperty(key);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
