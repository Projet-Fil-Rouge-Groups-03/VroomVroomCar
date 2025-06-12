package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.TripMapper;
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

    @Autowired
    private TripMapper tripMapper;

    /**
     * Crée un nouveau trajet.
     */
    @Transactional
    public TripResponseDto createTrip(TripRequestDto tripRequestDto) throws FunctionnalException {
//        // Validation métier
//        validateTripDates(tripRequestDto.getDateDebut(), tripRequestDto.getDateFin());
//
//        // Conversion DTO -> Entity
//        Trip trip = tripMapper.toEntity(tripRequestDto);
//
//        // Calcul de l'heure d'arrivée (logique métier)
//        trip.setHeureArrivee(calculateArrivalTime(trip.getHeureDepart(), trip.getLieuDepart(), trip.getLieuArrivee()));
//
//        // Sauvegarde
//        Trip savedTrip = tripRepository.save(trip);
//
//        // Conversion Entity -> DTO pour la réponse
//        return tripMapper.toResponseDto(savedTrip);
        return null;
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
