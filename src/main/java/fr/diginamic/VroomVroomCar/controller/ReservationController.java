package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.ReservationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.ReservationResponseDto;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController implements IReservationController {

    @Autowired
    private ReservationService reservationService;

    // Create Reservation (POST)
    @PostMapping("/create")
    public ResponseEntity<ReservationResponseDto> createReservation(@Valid @RequestBody ReservationRequestDto requestDto) throws FunctionnalException {
        ReservationResponseDto reservationCreate = reservationService.createReservation(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationCreate);
    }

    // Read Reservation (GET)
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations(){
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
    @GetMapping("/all-available-cars")
    public ResponseEntity<List<CompanyCarResponseDto>> getAllAvailableCompanyCars(){
        List<CompanyCarResponseDto> cars = reservationService.getAllAvailableCompanyCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservationById(@PathVariable Integer id) throws FunctionnalException {
        ReservationResponseDto reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }
    @GetMapping("/car/{carId}") //?page=0&size=5
    public ResponseEntity<Page<ReservationResponseDto>> getReservationsByCarId(@PathVariable Integer carId,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "5") int size) throws FunctionnalException {
        Page<ReservationResponseDto> result = reservationService.getReservationsByCarId(carId, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByUserId(@PathVariable Integer userId) {
        List<ReservationResponseDto> reservations = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<List<ReservationResponseDto>> getUpcomingReservationsByUserId(@PathVariable Integer userId) {
        List<ReservationResponseDto> reservations = reservationService.getUpcomingReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{userId}/past")
    public ResponseEntity<List<ReservationResponseDto>> getPastReservationsByUserId(@PathVariable Integer userId) {
        List<ReservationResponseDto> reservations = reservationService.getPastReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    // Update Reservation (PUT)
    @PutMapping("/update/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Integer id, @Valid @RequestBody ReservationRequestDto requestDto) throws FunctionnalException {
        ReservationResponseDto reservationEdit = reservationService.updateReservation(id, requestDto);
        return ResponseEntity.ok(reservationEdit);
    }

    // Delete Reservation (DELETE)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) throws FunctionnalException {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation supprimée avec succès");
    }
}
