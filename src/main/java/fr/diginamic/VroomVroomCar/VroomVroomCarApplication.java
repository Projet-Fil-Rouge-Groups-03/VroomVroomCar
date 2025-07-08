package fr.diginamic.VroomVroomCar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VroomVroomCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(VroomVroomCarApplication.class, args);
	}

}
