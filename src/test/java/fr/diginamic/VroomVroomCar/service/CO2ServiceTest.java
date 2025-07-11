package fr.diginamic.VroomVroomCar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import fr.diginamic.VroomVroomCar.entity.Trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class CO2ServiceTest {

    private final CO2Service co2Service = new CO2Service();

    @Test
    void testExtractCO2FromPollution_withValidString() {
        Double co2 = co2Service.extractCO2FromPollution("130");
        assertNotNull(co2);
        assertEquals(130.0, co2, 0.1);
    }

    @Test
    void testExtractCO2FromPollution_withInvalidString() {
        Double co2 = co2Service.extractCO2FromPollution("abc");
        assertNull(co2);
    }

    @Test
    void testGetCo2Km_withValidPollutionValue() {
        Car car = new Car();
        car.setPollution("123");
        double co2 = co2Service.getCo2Km(car);
        assertEquals(123.0, co2, 0.1);
    }

    @Test
    void testGetCo2Km_withNullPollution_useDefault() {
        Car car = new Car();
        car.setPollution(null);
        car.setMotorisation(Motorisation.ESSENCE);
        double expectedDefault = fr.diginamic.VroomVroomCar.util.CO2Util.getDefaultCO2ForMotorisation(Motorisation.ESSENCE);
        double co2 = co2Service.getCo2Km(car);
        assertEquals(expectedDefault, co2, 0.1);
    }

    @Test
    void testCalculerCo2Voiture() {
        Car car = new Car();
        car.setPollution("140");
        double result = co2Service.calculateCarCO2(car, 10);
        assertEquals(1400.0, result, 0.1);
    }

    @Test
    void testCalculerCo2Voiture_zeroDistance() {
        Car car = new Car();
        car.setPollution("140");
        double result = co2Service.calculateCarCO2(car, 0);
        assertEquals(0.0, result, 0.1);
    }

    @Test
    void testCalculerCo2Voiture_nullCar() {
        double result = co2Service.calculateCarCO2(null, 10);
        assertEquals(0.0, result, 0.1);
    }

}