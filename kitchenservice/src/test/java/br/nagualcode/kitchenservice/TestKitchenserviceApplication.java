package br.nagualcode.kitchenservice;

import org.springframework.boot.SpringApplication;

public class TestKitchenserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(KitchenserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
