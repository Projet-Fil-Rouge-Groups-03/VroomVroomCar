package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

/**
 * Cette classe représente une entité Voiture dans la base de données.
 * Elle contient les informations relatives à une voiture, telles que la marque,
 * le modèle, le nombre de places, etc.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voiture")
public class Car {

    /**
     * L'identifiant unique de la voiture.
     * Cet identifiant est généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * La marque de la voiture.
     * Ce champ ne peut pas être vide et a une longueur maximale de 30 caractères.
     */
    @NotBlank(message = "La marque est obligatoire")
    @Column(nullable = false, length = 30)
    private String marque;

    /**
     * Le modèle de la voiture.
     * Ce champ ne peut pas être vide et a une longueur maximale de 30 caractères.
     */
    @NotBlank(message = "Le modèle est obligatoire")
    @Column(nullable = false, length = 30)
    private String modele;

    /**
     * Le nombre de places de la voiture.
     * Ce nombre doit être compris entre 1 et 10.
     */
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "nb_places", nullable = false)
    private int nbDePlaces;

    /**
     * Le niveau de pollution de la voiture.
     * Ce champ a une longueur maximale de 30 caractères.
     */
    @Column(length = 30)
    private String pollution;

    /**
     * Informations supplémentaires sur la voiture.
     * Ce champ peut contenir de grandes quantités de texte.
     */
    @Lob
    @Column(name = "infos_supp")
    private String infosSupp;

    /**
     * L'utilisateur associé à cette voiture.
     * Ce champ ne peut pas être null et est chargé de manière paresseuse.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateurId", nullable = false)
    private User user;

    /**
     * La motorisation de la voiture.
     * Ce champ est une énumération stockée sous forme de chaîne de caractères.
     */
    @Enumerated(EnumType.STRING)
    private Motorisation motorisation;

    /**
     * La catégorie de la voiture.
     * Ce champ est une énumération stockée sous forme de chaîne de caractères.
     */
    @Enumerated(EnumType.STRING)
    private Categorie categories;

    /**
     * Les trajets associés à cette voiture.
     * Les trajets sont chargés de manière paresseuse et toutes les opérations de persistance
     * sont cascadées depuis la voiture.
     */
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Trip> trips = new HashSet<>();

    /**
     * Constructeur pour créer une nouvelle instance de Car avec les détails spécifiés.
     *
     * @param marque La marque de la voiture.
     * @param modele Le modèle de la voiture.
     * @param nbDePlaces Le nombre de places de la voiture.
     * @param pollution Le niveau de pollution de la voiture.
     * @param infosSupp Informations supplémentaires sur la voiture.
     * @param user L'utilisateur associé à cette voiture.
     * @param motorisation La motorisation de la voiture.
     * @param categories La catégorie de la voiture.
     * @param trips Les trajets associés à cette voiture.
     */
    public Car(String marque, String modele, int nbDePlaces, String pollution, String infosSupp, User user, Motorisation motorisation, Categorie categories, Set<Trip> trips) {
        this.marque = marque;
        this.modele = modele;
        this.nbDePlaces = nbDePlaces;
        this.pollution = pollution;
        this.infosSupp = infosSupp;
        this.user = user;
        this.motorisation = motorisation;
        this.categories = categories;
        this.trips = trips;
    }
}

