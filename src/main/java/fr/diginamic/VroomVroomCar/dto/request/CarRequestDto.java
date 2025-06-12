package fr.diginamic.VroomVroomCar.dto.request;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les requêtes relatives à une voiture.
 * Elle est utilisée pour transférer les données de requête relatives à une voiture entre les couches de l'application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRequestDto {

    /**
     * La marque de la voiture.
     * Ce champ ne peut pas être vide.
     */
    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    /**
     * Le modèle de la voiture.
     * Ce champ ne peut pas être vide.
     */
    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    /**
     * Le nombre de places de la voiture.
     * Ce nombre doit être compris entre 1 et 10.
     */
    @Min(value = 1, message = "Le nombre de places doit être au minimum de 1")
    @Max(value = 10, message = "Le nombre de places doit être au maximum de 10")
    private int nbDePlaces;

    /**
     * Le niveau de pollution de la voiture.
     */
    private String pollution;

    /**
     * Informations supplémentaires sur la voiture.
     */
    private String infosSupp;

    /**
     * L'identifiant de l'utilisateur associé à cette voiture.
     * Ce champ ne peut pas être null.
     */
    @NotNull(message = "L'utilisateur est obligatoire")
    private Integer utilisateurId;

    /**
     * La motorisation de la voiture.
     */
    private Motorisation motorisation;

    /**
     * La catégorie de la voiture.
     */
    private Categorie categorie;
}