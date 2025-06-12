package fr.diginamic.VroomVroomCar.dto.response;

import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.Motorisation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Cette classe représente un DTO (Data Transfer Object) pour les réponses relatives à une voiture.
 * Elle est utilisée pour transférer les données de réponse relatives à une voiture entre les couches de l'application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarResponseDto {

    /**
     * L'identifiant unique de la voiture.
     */
    private Integer id;

    /**
     * La marque de la voiture.
     */
    private String marque;

    /**
     * Le modèle de la voiture.
     */
    private String modele;

    /**
     * Le nombre de places de la voiture.
     */
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
     */
    private Integer utilisateurId;

    /**
     * Le nom de l'utilisateur associé à cette voiture.
     */
    private String utilisateurNom;

    /**
     * La motorisation de la voiture.
     */
    private Motorisation motorisation;

    /**
     * La catégorie de la voiture.
     */
    private Categorie categorie;
}
