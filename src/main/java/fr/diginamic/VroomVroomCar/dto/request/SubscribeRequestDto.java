package fr.diginamic.VroomVroomCar.dto.request;

import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 /**
 * Cette classe représente un DTO pour les requêtes relatives à une inscription.
 * Elle est utilisée pour transférer les données de requête relatives à une inscription entre les couches de l'application.
 * <p>
 * Contient les identifiants nécessaires pour lier un utilisateur à un voyage,
 * ainsi que la date d'inscription.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Représente une inscription à un trajet.")
public class SubscribeRequestDto {

    /**
     * Identifiant de l'utilisateur souhaitant s'inscrire.
     */
    @Schema(description = "Identifiant de l'utilisateur", required = true)
    private Integer userId;
    /**
     * Identifiant du trajet auquel l'utilisateur veut s'inscrire.
     */
    @Schema(description = "Identifiant du voyage", required = true)
    private Integer tripId;
    /**
     * Date d'inscription de l'utilisateur au trajet.
     */
    @Schema(description = "Date de l'inscription", required = true, type = "string", format = "date")
    private Date dateInscription;
}
