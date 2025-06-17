package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
/**
 * Cette interface est un dépôt Spring Data JPA pour l'entité Subscribe.
 * Elle fournit des méthodes pour effectuer des opérations de base de données sur les entités Subscribe.
 */
public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {
    /**
     * Compte le nombre d'inscriptions {@link Subscribe} pour un identifiant de voyage
     * et une plage de dates donnée.
     *
     * @param tripId     l'identifiant du voyage
     * @param dateDebut  la date de début de la période
     * @param dateFin    la date de fin de la période
     * @return le nombre d'abonnements correspondant aux critères
     */
    public int countByTripIdAndDateDebutAndDateFin(Integer tripId, Date dateDebut, Date dateFin);

    /**
     * Récupère la liste des inscriptions liées à un voyage spécifique.
     *
     * @param tripId l'identifiant du trajet
     * @return la liste des inscriptions correspondantes
     */
    public List<Subscribe> findByTripId(int tripId);

    /**
     * Récupère la liste des inscriptions liées à un utilisateur spécifique.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste des inscriptions correspondantes
     */
    public List<Subscribe> findByUserId(int userId);
    int countByTrip_Id(Integer tripId);

}
