package fr.diginamic.VroomVroomCar.util;

import fr.diginamic.VroomVroomCar.entity.Motorisation;

public class CO2Util {
    /**
     * Parse une chaîne en Double de manière sécurisée.
     * @param value La chaîne à parser
     * @return La valeur Double ou null si la chaîne ne peut pas être parsée
     */
    public static Double parseDoubleSafe(String value) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur sur le parsage du CO2");
        }
        return null;
    }

    /**
     * Récupère la valeur par défaut de CO2 pour une motorisation donnée.
     * @param motorisation La motorisation
     * @return La valeur par défaut de CO2 en g/km
     */
    public static double getDefaultCO2ForMotorisation(Motorisation motorisation) {
        if (motorisation == null) {
            return 0.0;
        }
        return switch (motorisation) {
            case ESSENCE -> 120.0;
            case HYBRIDE -> 80.0;
            case ELECTRIQUE -> 0.0;
            default -> 0.0;
        };
    }
}
