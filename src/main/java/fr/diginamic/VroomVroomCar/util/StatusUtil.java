package fr.diginamic.VroomVroomCar.util;

import fr.diginamic.VroomVroomCar.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class StatusUtil {
    public String convertStatusToString(Status status) {
        switch (status) {
            case ROLE_ACTIF:
                return "actif";
            case ROLE_ADMIN:
                return "admin";
            case ROLE_BANNI:
                return "banni";
            default:
                return "inconnu";
        }
    }
}
