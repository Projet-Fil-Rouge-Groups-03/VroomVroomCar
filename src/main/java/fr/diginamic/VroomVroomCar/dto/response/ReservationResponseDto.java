package fr.diginamic.VroomVroomCar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * DTO représentant les données d'une réservation renvoyées au client.
 * Contient toutes les informations nécessaires à l'affichage de la réservation.
 */

@Data
@NoArgsConstructor
@Schema(description = "Représente une réservation renvoyé par l'API.")
public class ReservationResponseDto {

    private Integer id;

    @Schema(description = "Date de début de la réservation", example = "2025-07-15")
    private Date dateDebut;
    @Schema(description = "Date de fin de la réservation", example = "2025-07-15")
    private Date dateFin;

    @Schema(description = "Identifiant de l'utilisateur qui réserve le véhicule", example = "2")
    private Integer userId;

    @Schema(description = "Identifiant du véhicule réservé", example = "1")
    private Integer carId;
}
