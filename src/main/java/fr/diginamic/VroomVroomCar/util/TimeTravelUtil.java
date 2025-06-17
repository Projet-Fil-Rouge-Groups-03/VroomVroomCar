package fr.diginamic.VroomVroomCar.util;

import org.springframework.stereotype.Component;

/**
 * Utilitaire pour la manipulation et le formatage de durées de trajet.
 *
 * Fournit une méthode pour convertir une durée exprimée en secondes
 * en une chaîne lisible de type "Xh Ymin", "Xh" ou "Ymin".
 *
 */
@Component
public class TimeTravelUtil {

    /**
     * Formate une durée (en secondes) en une chaîne lisible.
     *
     * Exemples :
     *   3660 → "1h 1min"
     *   3600 → "1h"
     *   720  → "12min"
     *
     * @param durationInSeconds la durée en secondes
     * @return une chaîne de caractères représentant la durée formatée
     */
    public String formatDuration(double durationInSeconds) {
        int totalMinutes = (int) Math.round(durationInSeconds / 60.0);
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        if (hours > 0 && minutes > 0) {
            return String.format("%dh %dmin", hours, minutes);
        } else if (hours > 0) {
            return String.format("%dh", hours);
        } else {
            return String.format("%dmin", minutes);
        }
    }

}

