package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CarApiService {
    private final RestTemplate restTemplate;

    @Value("${app.api.ademe.url:https://public.opendatasoft.com/api/records/1.0/search/}")
    private String ademeApiUrl;

    @Value("${app.api.ademe.dataset:vehicules-commercialises}")
    private String ademeDataset;

    public CarApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Récupère les données CO2 d'un véhicule via l'API ADEME
     * @param marque La marque du véhicule
     * @param modele Le modèle du véhicule
     * @param motorisation La motorisation (pour filtrer)
     * @return Les émissions CO2 en g/km ou null si non trouvé
     */
    public Optional<Double> getCO2FromApi(String marque, String modele, Motorisation motorisation) {
        try {
            String query = buildRequestAPI(marque, modele, motorisation);
            String url = UriComponentsBuilder.fromUriString(ademeApiUrl)
                    .queryParam("dataset", ademeDataset)
                    .queryParam("q", query)
                    .queryParam("rows", 1)
                    .queryParam("sort", "-co2_g_km") // Prendre le plus récent/fiable
                    .build()
                    .toUriString();

            log.debug("Appel API ADEME pour {} {} : {}", marque, modele, url);

            ResponseEntity<AdemeApiResponse> response = restTemplate.getForEntity(url, AdemeApiResponse.class);

            if (response.getBody() != null &&
                    response.getBody().getRecords() != null &&
                    !response.getBody().getRecords().isEmpty()) {

                AdemeRecord record = response.getBody().getRecords().get(0);
                Double co2 = record.getFields().getCo2();

                if (co2 != null && co2 > 0) {
                    log.info("Données CO2 trouvées pour {} {} : {} g/km", marque, modele, co2);
                    return Optional.of(co2);
                }
            }

            log.info("Aucune donnée CO2 trouvée pour {} {}", marque, modele);
            return Optional.empty();

        } catch (Exception e) {
            log.warn("Erreur lors de la récupération des données CO2 pour {} {} : {}",
                    marque, modele, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Met à jour le champ pollution d'une voiture avec les données API
     * @param car La voiture à mettre à jour
     * @return true si mise à jour réussie, false sinon
     */
    public boolean updatePollutionFromApi(Car car) {
        if (car == null || car.getMarque() == null || car.getModele() == null) {
            return false;
        }

        // Ne pas écraser une valeur existante valide
        if (car.getPollution() != null && !car.getPollution().trim().isEmpty() &&
                !car.getPollution().equals("0")) {
            log.debug("Valeur pollution déjà présente pour {} {} : {}",
                    car.getMarque(), car.getModele(), car.getPollution());
            return true;
        }

        Optional<Double> co2Api = getCO2FromApi(car.getMarque(), car.getModele(), car.getMotorisation());

        if (co2Api.isPresent()) {
            car.setPollution(co2Api.get().toString());
            return true;
        }

        // Fallback : utiliser les valeurs par défaut de la motorisation
        if (car.getMotorisation() != null) {
            double defaultCo2 = getDefaultCO2ForMotorisation(car.getMotorisation());
            car.setPollution(String.valueOf(defaultCo2));
            log.info("Utilisation valeur par défaut pour {} {} {} : {} g/km",
                    car.getMarque(), car.getModele(), car.getMotorisation(), defaultCo2);
            return true;
        }

        return false;
    }

    private String buildRequestAPI(String marque, String modele, Motorisation motorisation) {
        StringBuilder query = new StringBuilder();
        query.append(marque.trim()).append(" ").append(modele.trim());

        // Mapping enum vers termes API
        if (motorisation != null) {
            switch (motorisation) {
                case ESSENCE -> query.append(" essence");
                case HYBRIDE -> query.append(" hybride");
                case ELECTRIQUE -> query.append(" electrique");
            }
        }

        return query.toString();
    }

    private double getDefaultCO2ForMotorisation(Motorisation motorisation) {
        return switch (motorisation) {
            case ESSENCE -> 120.0;
            case HYBRIDE -> 80.0;
            case ELECTRIQUE -> 0.0;
        };
    }
}

// Classes pour désérialisation JSON ADEME
@Data
class AdemeApiResponse {
    private List<AdemeRecord> records;
}

@Data
class AdemeRecord {
    private AdemeFields fields;
}

@Data
class AdemeFields {
    private String marque;
    private String modele;
    private String carburant;

    @JsonProperty("co2_g_km")
    private Double co2;

    private Double consommation;
    private String version;
}
