package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * Repository JPA pour l'entité Trip.
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

    /**
     * Vérifie l'existence d'un trajet à partir de son identifiant.
     *
     * @param id L'identifiant du trajet
     * @return true si un trajet avec cet ID existe, sinon false
     */
    boolean existsById(Integer id);

    /**
     * Recherche les trajets correspondant aux filtres spécifiés. Tous les paramètres sont optionnels :
     * si un paramètre est null (ou "TOUS" pour le type de véhicule), il est ignoré dans les critères de recherche.
     *
     * La recherche prend en compte les critères suivants :
     *   villeDepart : la ville de départ du trajet (exacte).
     *   villeArrivee : la ville d'arrivée du trajet (exacte).
     *   dateDebut : la date de début minimale du trajet.
     *   heureDepart : l'heure de départ minimale du trajet.
     *   vehiculeType :
     *       "VOITURE_SERVICE" : seuls les trajets avec une voiture de service sont inclus.
     *       "VOITURE_COVOIT" : seuls les trajets sans voiture de service sont inclus.
     *       "TOUS" : tous les trajets sont inclus, quel que soit le type de véhicule.
     *
     * @param villeDepart la ville de départ à filtrer (peut être {@code null})
     * @param villeArrivee la ville d'arrivée à filtrer (peut être {@code null})
     * @param dateDebut la date de début minimale du trajet (peut être {@code null})
     * @param heureDepart l'heure de départ minimale du trajet (peut être {@code null})
     * @param vehiculeType le type de véhicule à filtrer : "VOITURE_SERVICE", "VOITURE_COVOIT" ou "TOUS"
     * @return une liste de trajets correspondant aux filtres appliqués
     */
    @Query("SELECT t FROM Trip t LEFT JOIN CompanyCar cc ON (t.car IS NOT NULL AND t.car.id = cc.id) " +
            "WHERE (:villeDepart IS NULL OR t.villeDepart = :villeDepart) " +
            "AND (:villeArrivee IS NULL OR t.villeArrivee = :villeArrivee) " +
            "AND (:dateDebut IS NULL OR t.dateDebut >= :dateDebut) " +
            "AND (:heureDepart IS NULL OR t.heureDepart >= :heureDepart) " +
            "AND (:vehiculeType = 'TOUS' OR " +
            "     (:vehiculeType = 'VOITURE_SERVICE' AND cc.id IS NOT NULL) OR " +
            "     (:vehiculeType = 'VOITURE_COVOIT' AND cc.id IS NULL))")
    List<Trip> findTripsWithFilters(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("dateDebut") Date dateDebut,
            @Param("heureDepart") LocalTime heureDepart,
            @Param("vehiculeType") String vehiculeType
    );

    /**
     * Récupère la liste des trajets à venir pour un utilisateur donné.
     * Un trajet est considéré comme à venir si :
     * - la date de fin est null et la date de début est aujourd'hui ou plus tard
     * - ou la date de fin est aujourd'hui ou plus tard
     *
     * L'utilisateur peut être :
     * - organisateur du trajet
     * - ou inscrit au trajet via une souscription
     *
     * @param userId l'identifiant de l'utilisateur (organisateur ou passager)
     * @return une liste de trajets futurs, triés par date de début et heure de départ croissantes
     */
    @Query("SELECT DISTINCT t FROM Trip t " +
            "LEFT JOIN Subscribe s ON s.trip.id = t.id " +
            "WHERE (t.organisateur.id = :userId OR s.user.id = :userId) " +
            "AND (t.dateFin IS NULL AND t.dateDebut >= CURRENT_DATE OR t.dateFin >= CURRENT_DATE) " +
            "ORDER BY t.dateDebut ASC, t.heureDepart ASC")
    List<Trip> findUpcomingUserTrips(@Param("userId") Integer userId);
    /**
     * Récupère la liste des trajets passés pour un utilisateur donné.
     * Un trajet est considéré comme passé si :
     * - la date de fin est définie et antérieure à aujourd'hui
     * - ou si la date de fin est null et que la date de début est antérieure à aujourd'hui
     *
     * L'utilisateur peut être :
     * - organisateur du trajet
     * - ou inscrit au trajet via une souscription
     *
     * @param userId l'identifiant de l'utilisateur (organisateur ou passager)
     * @return une liste de trajets passés, triés par date de début et heure de départ décroissantes
     */
    @Query("SELECT DISTINCT t FROM Trip t " +
            "LEFT JOIN Subscribe s ON s.trip.id = t.id " +
            "WHERE (t.organisateur.id = :userId OR s.user.id = :userId) " +
            "AND (t.dateFin IS NOT NULL AND t.dateFin < CURRENT_DATE OR t.dateFin IS NULL AND t.dateDebut < CURRENT_DATE) " +
            "ORDER BY t.dateDebut DESC, t.heureDepart DESC")
    List<Trip> findPastUserTrips(@Param("userId") Integer userId);

    /**
     * Récupère tous les trajets dont la date de début est aujourd’hui ou dans le futur.
     *
     * @return Liste des trajets à venir, triés par date de début croissante
     */
    @Query("SELECT t FROM Trip t WHERE t.dateDebut >= CURRENT_DATE ORDER BY t.dateDebut")
    List<Trip> findUpcomingTrips();


}

