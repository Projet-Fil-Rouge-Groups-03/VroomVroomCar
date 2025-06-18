package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;
import java.util.Set;

/**
 * Entité représentant un trajet dans le système.
 *
 * Cette classe contient toutes les informations nécessaires à la définition d’un trajet,
 * y compris les dates, les horaires, les lieux, les participants et le véhicule utilisé.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trajet")
public class Trip {

    /**
     * Identifiant unique du trajet.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Date de début du trajet.
     * Ne peut pas être nulle.
     */
    @NotNull
    private Date dateDebut;
    /**
     * Date de fin du trajet.
     * Ne peut pas être nulle.
     */
    @NotNull
    private Date dateFin;

    /**
     * Heure prévue de départ.
     * Ne peut pas être nulle.
     */
    @NotNull
    private LocalTime heureDepart;
    /**
     * Heure estimée d'arrivée.
     * Elle sera calculée dynamiquement.
     */
    private LocalTime heureArrivee;

    /**
     * Lieu de départ
     * Ne peut pas être vide.
     */
    @NotBlank
    private String lieuDepart;
    /**
     * Lieu d'arrivée
     * Ne peut pas être vide.
     */
    @NotBlank
    private String lieuArrivee;

    /**
     * Ville de départ du trajet.
     * Ne peut pas être vide.
     */
    @NotBlank
    private String villeDepart;
    /**
     * Ville d'arrivée du trajet.
     * Ne peut pas être vide.
     */
    @NotBlank
    private String villeArrivee;

    /**
     * Nombre de places restantes disponibles pour ce trajet.
     * Ne peut pas être négatif, il y aura au minimum 0 places restantes.
     */
    @Min(0)
    private int nbPlacesRestantes;

    /**
     * Utilisateur organisateur du trajet.
     * Relation Many-to-One obligatoire.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private User organisateur;

    /**
     * Véhicule utilisé pour ce trajet.
     * Relation Many-to-One obligatoire.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    /**
     * Ensemble des inscriptions liées à ce trajet.
     * Relation One-to-Many avec l'entité Subscribe.
     * Chargement paresseux et suppression en cascade.
     */
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Subscribe> subscribes;

    /**
     * Constructeur complet.
     */
    public Trip(Date dateDebut, Date dateFin, LocalTime heureDepart, LocalTime heureArrivee,
                String lieuDepart, String lieuArrivee, String villeDepart, String villeArrivee,
                int nbPlacesRestantes, User organisateur, Car car, Set<Subscribe> subscribes) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.lieuDepart = lieuDepart;
        this.lieuArrivee = lieuArrivee;
        this.villeDepart = villeDepart;
        this.villeArrivee = villeArrivee;
        this.nbPlacesRestantes = nbPlacesRestantes;
        this.organisateur = organisateur;
        this.car = car;
        this.subscribes = subscribes;
    }
}
