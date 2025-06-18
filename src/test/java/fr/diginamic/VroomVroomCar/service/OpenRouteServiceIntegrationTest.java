package fr.diginamic.VroomVroomCar.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpenRouteServiceIntegrationTest {
    @Value("${openroute.api.key}")
    private String API_KEY;


    @Test
    public void testApiKeyValidity() {
        String testUrl = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=Paris";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(testUrl, HttpMethod.GET, entity, String.class);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Clé API valide !");
            } else {
                System.out.println("Clé API invalide ou problème de configuration");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
