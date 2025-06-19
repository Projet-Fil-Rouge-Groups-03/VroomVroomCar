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
     * Recherche les trajets ayant un nombre de places restantes supérieur à un seuil donné.
     *
     * @param nbPlaces Seuil minimum de places restantes
     * @return Liste des trajets disponibles avec des places restantes
     */
    List<Trip> findByNbPlacesRestantesGreaterThan(int nbPlaces);

    /**
     * Récupère tous les trajets organisés par un utilisateur spécifique.
     *
     * @param organisateurId L'identifiant de l'organisateur
     * @return Liste des trajets organisés par cet utilisateur
     */
    List<Trip> findByOrganisateurId(Integer organisateurId);

    /**
     * Récupère tous les trajets dont la date de début est aujourd’hui ou dans le futur.
     *
     * @return Liste des trajets à venir, triés par date de début croissante
     */
    @Query("SELECT t FROM Trip t WHERE t.dateDebut >= CURRENT_DATE ORDER BY t.dateDebut")
    List<Trip> findUpcomingTrips();


}

