package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.*;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.TripMapper;
import fr.diginamic.VroomVroomCar.repository.CarRepository;
import fr.diginamic.VroomVroomCar.repository.ReservationRepository;
import fr.diginamic.VroomVroomCar.repository.SubscribeRepository;
import fr.diginamic.VroomVroomCar.repository.TripRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class TripService implements ITripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;
    private final SubscribeRepository subscribeRepository;

    private final ValidationUtil validationUtil;

    // Create Trip
    @Transactional
    public TripResponseDto createTrip(TripRequestDto tripRequestDto, UserResponseDto userResponseDto, CarResponseDto carResponseDto) throws FunctionnalException {
        // Validation des dates
        validationUtil.validateEndDateBeforeStartDate(tripRequestDto.getDateDebut(), tripRequestDto.getDateFin());

        Trip trip = tripMapper.toEntity(tripRequestDto, userResponseDto, carResponseDto);
        // Calcul de l'heure d'arrivée
        trip.setHeureArrivee(calculateArrivalTime(trip.getHeureDepart(), trip.getLieuDepart(),
                trip.getLieuArrivee(), trip.getVilleDepart(), trip.getVilleArrivee()));
        // Calcul des places restantes
        trip.setNbPlacesRestantes(calculatePlaceRest(tripRequestDto, carResponseDto));

        Trip savedTrip = tripRepository.save(trip);

        return tripMapper.toResponse(savedTrip);
    }


    // Read Trip
    @Transactional(readOnly = true)
    public List<TripResponseDto> getAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream()
                .map(tripMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public TripResponseDto getTripById(Integer id) throws FunctionnalException {
        // Vérification existence
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas."));

        return tripMapper.toResponse(trip);
    }

    // Update Trip
    @Transactional
    public TripResponseDto updateTrip(Integer id, TripRequestDto tripRequestDto, UserResponseDto userResponseDto, CarResponseDto carResponseDto) throws FunctionnalException {
        // Vérification existence
        Trip existingTrip = tripRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas."));

        // Validation des dates
        if (tripRequestDto.getDateDebut() != null && tripRequestDto.getDateFin() != null) {
            validationUtil.validateEndDateBeforeStartDate(tripRequestDto.getDateDebut(), tripRequestDto.getDateFin());
        }

        // Mise à jour des données
        tripMapper.updateEntity(existingTrip, tripRequestDto, userResponseDto, carResponseDto);
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
        // Recalcul des places restantes si nécessaire
        if (tripRequestDto.getNbPlacesRestantes() >= 0) {
            existingTrip.setNbPlacesRestantes(calculatePlaceRest(tripRequestDto, carResponseDto));
        }

        Trip updatedTrip = tripRepository.save(existingTrip);
        return tripMapper.toResponse(updatedTrip);
    }

    // Delete Trip
    @Transactional
    public void deleteTrip(Integer id) throws FunctionnalException {
        if (!tripRepository.existsById(id)) {
            throw new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas.");
        }
        tripRepository.deleteById(id);
    }

    // Calcule l'heure d'arrivée estimée
    public LocalTime calculateArrivalTime(LocalTime heureDepart, String lieuDepart, String lieuArrivee, String villeDepart, String villeArrivee) {
        // TODO: Implémenter la logique de calcul (API Google Maps, durée fixe, etc.)
        return heureDepart.plusHours(2);
    }

    // Calcul nombre de places restante
    public int calculatePlaceRest(TripRequestDto tripRequestDto, CarResponseDto carResponseDto) throws FunctionnalException {
        int nbPlacesRestants = carResponseDto.getNbDePlaces();
        Integer organizerId = tripRequestDto.getOrganisateurId();

        // Si c'est un véhicule de service, on doit décompter les places occupées
        if (validationUtil.estVehiculeDeService(carResponseDto.getId(), carRepository)) {
            // Vérifier si l'organisateur a déjà une réservation pour ce véhicule sur ces dates
            boolean organizerHasReservation = reservationRepository.existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                    carResponseDto.getId(),
                    organizerId, // ID de l'organisateur
                    tripRequestDto.getDateDebut(),
                    tripRequestDto.getDateFin()
            );
            if (organizerHasReservation) {
                nbPlacesRestants -= 1;
            }
        }

        int totalSubscriptions = subscribeRepository.countByTrip_Id(tripRequestDto.getId());

        nbPlacesRestants -= totalSubscriptions;

        return Math.max(0, nbPlacesRestants);
    }


}
