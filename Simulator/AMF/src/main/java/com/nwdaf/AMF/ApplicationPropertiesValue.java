package com.nwdaf.AMF;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class ApplicationPropertiesValue {

    String result = "";
    InputStream inputStream;

    public List<String> getPropValues() throws IOException {

        List<String> propList = new ArrayList<>();

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
            String eventVal = prop.getProperty("spring.event.value");

            // String company1 = prop.getProperty("company1");
            //String company2 = prop.getProperty("company2");
            //String company3 = prop.getProperty("company3");


            propList.add(subList);
            propList.add(eventVal);

            result = subList;

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return propList;
    }
}
