package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
     * Recherche tous les trajets correspondant à une ville de départ et une ville d'arrivée spécifiques.
     *
     * @param villeDepart  La ville de départ
     * @param villeArrivee La ville d'arrivée
     * @return Liste des trajets correspondant aux villes spécifiées
     */
    List<Trip> findByVilleDepartAndVilleArrivee(String villeDepart, String villeArrivee);

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

