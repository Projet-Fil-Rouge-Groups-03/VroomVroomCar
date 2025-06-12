package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "trajet")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Date dateDebut;
    @NotNull
    private Date dateFin;

    @NotNull
    private LocalTime heureDepart;
    private LocalTime heureArrivee;

    @NotBlank
    private String lieuDepart;
    @NotBlank
    private String lieuArrivee;

    @NotBlank
    private String villeDepart;
    @NotBlank
    private String villeArrivee;

    @Min(0)
    private int nbPlacesRestantes;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private User organisateur;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(mappedBy = "trip",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Subscribe> subscribes;

    public Trip(Date dateDebut, Date dateFin, LocalTime heureDepart, LocalTime heureArrivee, String lieuDepart, String lieuArrivee, String villeDepart, String villeArrivee, int nbPlacesRestantes, User organisateur, Car car, Set<Subscribe> subscribes) {
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
