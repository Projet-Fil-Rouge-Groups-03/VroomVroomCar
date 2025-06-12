package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité Trip.
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {


    boolean existsById(Integer id);

    // Recherche par critères géographiques
    List<Trip> findByVilleDepartAndVilleArrivee(String villeDepart, String villeArrivee);

    // Trajets disponibles (avec places)
    List<Trip> findByNbPlacesRestantesGreaterThan(int nbPlaces);

    // Trajets d'un organisateur
    List<Trip> findByOrganisateurId(Integer organisateurId);

    // Trajets futurs
    @Query("SELECT t FROM Trip t WHERE t.dateDebut >= CURRENT_DATE ORDER BY t.dateDebut")
    List<Trip> findUpcomingTrips();

}
