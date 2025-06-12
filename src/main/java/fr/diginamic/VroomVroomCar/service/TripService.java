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
    @Transactional
    public List<Trip> findAllTrip(){
        return tripRepository.findAll();
    }
    @Transactional
    public Optional<Trip> findTripById(Integer id){
        return tripRepository.findById(id);
    }

    //Update Trip
    @Transactional
    public void editTrip(int idTrip, Trip tripEdit) throws FunctionnalException {
        Trip tripBDD = tripRepository.findById(idTrip)
                .orElseThrow(() -> new FunctionnalException("Le voyage avec l'ID " + idTrip + " n'existe pas."));

        tripBDD.setDateDebut(tripEdit.getDateDebut());
        tripBDD.setDateFin(tripEdit.getDateFin());

        tripBDD.setHeureDepart(tripEdit.getHeureDepart());
        tripBDD.setHeureArrivee(tripEdit.getHeureArrivee());

        tripBDD.setLieuDepart(tripEdit.getLieuDepart());
        tripBDD.setLieuArrivee(tripEdit.getLieuArrivee());

        tripBDD.setVilleDepart(tripEdit.getVilleDepart());
        tripBDD.setVilleArrivee(tripEdit.getVilleArrivee());

        tripBDD.setNbPlacesRestantes(tripEdit.getNbPlacesRestantes());

        tripBDD.setOrganisateur(tripEdit.getOrganisateur());
        tripBDD.setCar(tripEdit.getCar());
        tripBDD.setSubscribes(tripEdit.getSubscribes());

        tripRepository.save(tripBDD);
    }

    //Delete Trip
    @Transactional
    public void deleteTrip(Integer id){
        boolean exist = tripRepository.existsById(id);
        if (exist){
            tripRepository.deleteById(id);
        }
    }

}
