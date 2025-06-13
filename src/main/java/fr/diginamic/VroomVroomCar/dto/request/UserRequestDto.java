package fr.diginamic.VroomVroomCar.dto.request;

import fr.diginamic.VroomVroomCar.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les requêtes relatives à un utilisateur.
 * Elle est utilisée pour transférer les données de requête relatives à un utilisateur entre les couches de l'application.
 */
@Schema(description = "Cette classe représente un DTO (Data Transfer Object) pour les requêtes relatives à un utilisateur. Elle est utilisée pour transférer les données de requête relatives à un utilisateur entre les couches de l'application.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    /**
     *  Le nom de l'utilisateur.
     *  Ce champ ne peut pas être vide.
     */
    @Schema(description = "Le nom de l'utilisateur.  Ce champ ne peut pas être vide.")
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    /**
     *  Le prénom de l'utilisateur.
     *  Ce champ ne peut pas être vide.
     */
    @Schema(description = "Le prénom de l'utilisateur.  Ce champ ne peut pas être vide.")
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    /**
     *  Le mail de l'utilisateur.
     *  Ce champ ne peut pas être vide.
     */
    @Schema(description = "Le mail de l'utilisateur.  Ce champ ne peut pas être vide.")
    @NotBlank(message = "Le mail est obligatoire")
    private String mail;

    /**
     *  L'adresse de l'utilisateur.
     *  Ce champ ne peut pas être vide.
     */
    @Schema(description = "L'adresse de l'utilisateur.  Ce champ ne peut pas être vide.")
    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    /**
     *  Le mot de passe de l'utilisateur.
     *  Ce champ ne peut pas être vide.
     */
    @Schema(description = "Le mot de passe de l'utilisateur.  Ce champ ne peut pas être vide.")
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    /**
     *  Le status de l'utilisateur.
     *  Ce champ ne peut pas être vide.
     */
    @Schema(description = "Le status de l'utilisateur.  Ce champ ne peut pas être vide.")
    @NotBlank(message = "Le status est obligatoire")
    private Status status;

}
