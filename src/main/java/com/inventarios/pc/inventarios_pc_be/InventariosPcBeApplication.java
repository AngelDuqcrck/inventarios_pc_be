package com.inventarios.pc.inventarios_pc_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InventariosPcBeApplication {

	public static void main(String[] args) {	
		SpringApplication.run(InventariosPcBeApplication.class, args);
	}

}
