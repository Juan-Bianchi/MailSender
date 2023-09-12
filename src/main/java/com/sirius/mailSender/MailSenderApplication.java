package com.sirius.mailSender;

import com.sirius.mailSender.models.Role;
import com.sirius.mailSender.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class MailSenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailSenderApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(RoleRepository roleRepository) {
		return (args) -> {

			Role role = new Role("ADMIN");
			Role role1 = new Role("USER");

			roleRepository.save(role);
			roleRepository.save(role1);
		};

	}
}
