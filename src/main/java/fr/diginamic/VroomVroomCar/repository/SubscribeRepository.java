package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Subscribe;
import fr.diginamic.VroomVroomCar.entity.SubscribeKey;
import fr.diginamic.VroomVroomCar.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
/**
 * Cette interface est un dépôt Spring Data JPA pour l'entité Subscribe.
 * Elle fournit des méthodes pour effectuer des opérations de base de données sur les entités Subscribe.
 */
public interface SubscribeRepository extends JpaRepository<Subscribe, SubscribeKey> {

    /**
     * Récupère la liste des inscriptions liées à un voyage spécifique.
     *
     * @param tripId l'identifiant du trajet
     * @return la liste des inscriptions correspondantes
     */
    public List<Subscribe> findByTripId(Integer tripId);

    /**
     * Récupère la liste des inscriptions liées à un utilisateur spécifique.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste des inscriptions correspondantes
     */
    public List<Subscribe> findByUserId(Integer userId);
    int countByTrip_Id(Integer tripId);

    /**
     * Récupère toutes les inscriptions liées à un trajet spécifique.
     *
     * @param trip L'entité {@link Trip} représentant le trajet concerné.
     * @return Une liste d'objets {@link Subscribe} correspondant aux utilisateurs inscrits à ce trajet.
     *
     */
    List<Subscribe> findByTrip(Trip trip);

}
