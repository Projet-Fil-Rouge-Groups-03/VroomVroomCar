package fr.diginamic.VroomVroomCar.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO utilisé pour renvoyer les informations de l'inscription
 * d'un utilisateur à un trajet.
 * <p>
 * Ce DTO est retourné par les endpoints de l'API lors de la consultation
 * des abonnements (inscriptions).
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Représente les informations d'une inscription à un trajet.")
public class SubscribeResponseDto {

    /**
     * Informations de l'utilisateur inscrit au trajet.
     */
    @Schema(description = "Utilisateur inscrit", hidden = true)
    private Integer userId;
    /**
     * Informations du trajet auquel l'utilisateur est inscrit.
     */
    @Schema(description = "trajet concerné", hidden = true)
    private Integer tripId;
    /**
     * Date à laquelle l'utilisateur s'est inscrit au voyage.
     */
    @Schema(description = "Date de l'inscription", hidden = true)
    private Date dateInscription;
}
