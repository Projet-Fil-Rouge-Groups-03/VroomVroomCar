package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.TripNotificationDetailsDto;
import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.*;
import fr.diginamic.VroomVroomCar.entity.*;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.TripMapper;
import fr.diginamic.VroomVroomCar.repository.*;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripService implements ITripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final SubscribeRepository subscribeRepository;
    private final NotificationService notificationService;
    private final OpenRouteService openRouteService;
    private final ValidationUtil validationUtil;

    // Create Trip
    @Transactional
    public TripResponseDto createTrip(TripRequestDto tripRequestDto) throws FunctionnalException {
        // Validation des dates
        validationUtil.validateEndDateBeforeStartDate(tripRequestDto.getDateDebut(), tripRequestDto.getDateFin());

        // Récupération des entités depuis la base de données
        User organisateur = userRepository.findById(tripRequestDto.getOrganisateurId())
                .orElseThrow(() -> new FunctionnalException("Utilisateur avec l'ID " + tripRequestDto.getOrganisateurId() + " n'existe pas."));

        Car car = carRepository.findById(tripRequestDto.getCarId())
                .orElseThrow(() -> new FunctionnalException("Voiture avec l'ID " + tripRequestDto.getCarId() + " n'existe pas."));

        Trip trip = tripMapper.toEntity(tripRequestDto, organisateur, car);

        // Calcul de l'heure d'arrivée
        trip.setHeureArrivee(calculateArrivalTime(
                trip.getHeureDepart(),
                trip.getLieuDepart(),
                trip.getLieuArrivee(),
                trip.getVilleDepart(),
                trip.getVilleArrivee()));

        // Calcul des places restantes
        trip.setNbPlacesRestantes(calculatePlaceRest(tripRequestDto, car));

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
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas."));
        return tripMapper.toResponse(trip);
    }

    @Transactional(readOnly = true)
    public List<TripResponseDto> searchTrips(String villeDepart, String villeArrivee, Date dateDebut,
                                  LocalTime heureDepart, VehiculeType vehiculeType) throws FunctionnalException {
        List<Trip> trips = tripRepository.findTripsWithFilters(
                villeDepart,
                villeArrivee,
                dateDebut,
                heureDepart,
                vehiculeType.name()
        );
        return trips.stream()
                .map(tripMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TripResponseDto> getUpcomingUserTrips(Integer userId) {
        List<Trip> trips = tripRepository.findUpcomingUserTrips(userId);
        return trips.stream()
                .map(tripMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TripResponseDto> getPastUserTrips(Integer userId) {
        List<Trip> trips = tripRepository.findPastUserTrips(userId);
        return trips.stream()
                .map(tripMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Update Trip
    @Transactional
    public TripResponseDto updateTrip(Integer id, TripRequestDto tripRequestDto) throws FunctionnalException {
        // Vérification existence
        Trip existingTrip = tripRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas."));

        // Validation des dates
        if (tripRequestDto.getDateDebut() != null && tripRequestDto.getDateFin() != null) {
            validationUtil.validateEndDateBeforeStartDate(tripRequestDto.getDateDebut(), tripRequestDto.getDateFin());
        }

        // Récupération des entités si elles sont modifiées
        User organisateur = null;
        Car car = null;

        if (tripRequestDto.getOrganisateurId() != null) {
            organisateur = userRepository.findById(tripRequestDto.getOrganisateurId())
                    .orElseThrow(() -> new FunctionnalException("Utilisateur avec l'ID " + tripRequestDto.getOrganisateurId() + " n'existe pas."));
        }

        if (tripRequestDto.getCarId() != null) {
            car = carRepository.findById(tripRequestDto.getCarId())
                    .orElseThrow(() -> new FunctionnalException("Voiture avec l'ID " + tripRequestDto.getCarId() + " n'existe pas."));
        }

        // Mise à jour des données
        tripMapper.updateEntity(existingTrip, tripRequestDto, organisateur, car);

        // Recalcul de l'heure d'arrivée si nécessaire
        if (tripRequestDto.getHeureDepart() != null || tripRequestDto.getLieuDepart() != null ||
                tripRequestDto.getLieuArrivee() != null || tripRequestDto.getVilleDepart() != null ||
                tripRequestDto.getVilleArrivee() != null) {
            existingTrip.setHeureArrivee(calculateArrivalTime(
                    existingTrip.getHeureDepart(),
                    existingTrip.getLieuDepart(),
                    existingTrip.getLieuArrivee(),
                    existingTrip.getVilleDepart(),
                    existingTrip.getVilleArrivee()
            ));
        }

        // Recalcul des places restantes si nécessaire
        if (car != null || tripRequestDto.getNbPlacesRestantes() >= 0) {
            Car carForCalculation = car != null ? car : existingTrip.getCar();
            existingTrip.setNbPlacesRestantes(calculatePlaceRest(tripRequestDto, carForCalculation));
        }

        Trip updatedTrip = tripRepository.save(existingTrip);
        // Envoi notifications aux participants
        notificationService.sendNotificationToParticipantsOnModification(updatedTrip);
        return tripMapper.toResponse(updatedTrip);
    }

    // Delete Trip
    @Transactional
    public void deleteTrip(Integer id) throws FunctionnalException {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("Le trajet avec l'ID " + id + " n'existe pas."));

        List<Subscribe> subscriptions = new ArrayList<>(trip.getSubscribes());
        TripNotificationDetailsDto detailsDto = new TripNotificationDetailsDto(trip);

        notificationService.sendNotificationToParticipantsOnAnnulation(subscriptions, detailsDto);

        tripRepository.delete(trip);
    }

    // Calcule l'heure d'arrivée estimée
    public LocalTime calculateArrivalTime(LocalTime heureDepart, String lieuDepart, String lieuArrivee, String villeDepart, String villeArrivee) throws FunctionnalException {
        String fromAddress = lieuDepart + ", " + villeDepart;
        String toAddress = lieuArrivee + ", " + villeArrivee;

        double durationInSeconds = openRouteService.getTravelDurationInSeconds(fromAddress, toAddress);
        long durationInMinutes = Math.round(durationInSeconds / 60);

        return heureDepart.plusMinutes(durationInMinutes);
    }

    // Calcul nombre de places restante
    public int calculatePlaceRest(TripRequestDto tripRequestDto, Car car) throws FunctionnalException {
        int nbPlacesRestants = car.getNbDePlaces();
        Integer organizerId = tripRequestDto.getOrganisateurId();

        // Si c'est un véhicule de service, on doit décompter les places occupées
        if (validationUtil.estVehiculeDeService(car.getId(), carRepository)) {
            // Vérifier si l'organisateur a déjà une réservation pour ce véhicule sur ces dates
            boolean organizerHasReservation = reservationRepository.existsByCompanyCar_IdAndUser_IdAndDateDebutAndDateFin(
                    car.getId(),
                    organizerId,
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

