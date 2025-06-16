package fr.diginamic.VroomVroomCar.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les réponses relatives à une notification.
 * Elle est utilisée pour transférer les données de réponse relatives à une notification entre les couches de l'application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO pour les réponses relatives à une notification.")
public class NotificationResponseDto {
    private Integer id;

    @Schema(description = "Le contenu de la notification", example = "Mazier Didier a annulé sa participation au covoit du 06/07/25")
    private String contenu;

    @Schema(description = "Le nom de la notification", example = "Annulation")
    private String nom;

    private Date date;

    @Schema(description = "L'identifiant de l'utilisateur associé à cette notification", example = "1", required = true)
    private Integer userId;

    private String userName;
}
