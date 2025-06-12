package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.TripMapper;
import fr.diginamic.VroomVroomCar.repository.TripRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private ValidationUtil validationUtil;

    /**
     * Crée un nouveau trajet.
     */
    @Transactional
    public TripResponseDto createTrip(TripRequestDto tripRequestDto) throws FunctionnalException {
        // Validation des dates
        validationUtil.validateEndDateBeforeStartDate(tripRequestDto.getDateDebut(), tripRequestDto.getDateFin());

        Trip trip = tripMapper.toEntity(tripRequestDto);
        // Calcul de l'heure d'arrivée
        trip.setHeureArrivee(calculateArrivalTime(trip.getHeureDepart(), trip.getLieuDepart(),
                trip.getLieuArrivee(), trip.getVilleDepart(), trip.getVilleArrivee()));

        Trip savedTrip = tripRepository.save(trip);

        return tripMapper.toResponse(savedTrip);
    }


    /**
     * Récupère tous les trajets.
     */
    @Transactional
    public List<TripResponseDto> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream()
                .map(tripMapper::toResponse)
                .collect(Collectors.toList());
    }
    /**
     * Récupère un trajet par son ID.
     */
    @Transactional
    public TripResponseDto getTripById(Integer id) throws FunctionnalException {
        // Vérification existence
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas."));

        return tripMapper.toResponse(trip);
    }

    /**
     * Met à jour un trajet existant.
     */
    @Transactional
    public TripResponseDto updateTrip(Integer id, TripRequestDto tripRequestDto) throws FunctionnalException {
        // Vérification existence
        Trip existingTrip = tripRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas."));

        // Validation des dates
        if (tripRequestDto.getDateDebut() != null && tripRequestDto.getDateFin() != null) {
            validationUtil.validateEndDateBeforeStartDate(tripRequestDto.getDateDebut(), tripRequestDto.getDateFin());
        }

        // Mise à jour des données
        tripMapper.updateEntity(existingTrip, tripRequestDto);
        // Recalcul de l'heure d'arrivée si nécessaire
        if (tripRequestDto.getHeureDepart() != null) {
            existingTrip.setHeureArrivee(calculateArrivalTime(
                    existingTrip.getHeureDepart(),
                    existingTrip.getLieuDepart(),
                    existingTrip.getLieuArrivee(),
                    existingTrip.getVilleDepart(),
                    existingTrip.getVilleArrivee()
            ));
        }

        Trip updatedTrip = tripRepository.save(existingTrip);
        return tripMapper.toResponse(updatedTrip);
    }

    /**
     * Supprime un trajet.
     */
    @Transactional
    public void deleteTrip(Integer id) throws FunctionnalException {
        if (!tripRepository.existsById(id)) {
            throw new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas.");
        }
        tripRepository.deleteById(id);
    }

    /**
     * Calcule l'heure d'arrivée estimée
     */
    private LocalTime calculateArrivalTime(LocalTime heureDepart, String lieuDepart, String lieuArrivee, String villeDepart, String villeArrivee) {
        // TODO: Implémenter la logique de calcul (API Google Maps, durée fixe, etc.)
        return heureDepart.plusHours(2);
    }


}
