package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository JPA pour l'entité Reservation.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /**
     * Recherche paginée des réservations associées à un véhicule spécifique.
     *
     * @param carId l'identifiant unique du véhicule
     * @param pageable l'objet Pageable définissant les paramètres de pagination (page, taille, tri, etc.)
     * @return une page de réservations Page correspondant au véhicule spécifié
     */
    Page<Reservation> findByCompanyCar_Id(Integer carId, Pageable pageable);


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
    boolean existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(Integer carId, Integer userId, Date dateDebut, Date dateFin);

    List<Reservation> findByCompanyCarAndDateDebutAfter(CompanyCar companyCar, Date date);
    List<Reservation> findByUserId(Integer userId);

    /**
     * Trouve les réservations futures ou en cours pour un utilisateur donné,
     * triées par date de début croissante.
     * @param userId L'ID de l'utilisateur.
     * @return Une liste de réservations.
     */
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.dateFin >= CURRENT_DATE ORDER BY r.dateDebut ASC")
    List<Reservation> findUpcomingByUserId(@Param("userId") Integer userId);

    /**
     * Trouve les réservations passées pour un utilisateur donné,
     * triées par date de début décroissante (les plus récentes d'abord).
     * @param userId L'ID de l'utilisateur.
     * @return Une liste de réservations.
     */
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.dateFin < CURRENT_DATE ORDER BY r.dateDebut DESC")
    List<Reservation> findPastByUserId(@Param("userId") Integer userId);
}
