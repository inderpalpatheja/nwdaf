package com.nwdaf.AMF;
/*
 * @created 26/02/2020 - 11:29 AM
 * @project EventSubscription
 * @author  sheetalkumar
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;


public class ApplicationPropertiesValue {

    String result = "";
    InputStream inputStream;

    public String getPropValues() throws IOException {

        try {
            Properties prop = new Properties();
            String propFileName = "application.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            String subList = prop.getProperty("spring.subscriber.count");
            // String company1 = prop.getProperty("company1");
            //String company2 = prop.getProperty("company2");
            //String company3 = prop.getProperty("company3");


            result = subList;

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }
}
