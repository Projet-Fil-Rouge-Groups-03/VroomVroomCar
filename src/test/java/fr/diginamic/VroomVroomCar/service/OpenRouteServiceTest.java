package fr.diginamic.VroomVroomCar.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenRouteServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenRouteService openRouteService;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OpenRouteService selfMock;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(openRouteService, "API_KEY", "clé-fictive");
        ReflectionTestUtils.setField(openRouteService, "self", selfMock);
    }

    @Test
    void testGetCoordinatesFromAddress() {
        String mockResponse = "{\"features\":[{\"geometry\":{\"coordinates\":[2.3522,48.8566]}}]}";

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        double[] coordinates = openRouteService.getCoordinatesFromAddress("10 rue de la paix, Paris");

        assertEquals(2.3522, coordinates[0], 0.001);
        assertEquals(48.8566, coordinates[1], 0.001);
    }

    @Test
    void testGetTravelDurationInSeconds() throws Exception {
        String routeJson = """
            {
              "routes": [{
                "summary": {
                  "duration": 3600.0
                }
              }]
            }
        """;
        JsonNode mockedJson = objectMapper.readTree(routeJson);
        when(selfMock.getRouteResponse(anyString(), anyString())).thenReturn(mockedJson);
        double duration = openRouteService.getTravelDurationInSeconds(
                "10 rue de la paix, Paris", "1 place Bellecour, Lyon"
        );

        assertEquals(3600.0, duration, 0.001);
    }

    @Test
    void testGetTravelDistanceInKilometers() throws Exception {
        String routeJson = """
            {
              "routes": [{
                "summary": {
                  "distance": 500000.0
                }
              }]
            }
        """;
        JsonNode mockedJson = objectMapper.readTree(routeJson);
        when(selfMock.getRouteResponse(anyString(), anyString())).thenReturn(mockedJson);
        double distance = openRouteService.getTravelDistanceInKilometers(
                "10 rue de la paix, Paris", "1 place Bellecour, Lyon"
        );
        assertEquals(500.0, distance, 0.001);
    }
}
