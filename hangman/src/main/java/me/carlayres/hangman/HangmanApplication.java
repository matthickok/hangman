package me.carlayres.hangman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HangmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(HangmanApplication.class, args);
	}

}
