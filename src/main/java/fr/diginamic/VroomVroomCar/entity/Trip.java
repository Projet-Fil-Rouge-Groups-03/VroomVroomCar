package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trajet")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date dateDebut;
    private Date dateFin;

    private LocalDate heureDepart;
    private LocalDate heureArrivee;

    private String lieuDepart;
    private String lieuArrivee;

    private String villeDepart;
    private String villeArrivee;

    private int nbPlacesRestantes;

    @ManyToOne
    @JoinColumn(name = "organisateur_id")
    private User organisateur;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToMany
    @JoinTable(
            name = "subscribe",
            joinColumns = @JoinColumn(name = "trajet_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> user = new HashSet<>();

}
