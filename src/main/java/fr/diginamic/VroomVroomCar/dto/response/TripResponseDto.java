package fr.diginamic.VroomVroomCar.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TripResponseDto {

        private Integer id;

        private Date dateDebut;
        private Date dateFin;

        private LocalTime heureDepart;
        private LocalTime heureArrivee;

        private String lieuDepart;
        private String lieuArrivee;

        private String villeDepart;
        private String villeArrivee;

        private int nbPlacesRestantes;

        private Integer organisateurId;

        private Integer carId;
}
