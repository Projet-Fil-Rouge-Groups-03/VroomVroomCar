package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

/**
 * Repository JPA pour l'entité Reservation.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /**
     * Vérifie si l'utilisateur a déjà une réservation pour cette voiture sur ce trajet.
     * Utilisé pour éviter les réservations multiples et calculer les places restantes.
     *
     * @param carId l'identifiant de la voiture
     * @param userId l'identifiant de l'utilisateur
     * @param dateDebut la date de début du trajet
     * @param dateFin la date de fin du trajet
     * @return vrai si l'utilisateur a déjà réservé, sinon faux
     */
    boolean existsByCar_IdAndUser_IdAndDateDebutAndDateFin(Integer carId, Integer userId, Date dateDebut, Date dateFin);
}
