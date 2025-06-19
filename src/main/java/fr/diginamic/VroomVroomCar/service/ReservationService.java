package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.entity.Reservation;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.mapper.CompanyCarMapper;
import fr.diginamic.VroomVroomCar.mapper.ReservationMapper;
import fr.diginamic.VroomVroomCar.repository.CompanyCarRepository;
import fr.diginamic.VroomVroomCar.repository.ReservationRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final CompanyCarMapper companyCarMapper;

    private final UserRepository userRepository;
    private final CompanyCarRepository companyCarRepository;

    private final ValidationUtil validationUtil;

    // Create Reservation
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) throws FunctionnalException {
        // Validité des dates
        validationUtil.validateEndDateBeforeStartDate(requestDto.getDateDebut(), requestDto.getDateFin());

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new FunctionnalException("Utilisateur non trouvé"));

        CompanyCar car = companyCarRepository.findById(requestDto.getCarId())
                .orElseThrow(() -> new FunctionnalException("Véhicule non trouvé"));

        Reservation reservation = reservationMapper.toEntity(requestDto, user, car);

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
    @Transactional(readOnly = true)
    public Page<ReservationResponseDto> getReservationsByCarId(Integer carId, int page, int size) throws FunctionnalException {
        // Vérification de l'existence de la voiture
        if (!companyCarRepository.existsById(carId)) {
            throw new FunctionnalException("Voiture non trouvée avec l'ID: " + carId);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateDebut").descending());
        Page<Reservation> reservations = reservationRepository.findByCompanyCar_Id(carId, pageable);

        return reservations.map(reservationMapper::toResponse);
    }


    // Update Reservation
    @Transactional
    public ReservationResponseDto updateReservation(Integer id, ReservationRequestDto requestDto) throws FunctionnalException {
        // Verification de l'existence
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new FunctionnalException("La reservation avec l'ID " + id + " n'existe pas."));

        // Validité des dates
        validationUtil.validateEndDateBeforeStartDate(requestDto.getDateDebut(), requestDto.getDateFin());

        User user = null;
        if (requestDto.getUserId() != null) {
            user = userRepository.findById(requestDto.getUserId())
                    .orElseThrow(() -> new FunctionnalException("Utilisateur avec l'ID " + requestDto.getUserId() + " non trouvé."));
        }

        CompanyCar car = null;
        if (requestDto.getCarId() != null) {
            car = companyCarRepository.findById(requestDto.getCarId())
                    .orElseThrow(() -> new FunctionnalException("Véhicule avec l'ID " + requestDto.getCarId() + " non trouvé."));
        }

        // MAJ des données
        reservationMapper.updateEntity(existingReservation, requestDto, user, car);
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

    @Override
    public List<CompanyCarResponseDto> getAllAvailableCompanyCars() {
        Date now = Date.valueOf(LocalDate.now());

        List<CompanyCar> allCars = companyCarRepository.findAll();
        List<CompanyCar> reservedCars = reservationRepository.findAll().stream()
                .filter(r -> r.getDateFin().after(now))
                .map(Reservation::getCompanyCar)
                .toList();

        return allCars.stream()
                .filter(car -> !reservedCars.contains(car))
                .map(companyCarMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
