package com.nwdaf.AMF;
//import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmfApplication {

	private static final Logger logger= LoggerFactory.getLogger(AmfApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(AmfApplication.class, args);

		logger.debug("In AmfApplication class");
	}
}
