package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;

/**
 * Entité représentant une réservation d'un véhicule de service par un utilisateur sur une période donnée.
 * Chaque réservation est associée à un utilisateur et à un véhicule spécifique.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "réservation")
@ToString(exclude = {"user", "companyCar"})
@EqualsAndHashCode(of = "id")
public class Reservation {

    /**
     * Identifiant unique de la réservation (généré automatiquement).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Date de début de la réservation.
     * Ne peut pas être nulle.
     */
    @NotNull
    @Column(name = "date_début")
    private Date dateDebut;

    /**
     * Date de fin de la réservation.
     * Ne peut pas être nulle.
     */
    @NotNull
    @Column(name = "date_fin")
    private Date dateFin;

    /**
     * Utilisateur ayant effectué la réservation.
     * Relation Many-to-One : plusieurs réservations peuvent être faites par un même utilisateur.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Véhicule réservé.
     * Relation Many-to-One : plusieurs réservations peuvent concerner un même véhicule.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "voiture_de_service_id")
    private CompanyCar companyCar;

    /**
     * Constructeur personnalisé pour créer une instance de réservation.
     */
    public Reservation(Date dateDebut, Date dateFin, User user, CompanyCar companyCar) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.user = user;
        this.companyCar = companyCar;
    }
}

