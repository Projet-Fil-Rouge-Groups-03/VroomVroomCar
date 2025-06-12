package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
class SubscribeKey implements Serializable {
    @Column(name = "utilisateur_id")
    Integer userId;
    @Column(name = "trajet_id")
    Integer tripId;

    public SubscribeKey(Integer userid, Integer tripid) {
        this.userId = userid;
        this.tripId = tripid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }
}
