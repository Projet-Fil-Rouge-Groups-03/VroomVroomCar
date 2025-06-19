package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.entity.Reservation;
import fr.diginamic.VroomVroomCar.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationRequestDto request, User user, CompanyCar car) {
        Reservation reservation = new Reservation();
        reservation.setDateDebut(request.getDateDebut());
        reservation.setDateFin(request.getDateFin());
        reservation.setUser(user);
        reservation.setCompanyCar(car);
        return reservation;
    }

    public ReservationResponseDto toResponse(Reservation reservation) {
        ReservationResponseDto response = new ReservationResponseDto();
        response.setId(reservation.getId());
        response.setDateDebut(reservation.getDateDebut());
        response.setDateFin(reservation.getDateFin());

        if (reservation.getUser() != null) {
            response.setUserId(reservation.getUser().getId());
        }

        if (reservation.getCompanyCar() != null) {
            response.setCarId(reservation.getCompanyCar().getId());
        }

        return response;
    }

    public void updateEntity(Reservation existingReservation, ReservationRequestDto request, User user, CompanyCar car) {
        if (request.getDateDebut() != null) {
            existingReservation.setDateDebut(request.getDateDebut());
        }
        if (request.getDateFin() != null) {
            existingReservation.setDateFin(request.getDateFin());
        }
        if (user != null) {
            existingReservation.setUser(user);
        }
        if (car != null) {
            existingReservation.setCompanyCar(car);
        }
    }
}
