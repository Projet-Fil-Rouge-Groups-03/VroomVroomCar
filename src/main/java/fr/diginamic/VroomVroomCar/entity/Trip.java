package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalTime;
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

}
