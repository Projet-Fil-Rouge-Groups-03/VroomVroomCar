package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Reservation;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.ReservationMapper;
import fr.diginamic.VroomVroomCar.repository.ReservationRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    private final ValidationUtil validationUtil;

    // Create Reservation
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto, UserResponseDto userResponseDto, CarResponseDto carResponseDto) throws FunctionnalException {
        // Validité des dates
        validationUtil.validateEndDateBeforeStartDate(requestDto.getDateDebut(), requestDto.getDateFin());

        Reservation reservation = reservationMapper.toEntity(requestDto, userResponseDto, carResponseDto);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponse(savedReservation);
    }

    // Read Reservation
    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getAllReservations(){
        List<Reservation> reservations = reservationRepository.findAll();
        return  reservations.stream()
                .map(reservationMapper::toResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public ReservationResponseDto getReservationById(Integer id) throws FunctionnalException {
        // Verification de l'existence
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("La reservation avec l'ID " + id + " n'existe pas."));
        return reservationMapper.toResponse(reservation);
    }

    // Update Reservation
    @Transactional
    public ReservationResponseDto updateReservation(Integer id, ReservationRequestDto requestDto, CarResponseDto carResponseDto) throws FunctionnalException {
        // Verification de l'existence
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("La reservation avec l'ID " + id + " n'existe pas."));

        // Validité des dates
        validationUtil.validateEndDateBeforeStartDate(requestDto.getDateDebut(), requestDto.getDateFin());

        // MAJ des données
        reservationMapper.updateEntity(existingReservation, requestDto);
        Reservation updatedReservation = reservationRepository.save(existingReservation);

        return reservationMapper.toResponse(updatedReservation);
    }

    // Delete Reservation
    @Transactional
    public void deleteReservation(Integer id) throws FunctionnalException {
        if(!reservationRepository.existsById(id)){
            throw new FunctionnalException("La reservation avec l'ID \" + id + \" n'existe pas.");
        }
        reservationRepository.deleteById(id);
    }
}
