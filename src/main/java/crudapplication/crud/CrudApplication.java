package crudapplication.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;

/**
 * CrudApplication Class that is used to launch a Spring application from Java main method.
 */
@Log4j2
@SpringBootApplication
public class CrudApplication {
	public static void main(String[] args) {
		log.info("Application has started :: main() is running...");
		SpringApplication.run(CrudApplication.class, args);
	}
}
