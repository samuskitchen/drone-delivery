package com.s4n.dronedelivery;

import com.s4n.dronedelivery.controller.DomicileController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DroneDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DroneDeliveryApplication.class, args);
	}

	@Bean(initMethod = "readFiles")
	public DomicileController domicileController (){
		return new DomicileController();
	}
}