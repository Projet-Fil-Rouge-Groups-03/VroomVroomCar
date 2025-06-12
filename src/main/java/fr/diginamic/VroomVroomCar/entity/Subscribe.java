package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
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
}