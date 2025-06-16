package fr.diginamic.VroomVroomCar.util;

import fr.diginamic.VroomVroomCar.entity.Trip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String formatToFrench(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

}
