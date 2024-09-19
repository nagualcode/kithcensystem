package br.nagualcode.menuservice;

import org.springframework.boot.SpringApplication;

public class TestMenuserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(MenuserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
