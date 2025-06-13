package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "réservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "date_début")
    private Date dateDebut;
    @NotNull
    @Column(name = "date_fin")
    private Date dateFin;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Reservation(Date dateDebut, Date dateFin, User user, Car car) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.user = user;
        this.car = car;
    }
}
