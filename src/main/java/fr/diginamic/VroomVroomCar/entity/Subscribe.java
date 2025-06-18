package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inscription")
public class Subscribe {
    @EmbeddedId
    SubscribeKey id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "utilisateur_id")
    private User user;

    @ManyToOne
    @MapsId("tripId")
    @JoinColumn(name = "trajet_id")
    private Trip trip;

    @Column(name = "date_inscription")
    private Date dateInscription;

    public Subscribe(User user, Trip trip, Date dateInscription) {
        this.user = user;
        this.trip = trip;
        this.dateInscription = dateInscription;
    }

    @PrePersist
    protected void onCreate() {
        dateInscription = new Date();
    }
}