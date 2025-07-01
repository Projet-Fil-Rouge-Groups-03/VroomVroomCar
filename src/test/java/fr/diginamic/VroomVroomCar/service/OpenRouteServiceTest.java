package fr.diginamic.VroomVroomCar.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

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
    void testGetTravelDurationInSeconds() throws FunctionnalException {
        // Mock géocodage (appelé 2 fois)
        String mockGeocodeResponseStart = "{\"features\":[{\"geometry\":{\"coordinates\":[2.3522,48.8566]}}]}";
        String mockGeocodeResponseEnd = "{\"features\":[{\"geometry\":{\"coordinates\":[4.8357,45.7640]}}]}";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockGeocodeResponseStart))
                .thenReturn(ResponseEntity.ok(mockGeocodeResponseEnd));
        String mockRouteResponse = "{\"routes\":[{\"summary\":{\"duration\":3600.0}}]}";
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockRouteResponse));

        double duration = openRouteService.getTravelDurationInSeconds("10 rue de la paix, Paris", "1 place Bellecour, Lyon");

        assertEquals(3600, duration, 0.001);

        verify(restTemplate, times(2)).exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class));
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(String.class));
    }
}
