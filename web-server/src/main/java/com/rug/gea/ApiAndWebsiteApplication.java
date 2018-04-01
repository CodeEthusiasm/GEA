package com.rug.gea;

import com.rug.gea.Controllers.MessageController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class ApiAndWebsiteApplication {
	public static void main(String[] args) throws IOException, TimeoutException {
		MessageController.receiveMessage();
		SpringApplication.run(ApiAndWebsiteApplication.class, args);
	}
}
