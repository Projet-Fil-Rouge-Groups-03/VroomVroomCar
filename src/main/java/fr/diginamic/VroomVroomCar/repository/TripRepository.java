package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    Optional<Trip> findById(int id);
}
