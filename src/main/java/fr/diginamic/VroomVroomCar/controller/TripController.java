package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.VehiculeType;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController implements ITripController {

    @Autowired
    private TripService tripService;

    // Create Trip (POST)
    @PostMapping("/create")
    public ResponseEntity<TripResponseDto> createTrip(@Valid @RequestBody TripRequestDto tripRequestDto, UserResponseDto userResponseDto, CarResponseDto carResponseDto) throws ResourceNotFoundException, FunctionnalException {
        TripResponseDto tripCreate = tripService.createTrip(tripRequestDto, userResponseDto, carResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(tripCreate);
    }

    // Read Trip (GET)
    @GetMapping
    public ResponseEntity<List<TripResponseDto>> getAllTrips(){
        List<TripResponseDto> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDto> getTripById(@PathVariable Integer id) throws FunctionnalException {
        TripResponseDto trip = tripService.getTripById(id);
        return ResponseEntity.ok(trip);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @RequestParam(required = false) String villeDepart,
            @RequestParam(required = false) String villeArrivee,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime heureDepart,
            @RequestParam(defaultValue = "TOUS") VehiculeType vehiculeType) {

        try {
            List<Trip> trips = tripService.searchTrips(villeDepart, villeArrivee, dateDebut, heureDepart, vehiculeType);
            return ResponseEntity.ok(trips);
        } catch (IllegalArgumentException | FunctionnalException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/upcoming/{userId}")
    public List<Trip> getUpcomingTrip(@PathVariable Integer userId) throws ResourceNotFoundException {
        return tripService.getUpcomingUserTrips(userId);
    }
    @GetMapping("/past/{userId}")
    public List<Trip> getPastTrip(@PathVariable Integer userId) throws ResourceNotFoundException {
        return tripService.getPastUserTrips(userId);
    }

    // Update Trip (PUT)
    @PutMapping("/update/{id}")
    public ResponseEntity<TripResponseDto> updateTrip(@PathVariable Integer id, @Valid @RequestBody TripRequestDto tripRequestDto,  UserResponseDto userResponseDto, CarResponseDto carResponseDto) throws FunctionnalException {
        TripResponseDto tripEdit = tripService.updateTrip(id, tripRequestDto, userResponseDto, carResponseDto);
        return ResponseEntity.ok(tripEdit);
    }

    // Delete Trip (DELETE)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTrip(@PathVariable Integer id) throws FunctionnalException {
        tripService.deleteTrip(id);
        return ResponseEntity.ok("Trajet supprimé avec succès");
    }
}
