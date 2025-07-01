package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.entity.CompanyCarStatus;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.repository.CompanyCarRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CompanyCarServiceIntegrationTest {

    @Autowired
    private CompanyCarService companyCarService;

    @Autowired
    private CompanyCarRepository companyCarRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testDeleteCar() throws ResourceNotFoundException {
        // Arrange
        User user = new User();
        user.setMail("test@test.com");
        user.setNom("Test");
        user.setPrenom("User");
        user = userRepository.saveAndFlush(user);

        CompanyCar car = new CompanyCar();
        car.setImmatriculation("TEST123");
        car.setMotorisation(Motorisation.HYBRIDE);
        car.setStatus(CompanyCarStatus.EN_SERVICE);
        car.setModele("Clio");
        car.setMarque("Renault");
        car.setNbDePlaces(5);
        car.setUser(user);

        car.setTrips(new HashSet<>());
        car.setReservations(new HashSet<>());

        car = companyCarRepository.saveAndFlush(car);
        Integer carId = car.getId();

        System.out.println("Car ID généré: " + carId);
        assertNotNull(carId, "Car ID should not be null");
        assertTrue(companyCarRepository.existsById(carId), "Car should exist after save");

        companyCarService.deleteCar(carId);

        assertFalse(companyCarRepository.existsById(carId), "Car should not exist after deletion");
    }
}
