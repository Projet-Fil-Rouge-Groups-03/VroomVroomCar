package fr.diginamic.VroomVroomCar.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utilitaire pour la gestion et le formatage des dates.
 * Cette classe fournit des méthodes pour formater les dates selon différents formats.
 */
public class DateUtil {

    /**
     * Formate une date en une chaîne de caractères selon le format français (jour/mois/année).
     *
     * @param date La date à formater.
     * @return Une chaîne de caractères représentant la date au format "dd/MM/yyyy".
     */
    public static String formatToFrench(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
