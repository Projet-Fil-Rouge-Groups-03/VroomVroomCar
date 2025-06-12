package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.repository.TripRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    //Create Trip
    @Transactional
    public void insertTrip(Trip trip) throws FunctionnalException {
        boolean exist = tripRepository.existsById(trip.getId());
        if (!exist){
            tripRepository.save(trip);
        } else {
            throw new FunctionnalException("La ville existe déjà sous l'identifiant " + trip.getId());
        }
    }

    //Read Trip

    //Update Trip

    //Delete Trip

}
