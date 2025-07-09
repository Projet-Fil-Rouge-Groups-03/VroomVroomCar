package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserSummaryDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.repository.CarRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.service.CO2Service;
import fr.diginamic.VroomVroomCar.service.OpenRouteService;
import fr.diginamic.VroomVroomCar.util.TimeTravelUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripMapper {

    private final CarMapper carMapper;
    private final OpenRouteService openRouteService;
    private final CO2Service co2Service;
    private final TimeTravelUtil timeTravelUtil;

    public Trip toEntity(TripRequestDto request, User organisateur, Car car) {
        Trip trip = new Trip();
        trip.setDateDebut(request.getDateDebut());
        trip.setDateFin(request.getDateFin());
        trip.setHeureDepart(request.getHeureDepart());
        trip.setLieuDepart(request.getLieuDepart());
        trip.setLieuArrivee(request.getLieuArrivee());
        trip.setVilleDepart(request.getVilleDepart());
        trip.setVilleArrivee(request.getVilleArrivee());
        trip.setNbPlacesRestantes(request.getNbPlacesRestantes());
        trip.setOrganisateur(organisateur);
        trip.setCar(car);
        return trip;
    }

    public TripResponseDto toResponse(Trip trip) {
        TripResponseDto response = new TripResponseDto();
        response.setId(trip.getId());
        response.setDateDebut(trip.getDateDebut());
        response.setDateFin(trip.getDateFin());
        response.setHeureDepart(trip.getHeureDepart());
        response.setHeureArrivee(trip.getHeureArrivee());
        response.setLieuDepart(trip.getLieuDepart());
        response.setLieuArrivee(trip.getLieuArrivee());
        response.setVilleDepart(trip.getVilleDepart());
        response.setVilleArrivee(trip.getVilleArrivee());
        response.setNbPlacesRestantes(trip.getNbPlacesRestantes());

        if (trip.getOrganisateur() != null) {
            User organisateurEntity = trip.getOrganisateur();
            response.setOrganisateurId(organisateurEntity.getId());
            response.setOrganisateur(new UserSummaryDto(organisateurEntity.getNom(), organisateurEntity.getPrenom()));
        }

        if (trip.getCar() != null) {
            Car carEntity = trip.getCar();
            response.setCarId(carEntity.getId());
            response.setCar(carMapper.toResponseDto(carEntity));
        }

        // === Estimations ===
        String adresseDepart = trip.getLieuDepart() + ", " + trip.getVilleDepart();
        String adresseArrivee = trip.getLieuArrivee() + ", " + trip.getVilleArrivee();
        // Valeurs par défaut
        response.setTimeTravel("Inconnue");
        response.setDistanceInKm(0.0);
        response.setPollution(0.0);
        // Calculs
        try {
            // Appel pour la durée
            double durationInSeconds = openRouteService.getTravelDurationInSeconds(adresseDepart, adresseArrivee);
            response.setTimeTravel(timeTravelUtil.formatDuration(durationInSeconds));
            // Appel pour la distance
            double distanceKm = openRouteService.getTravelDistanceInKilometers(adresseDepart, adresseArrivee);
            response.setDistanceInKm(distanceKm);
            // Appel pour la pollution
            double pollutionCO2 = co2Service.calculerCo2TrajetAvecOSM(trip.getCar(), trip);
            response.setPollution(pollutionCO2);

        } catch (FunctionnalException e) {
            System.err.println("Erreur fonctionnelle" + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur technique" + e.getMessage());
        }

        return response;
    }

    public void updateEntity(Trip existingTrip, TripRequestDto requestDto, User organisateur, Car car) {
        if (requestDto.getDateDebut() != null) {
            existingTrip.setDateDebut(requestDto.getDateDebut());
        } if (requestDto.getDateFin() != null) {
            existingTrip.setDateFin(requestDto.getDateFin());
        } if (requestDto.getHeureDepart() != null) {
            existingTrip.setHeureDepart(requestDto.getHeureDepart());
        } if (requestDto.getLieuDepart() != null) {
            existingTrip.setLieuDepart(requestDto.getLieuDepart());
        } if (requestDto.getLieuArrivee() != null) {
            existingTrip.setLieuArrivee(requestDto.getLieuArrivee());
        } if (requestDto.getVilleDepart() != null) {
            existingTrip.setVilleDepart(requestDto.getVilleDepart());
        } if (requestDto.getVilleArrivee() != null) {
            existingTrip.setVilleArrivee(requestDto.getVilleArrivee());
        } if (requestDto.getNbPlacesRestantes() >= 0) {
            existingTrip.setNbPlacesRestantes(requestDto.getNbPlacesRestantes());
        } if (requestDto.getOrganisateurId() != null) {
            existingTrip.setOrganisateur(organisateur);
        } if (requestDto.getCarId() != null) {
            existingTrip.setCar(car);
        }
    }
}
