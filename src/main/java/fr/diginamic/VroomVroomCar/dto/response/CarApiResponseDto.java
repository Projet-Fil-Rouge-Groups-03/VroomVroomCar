package fr.diginamic.VroomVroomCar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarApiResponseDto {
    private String marque;
    private String modele;
    private String carburant;
    private Double co2; // g/km
    private Double consommation;
    private String version;
}
