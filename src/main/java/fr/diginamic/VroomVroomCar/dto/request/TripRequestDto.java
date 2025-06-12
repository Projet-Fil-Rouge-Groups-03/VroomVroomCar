package fr.diginamic.VroomVroomCar.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class TripRequestDto {

    private Integer id;

    @NotNull
    private Date dateDebut;
    @NotNull
    private Date dateFin;

    @NotNull
    private LocalTime heureDepart;
    // heureArrivee supprimée car calculée

    @NotBlank
    private String lieuDepart;
    @NotBlank
    private String lieuArrivee;

    @NotBlank
    private String villeDepart;
    @NotBlank
    private String villeArrivee;

    @Min(0)
    private int nbPlacesRestantes;

    @NotNull
    private Integer organisateurId;

    @NotNull
    private Integer carId;


}
