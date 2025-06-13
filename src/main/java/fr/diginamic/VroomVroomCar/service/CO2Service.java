package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.util.CO2Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CO2Service {

    /**
     * Extrait la valeur CO2/km depuis le champ pollution
     * @param pollution Le champ pollution (String contenant la valeur CO2)
     * @return La valeur CO2 en g/km ou null si non parsable
     */
    public Double extractCO2FromPollution(String pollution) {
        return CO2Util.parseDoubleSafe(pollution);
    }

    /**
     * Récupère les émissions CO2 par km d'une voiture
     * Priorité : pollution spécifique > valeur par défaut motorisation
     * @param car La voiture
     * @return CO2 par km en grammes
     */
    public double getCo2Km(Car car) {
        if (car == null) {
            return 0.0;
        }

        // Valeur spécifique dans pollution
        Double carCO2 = extractCO2FromPollution(car.getPollution());
        if (carCO2 != null && carCO2 > 0) {
            return carCO2;
        }

        // Valeur par défaut de la motorisation
        return CO2Util.getDefaultCO2ForMotorisation(car.getMotorisation());
    }


    // Calculs en fonction de la distance :

    /**
     * Calcule les émissions CO2 d'une voiture pour une distance donnée
     * @param car La voiture
     * @param distanceKm Distance en kilomètres
     * @return Émissions CO2 en grammes
     */
    public double calculateCarCO2(Car car, double distanceKm) {
        if (car == null || distanceKm <= 0) {
            return 0.0;
        }

        double co2ParKm = getCo2Km(car);
        return co2ParKm * distanceKm;
    }


    // Méthode future pour intégrer OpenStreetMap
    public double calculerCo2TrajetAvecOSM(Car car, Trip trip) {
        // TODO: Intégrer avec OpenStreetMap pour calculer la distance réelle
        // double distance = openStreetMapService.calculerDistance(trip.getDepart(), trip.getArrivee());
        // return calculateCarCO2(car, distance);
        throw new UnsupportedOperationException("Intégration OSM à venir");
    }

    /**
     * Récupère la distance d'un trip (placeholder)
     * @param trip Le trajet
     * @return Distance en km
     */
    private double getDistanceFromTrip(Trip trip) {
        // TODO: Implémenter selon votre modèle Trip
        // return trip.getDistance(); // Si vous avez ce champ
        return 100.0; // Distance fictive pour l'exemple
    }
}