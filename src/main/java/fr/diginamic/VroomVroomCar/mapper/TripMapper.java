package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.repository.CarRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

public class TripMapper {

    private UserRepository userRepository;
    private CarRepository carRepository;

    public Trip toEntity(TripRequestDto request) {
        Trip trip = new Trip();
        trip.setDateDebut(request.getDateDebut());
        trip.setDateFin(request.getDateFin());
        trip.setHeureDepart(request.getHeureDepart());
        trip.setLieuDepart(request.getLieuDepart());
        trip.setLieuArrivee(request.getLieuArrivee());
        trip.setVilleDepart(request.getVilleDepart());
        trip.setVilleArrivee(request.getVilleArrivee());
        trip.setNbPlacesRestantes(request.getNbPlacesRestantes());

        User organisateur = userRepository.findById(request.getOrganisateurId())
                .orElseThrow(() -> new EntityNotFoundException("Organisateur non trouvé"));
        trip.setOrganisateur(organisateur);

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicule non trouvé"));
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
            response.setOrganisateurId(trip.getOrganisateur().getId());
        }

        if (trip.getCar() != null) {
            response.setCarId(trip.getCar().getId());
        }

        return response;
    }

    public void updateEntity(Trip source, Trip target) {
        target.setDateDebut(source.getDateDebut());
        target.setDateFin(source.getDateFin());
        target.setDateDebut(source.getDateDebut());
        target.setDateFin(source.getDateFin());
        target.setHeureDepart(source.getHeureDepart());
        target.setHeureArrivee(source.getHeureArrivee());
        target.setLieuDepart(source.getLieuDepart());
        target.setLieuArrivee(source.getLieuArrivee());
        target.setVilleDepart(source.getVilleDepart());
        target.setVilleArrivee(source.getVilleArrivee());
        target.setNbPlacesRestantes(source.getNbPlacesRestantes());
        target.setOrganisateur(source.getOrganisateur());
        target.setCar(source.getCar());
    }
}
