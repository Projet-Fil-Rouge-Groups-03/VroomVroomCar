package fr.diginamic.VroomVroomCar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CarApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CarApiService carApiService;

    private AdemeApiResponse mockResponse;
    private AdemeRecord record;
    private AdemeFields fields;

    @BeforeEach
    void setUp() {
        // Injection des propriétés de configuration via ReflectionTestUtils
        ReflectionTestUtils.setField(carApiService, "ademeApiUrl",
                "https://public.opendatasoft.com/api/records/1.0/search/");
        ReflectionTestUtils.setField(carApiService, "ademeDataset",
                "vehicules-commercialises");

        // Configuration commune des objets mock
        mockResponse = new AdemeApiResponse();
        record = new AdemeRecord();
        fields = new AdemeFields();
        record.setFields(fields);
    }

    @Test
    void testGetCO2FromApi_withValidData() {
        // Configuration du mock avec données valides
        fields.setCo2(123.0);
        mockResponse.setRecords(List.of(record));

        // Mock de l'appel REST - URL complète avec paramètres
        when(restTemplate.getForEntity(
                contains("vehicules-commercialises"),
                eq(AdemeApiResponse.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        // Exécution du test
        Optional<Double> co2 = carApiService.getCO2FromApi("Peugeot", "208", Motorisation.ESSENCE);

        // Vérifications
        assertTrue(co2.isPresent());
        assertEquals(123.0, co2.get());

        // Vérification que l'appel REST a bien été fait avec les bons paramètres
        verify(restTemplate, times(1)).getForEntity(
                argThat((String url) ->
                        url.contains("Peugeot") &&
                        url.contains("208") &&
                        url.contains("essence") &&
                        url.contains("vehicules-commercialises")),
                eq(AdemeApiResponse.class)
        );
    }

    @Test
    void testGetCO2FromApi_withNoData() {
        // Configuration du mock avec liste vide
        mockResponse.setRecords(Collections.emptyList());

        when(restTemplate.getForEntity(
                contains("vehicules-commercialises"),
                eq(AdemeApiResponse.class)
        )).thenReturn(ResponseEntity.ok(mockResponse));

        // Exécution du test
        Optional<Double> co2 = carApiService.getCO2FromApi("Marque", "Modele", Motorisation.ESSENCE);

        // Vérifications
        assertFalse(co2.isPresent());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(AdemeApiResponse.class));
    }

    @Test
    void testGetCO2FromApi_withApiError() {
        // Test du comportement en cas d'erreur API
        when(restTemplate.getForEntity(anyString(), eq(AdemeApiResponse.class)))
                .thenThrow(new RuntimeException("API Error"));

        // Exécution du test
        Optional<Double> co2 = carApiService.getCO2FromApi("Marque", "Modele", Motorisation.ESSENCE);

        // Vérification que la méthode gère l'erreur gracieusement
        assertTrue(co2.isEmpty());
    }

    @Test
    void testGetCO2FromApi_withNullCO2() {
        // Test avec CO2 null dans la réponse
        fields.setCo2(null);
        mockResponse.setRecords(List.of(record));

        when(restTemplate.getForEntity(anyString(), eq(AdemeApiResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        Optional<Double> co2 = carApiService.getCO2FromApi("Marque", "Modele", Motorisation.ESSENCE);

        assertTrue(co2.isEmpty());
    }

    @Test
    void testGetCO2FromApi_withZeroCO2() {
        // Test avec CO2 = 0 (considéré comme invalide)
        fields.setCo2(0.0);
        mockResponse.setRecords(List.of(record));

        when(restTemplate.getForEntity(anyString(), eq(AdemeApiResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        Optional<Double> co2 = carApiService.getCO2FromApi("Marque", "Modele", Motorisation.ESSENCE);

        assertTrue(co2.isEmpty());
    }

    @Test
    void testUpdatePollutionFromApi_withValidCar() {
        // Préparation
        Car car = new Car();
        car.setMarque("Peugeot");
        car.setModele("308");
        car.setMotorisation(Motorisation.ESSENCE);

        // Configuration du mock
        fields.setCo2(135.5);
        mockResponse.setRecords(List.of(record));

        when(restTemplate.getForEntity(anyString(), eq(AdemeApiResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Exécution
        boolean updated = carApiService.updatePollutionFromApi(car);

        // Vérifications
        assertTrue(updated);
        assertEquals("135.5", car.getPollution());
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(AdemeApiResponse.class));
    }

    @Test
    void testUpdatePollutionFromApi_withExistingPollution() {
        // Test avec pollution déjà présente (ne doit pas écraser)
        Car car = new Car();
        car.setMarque("Peugeot");
        car.setModele("308");
        car.setMotorisation(Motorisation.ESSENCE);
        car.setPollution("100.0"); // Valeur existante

        // Exécution
        boolean updated = carApiService.updatePollutionFromApi(car);

        // Vérifications
        assertTrue(updated);
        assertEquals("100.0", car.getPollution()); // Valeur inchangée

        // Aucun appel API ne doit être fait
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testUpdatePollutionFromApi_withApiFailureUsesDefault() {
        // Test avec échec de l'API -> utilise valeur par défaut
        Car car = new Car();
        car.setMarque("Peugeot");
        car.setModele("308");
        car.setMotorisation(Motorisation.ESSENCE);

        when(restTemplate.getForEntity(anyString(), eq(AdemeApiResponse.class)))
                .thenThrow(new RuntimeException("API Error"));

        // Exécution
        boolean updated = carApiService.updatePollutionFromApi(car);

        // Vérifications - devrait utiliser la valeur par défaut pour ESSENCE (120.0)
        assertTrue(updated);
        assertEquals("120.0", car.getPollution());
    }

    @Test
    void testUpdatePollutionFromApi_withHybridMotorisation() {
        // Test des valeurs par défaut pour HYBRIDE
        Car car = new Car();
        car.setMarque("Toyota");
        car.setModele("Prius");
        car.setMotorisation(Motorisation.HYBRIDE);

        // Mock avec liste vide (pas de données trouvées)
        mockResponse.setRecords(Collections.emptyList());
        when(restTemplate.getForEntity(anyString(), eq(AdemeApiResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Exécution
        boolean updated = carApiService.updatePollutionFromApi(car);

        // Vérifications - devrait utiliser la valeur par défaut pour HYBRIDE (80.0)
        assertTrue(updated);
        assertEquals("80.0", car.getPollution());
    }

    @Test
    void testUpdatePollutionFromApi_withElectricMotorisation() {
        // Test des valeurs par défaut pour ELECTRIQUE
        Car car = new Car();
        car.setMarque("Tesla");
        car.setModele("Model 3");
        car.setMotorisation(Motorisation.ELECTRIQUE);

        // Mock avec liste vide
        mockResponse.setRecords(Collections.emptyList());
        when(restTemplate.getForEntity(anyString(), eq(AdemeApiResponse.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Exécution
        boolean updated = carApiService.updatePollutionFromApi(car);

        // Vérifications - devrait utiliser la valeur par défaut pour ELECTRIQUE (0.0)
        assertTrue(updated);
        assertEquals("0.0", car.getPollution());
    }

    @Test
    void testUpdatePollutionFromApi_withNullCar() {
        // Test avec voiture null
        boolean updated = carApiService.updatePollutionFromApi(null);
        assertFalse(updated);

        // Vérification qu'aucun appel REST n'a été fait
        verifyNoInteractions(restTemplate);
    }

    @Test
    void testUpdatePollutionFromApi_withNullMarque() {
        // Test avec marque null
        Car car = new Car();
        car.setMarque(null);
        car.setModele("308");
        car.setMotorisation(Motorisation.ESSENCE);

        boolean updated = carApiService.updatePollutionFromApi(car);
        assertFalse(updated);

        verifyNoInteractions(restTemplate);
    }

    @Test
    void testUpdatePollutionFromApi_withNullModele() {
        // Test avec modèle null
        Car car = new Car();
        car.setMarque("Peugeot");
        car.setModele(null);
        car.setMotorisation(Motorisation.ESSENCE);

        boolean updated = carApiService.updatePollutionFromApi(car);
        assertFalse(updated);

        verifyNoInteractions(restTemplate);
    }
}