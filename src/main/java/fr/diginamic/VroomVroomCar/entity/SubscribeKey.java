package fr.diginamic.VroomVroomCar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SubscribeKey implements Serializable {
    @Column(name = "utilisateur_id")
    Integer userId;
    @Column(name = "trajet_id")
    Integer tripId;

}
