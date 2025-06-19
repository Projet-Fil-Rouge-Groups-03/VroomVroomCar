package fr.diginamic.VroomVroomCar.dto.response;

import fr.diginamic.VroomVroomCar.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les réponses relatives à un utilisateur.
 * Elle est utilisée pour transférer les données de réponse relatives à un utilisateur entre les couches de l'application.
 */
@Schema(description = "Cette classe représente un DTO (Data Transfer Object) pour les réponses relatives à un utilisateur. Elle est utilisée pour transférer les données de réponse relatives à un utilisateur entre les couches de l'application.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    /**
     *  L'identifiant de l'utilisateur.
     */
    @Schema(description = "L'identifiant de l'utilisateur.")
    private Integer id;

    /**
     *  Le nom de l'utilisateur.
     */
    @Schema(description = "Le nom de l'utilisateur.")
    private String nom;

    /**
     *  Le prénom de l'utilisateur.
     */
    @Schema(description = "Le prénom de l'utilisateur.")
    private String prenom;

    /**
     *  Le mail de l'utilisateur.
     */
    @Schema(description = "Le mail de l'utilisateur.")
    private String mail;

    /**
     *  L'adresse de l'utilisateur.
     */
    @Schema(description = "L'adresse de l'utilisateur.")
    private String libelle;

    private String codePostal;

    private String ville;

    /**
     *  Le status de l'utilisateur.
     */
    @Schema(description = "Le status de l'utilisateur.")
    private String status;

}
