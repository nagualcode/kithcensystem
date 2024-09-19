package br.nagualcode.printservice;

import org.springframework.boot.SpringApplication;

public class TestPrintserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(PrintserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
