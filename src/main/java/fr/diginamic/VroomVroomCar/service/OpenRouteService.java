package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.util.TimeTravelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenRouteService {

    private static final String API_KEY = "5b3ce3597851110001cf62484a02071cca65432897165ca1ad37c3bb";
    private static final String GEOCODE_URL = "https://api.openrouteservice.org/geocode/search";
    private static final String ROUTE_URL = "https://api.openrouteservice.org/v2/directions/driving-car";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TimeTravelUtil timeTravelUtil;

    // Géocodage d'une adresse
    public double[] getCoordinatesFromAddress(String adresse) {
        String url = GEOCODE_URL + "?api_key=" + API_KEY + "&text=" + adresse;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            JsonNode coordinates = objectMapper.readTree(response.getBody())
                    .path("features").get(0)
                    .path("geometry")
                    .path("coordinates");

            double lon = coordinates.get(0).asDouble();
            double lat = coordinates.get(1).asDouble();
            return new double[]{lon, lat};
        } catch (Exception e) {
            throw new RuntimeException("Erreur de parsing JSON pour le géocodage", e);
        }
    }


    public double getTravelDurationInSeconds(String fromAddress, String toAddress) {
        double[] start = getCoordinatesFromAddress(fromAddress);
        double[] end = getCoordinatesFromAddress(toAddress);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("coordinates", List.of(start, end));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(ROUTE_URL, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root
                    .path("features")
                    .get(0)
                    .path("properties")
                    .path("summary")
                    .path("duration")
                    .asDouble();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing de la durée", e);
        }
    }

}

