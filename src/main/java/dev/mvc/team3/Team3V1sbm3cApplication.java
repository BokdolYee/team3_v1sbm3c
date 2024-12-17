package dev.mvc.team3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.mvc"})
public class Team3V1sbm3cApplication {

	public static void main(String[] args) {
		SpringApplication.run(Team3V1sbm3cApplication.class, args);
	}

}
