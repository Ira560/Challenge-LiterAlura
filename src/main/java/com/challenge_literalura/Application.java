package com.challenge_literalura;

import com.challenge_literalura.principal.Principal;
import com.challenge_literalura.repository.IAutorRepository;
import com.challenge_literalura.repository.ILibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	IAutorRepository autorRepository;
	@Autowired
	ILibroRepository libroRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception{

		System.out.println("Hola mundo");
		Principal principal = new Principal(libroRepository,autorRepository);
		principal.menu();

	}

}


