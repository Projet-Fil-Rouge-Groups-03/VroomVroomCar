package fr.diginamic.VroomVroomCar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * Service pour interagir avec l'API OpenRouteService.
 *
 * Fournit des méthodes pour géocoder une adresse en coordonnées GPS
 * et calculer la durée de trajet entre deux adresses.
 *
 */
@Service
@RequiredArgsConstructor
public class OpenRouteService {

    @Value("${openroute.api.key}")
    private String API_KEY;
    private static final String GEOCODE_URL = "https://api.openrouteservice.org/geocode/search";
    private static final String ROUTE_URL = "https://api.openrouteservice.org/v2/directions/driving-car";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Géocode une adresse en coordonnées GPS (longitude, latitude).
     *
     * @param adresse l'adresse complète à géocoder (exemple : "10 rue de la paix, Paris")
     * @return un tableau de deux doubles : [longitude, latitude]
     */
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

    /**
     * Effectue un appel à l'API OpenRouteService Directions et retourne la réponse JSON.
     *
     * @param adresseDepart adresse de départ
     * @param adresseArrivee   adresse d’arrivée
     * @return la réponse JSON complète de l'itinéraire
     * @throws RuntimeException si la requête échoue ou le parsing JSON échoue
     */
    private JsonNode getRouteResponse(String adresseDepart, String adresseArrivee) {
        double[] start = getCoordinatesFromAddress(adresseDepart);
        double[] end = getCoordinatesFromAddress(adresseArrivee);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("coordinates", List.of(start, end));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(ROUTE_URL, request, String.class);

        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing de la réponse JSON", e);
        }
    }

    /**
     * Calcule la durée estimée du trajet (en secondes) entre deux adresses.
     *
     * @param adresseDepart adresse de départ complète (ex : "10 rue de la paix, Paris")
     * @param adresseArrivee   adresse d'arrivée complète (ex : "1 place Bellecour, Lyon")
     * @return la durée estimée du trajet en secondes
     */
    public double getTravelDurationInSeconds(String adresseDepart, String adresseArrivee) {
        JsonNode root = getRouteResponse(adresseDepart, adresseArrivee);
        return root.path("features")
                .get(0)
                .path("properties")
                .path("summary")
                .path("duration")
                .asDouble();
    }

    /**
     * Récupère la distance du trajet entre deux adresses, en kilomètres.
     *
     * @param adresseDepart adresse de départ (ex. : "10 rue de la paix, Paris")
     * @param adresseArrivee   adresse d'arrivée (ex. : "1 place Bellecour, Lyon")
     * @return la distance en kilomètres (double)
     */
    public double getTravelDistanceInKilometers(String adresseDepart, String adresseArrivee) {
        JsonNode root = getRouteResponse(adresseDepart, adresseArrivee);
        double distanceInMeters = root.path("features")
                .get(0)
                .path("properties")
                .path("summary")
                .path("distance")
                .asDouble();
        return distanceInMeters / 1000.0;
    }

}

