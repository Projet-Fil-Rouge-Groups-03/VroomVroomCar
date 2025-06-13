package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

    int countByTripIdAndDateDebutAndDateFin(Integer tripId, Date dateDebut, Date dateFin);
}
