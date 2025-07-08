package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * Service pour interagir avec l'API OpenRouteService avec cache Caffeine.
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

    @Lazy // Briser la dépendance circulaire
    @Autowired
    private OpenRouteService self;

    /**
     * Géocode une adresse en coordonnées GPS (longitude, latitude).
     * Cache pendant 24h car les coordonnées ne changent pas.
     *
     * @param adresse l'adresse complète à géocoder (exemple : "10 rue de la paix, Paris")
     * @return un tableau de deux doubles : [longitude, latitude]
     */
    @Cacheable(value = "coordinates", key = "#adresse")
    public double[] getCoordinatesFromAddress(String adresse) {
        String url = GEOCODE_URL + "?api_key=" + API_KEY + "&text=" + adresse;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

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
     * Cache pendant 6h car les routes peuvent changer.
     *
     * @param adresseDepart adresse de départ
     * @param adresseArrivee   adresse d’arrivée
     * @return la réponse JSON complète de l'itinéraire
     * @throws RuntimeException si la requête échoue ou le parsing JSON échoue
     */
    @Cacheable(value = "routes", key = "#adresseDepart + '_' + #adresseArrivee")
    public JsonNode getRouteResponse(String adresseDepart, String adresseArrivee) {
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
     * Utilise le cache automatiquement via getRouteResponse.
     *
     * @param adresseDepart adresse de départ complète (ex : "10 rue de la paix, Paris")
     * @param adresseArrivee   adresse d'arrivée complète (ex : "1 place Bellecour, Lyon")
     * @return la durée estimée du trajet en secondes
     */
    public double getTravelDurationInSeconds(String adresseDepart, String adresseArrivee) throws FunctionnalException {
        JsonNode root = self.getRouteResponse(adresseDepart, adresseArrivee);
        if (root.has("error")) {
            String errorMessage = root.path("error").path("message").asText("Erreur inconnue de l'API de routage.");
            throw new FunctionnalException("Impossible de calculer l'itinéraire : " + errorMessage);
        }

        JsonNode durationNode = root.path("routes").path(0).path("summary").path("duration");
        if (durationNode.isMissingNode()) {
            throw new FunctionnalException("La durée du trajet est manquante dans la réponse de l'API. Réponse reçue: " + root.toPrettyString());
        }

        return durationNode.asDouble();
    }

    /**
     * Récupère la distance du trajet entre deux adresses, en kilomètres.
     * Utilise le cache automatiquement via getRouteResponse.
     *
     * @param adresseDepart adresse de départ (ex. : "10 rue de la paix, Paris")
     * @param adresseArrivee   adresse d'arrivée (ex. : "1 place Bellecour, Lyon")
     * @return la distance en kilomètres (double)
     * @throws FunctionnalException si la distance ne peut pas être calculée
     */
    public double getTravelDistanceInKilometers(String adresseDepart, String adresseArrivee) throws FunctionnalException {
        JsonNode root = self.getRouteResponse(adresseDepart, adresseArrivee);
        if (root.has("error")) {
            String errorMessage = root.path("error").path("message").asText("Erreur inconnue de l'API de routage.");
            throw new FunctionnalException("Impossible de calculer la distance : " + errorMessage);
        }

        JsonNode distanceNode = root.path("routes").path(0).path("summary").path("distance");
        if (distanceNode.isMissingNode()) {
            throw new FunctionnalException("La distance du trajet est manquante dans la réponse de l'API. Réponse reçue: " + root.toPrettyString());
        }

        return distanceNode.asDouble() / 1000.0; // Conversion en kilomètres
    }

    /**
     * Vide le cache des routes pour éviter le stock
     * d'informations obsolètes.
     */
    @CacheEvict(value = "routes", allEntries = true)
    public void clearRoutesCache() {
        System.out.println("Cache des routes vidé");
    }

    /**
     * Vide le cache des coordonnées pour éviter le stock
     * d'informations obsolètes.
     */
    @CacheEvict(value = "coordinates", allEntries = true)
    public void clearCoordinatesCache() {
        System.out.println("Cache des coordonnées vidé");
    }

    /**
     * Vide tous les caches
     */
    @CacheEvict(value = {"routes", "coordinates"}, allEntries = true)
    public void clearAllCaches() {
        System.out.println("Tous les caches vidés");
    }

}

