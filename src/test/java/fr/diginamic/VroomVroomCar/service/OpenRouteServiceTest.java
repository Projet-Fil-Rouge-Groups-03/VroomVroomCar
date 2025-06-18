package fr.diginamic.VroomVroomCar.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenRouteServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OpenRouteService openRouteService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(openRouteService, "API_KEY", "clé-fictive");
        ReflectionTestUtils.setField(openRouteService, "objectMapper", new ObjectMapper());
    }

    @Value("${openroute.api.key}")
    private String API_KEY;

    @Test
    public void testApiKeyValidity() {
        String testUrl = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=Paris";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(testUrl, HttpMethod.GET, entity, String.class);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Clé API valide !");
            } else {
                System.out.println("❌ Clé API invalide ou problème de configuration");
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Test
    void testGetCoordinatesFromAddress() throws Exception {
        String mockResponse = "{\"features\":[{\"geometry\":{\"coordinates\":[2.3522,48.8566]}}]}";
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        double[] coordinates = openRouteService.getCoordinatesFromAddress("10 rue de la paix, Paris");

        assertEquals(2.3522, coordinates[0], 0.001);
        assertEquals(48.8566, coordinates[1], 0.001);
    }

    @Test
    void testGetTravelDurationInSeconds() {
        // Mock géocodage (appelé 2 fois)
        String mockGeocodeResponse = "{\"features\":[{\"geometry\":{\"coordinates\":[2.3522,48.8566]}}]}";
        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockGeocodeResponse));

        String mockRouteResponse = "{\"features\":[{\"properties\":{\"summary\":{\"duration\":3600}}}]}";
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockRouteResponse));

        double duration = openRouteService.getTravelDurationInSeconds("10 rue de la paix, Paris", "1 place Bellecour, Lyon");

        assertEquals(3600, duration, 0.001);

        verify(restTemplate, times(2)).getForEntity(anyString(), eq(String.class));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(String.class));
    }
}
