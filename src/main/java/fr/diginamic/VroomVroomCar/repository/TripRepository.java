package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité Trip.
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    @Override
    Trip save(Trip trip);

    @Override
    List<Trip> findAll();

    @Override
    Optional<Trip> findById(Integer id);

    @Override
    void deleteById(Integer id);

    @Override
    boolean existsById(Integer id);
}
