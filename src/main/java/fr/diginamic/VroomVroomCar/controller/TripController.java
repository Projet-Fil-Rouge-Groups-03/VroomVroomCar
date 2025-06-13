package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController implements ITripController {

    @Autowired
    private TripService tripService;

    // Create Trip (POST)
    @PostMapping("/create")
    public ResponseEntity<TripResponseDto> createTrip(@Valid @RequestBody TripRequestDto tripRequestDto) throws ResourceNotFoundException, FunctionnalException {
        TripResponseDto tripCreate = tripService.createTrip(tripRequestDto);
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

    // Update Trip (PUT)
    @PutMapping("/update/{id}")
    public ResponseEntity<TripResponseDto> updateTrip(@PathVariable Integer id, @Valid @RequestBody TripRequestDto tripRequestDto) throws FunctionnalException {
        TripResponseDto tripEdit = tripService.updateTrip(id, tripRequestDto);
        return ResponseEntity.ok(tripEdit);
    }

    // Delete Trip (DELETE)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTrip(@PathVariable Integer id) throws FunctionnalException {
        tripService.deleteTrip(id);
        return ResponseEntity.ok("Trajet supprimé avec succès");
    }
}
