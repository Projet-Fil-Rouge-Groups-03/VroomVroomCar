package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Reservation;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.repository.CarRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private UserRepository userRepository;
    private CarRepository carRepository;

    public Reservation toEntity(ReservationRequestDto request, UserResponseDto userResponseDto, CarResponseDto carResponseDto){
        Reservation reservation = new Reservation();
        reservation.setDateDebut(request.getDateDebut());
        reservation.setDateFin(request.getDateFin());

        User user = userRepository.findById(userResponseDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
        reservation.setUser(user);

        Car car = carRepository.findById(carResponseDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicule non trouvé"));
        reservation.setCar(car);

        return reservation;
    }

    public ReservationResponseDto toResponse(Reservation reservation){
        ReservationResponseDto response = new ReservationResponseDto();
        response.setId(response.getCarId());
        response.setDateDebut(reservation.getDateDebut());
        response.setDateFin(reservation.getDateFin());

        if (reservation.getUser() != null){
            response.setUserId(reservation.getUser().getId());
        }

        if (reservation.getCar() != null){
            response.setCarId(reservation.getCar().getId());
        }

        return response;
    }

    public void updateEntity(Reservation existingReservation, ReservationRequestDto requestDto){
        if (requestDto.getDateDebut() != null) {
            existingReservation.setDateDebut(requestDto.getDateDebut());
        } if (requestDto.getDateFin() != null) {
            existingReservation.setDateFin(requestDto.getDateFin());
        } if (requestDto.getUserId() != null){
            User user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));
            existingReservation.setUser(user);
        } if (requestDto.getCarId() != null) {
            Car car = carRepository.findById(requestDto.getCarId())
                    .orElseThrow(() -> new EntityNotFoundException("Vehicule non trouvé"));
            existingReservation.setCar(car);
        }
    }
}
