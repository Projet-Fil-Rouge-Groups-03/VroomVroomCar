package fr.diginamic.VroomVroomCar.util;

public class TimeTravelUtil {

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
