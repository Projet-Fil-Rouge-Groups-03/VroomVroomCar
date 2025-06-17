package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.CarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController implements ICarController {

    @Autowired
    private CarService carService;

    // GET par id

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Integer id) throws ResourceNotFoundException {
        CarResponseDto car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    // GET all

    @GetMapping
    @Override
    public ResponseEntity<List<CarResponseDto>> getAllCars() {
        List<CarResponseDto> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    // POST

    @PostMapping("/create")
    @Override
    public ResponseEntity<CarResponseDto> createCar(@Valid @RequestBody CarRequestDto carRequestDto) throws ResourceNotFoundException {
        CarResponseDto createdCar = carService.createCar(carRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    // UPDATE

    @PutMapping("/update/{id}")
    @Override
    public ResponseEntity<CarResponseDto> updateCar(@PathVariable Integer id, @Valid @RequestBody CarRequestDto carRequestDto) throws ResourceNotFoundException {
        CarResponseDto updatedCar = carService.updateCar(id, carRequestDto);
        return ResponseEntity.ok(updatedCar);
    }

    // DELETE

    @DeleteMapping("/delete/{id}")
    @Override
    public ResponseEntity<String> deleteCar(@PathVariable Integer id) throws ResourceNotFoundException {
        carService.deleteCar(id);
        return ResponseEntity.ok("Voiture supprimée avec succès");
    }

    // GET + spécifiques

    @GetMapping("/user/{userId}")
    @Override
    public ResponseEntity<List<CarResponseDto>> getCarsByUserId(@PathVariable Integer userId, @RequestParam(defaultValue = "5") int size) {
        List<CarResponseDto> cars = carService.getCarsByUserId(userId, size);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/search/marque")
    @Override
    public ResponseEntity<List<CarResponseDto>> searchCarsByMarque(@RequestParam String marque, @RequestParam(defaultValue = "5") int size) {
        List<CarResponseDto> cars = carService.searchCarsByMarque(marque, size);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/search/modele")
    @Override
    public ResponseEntity<List<CarResponseDto>> searchCarsByModele(@RequestParam String modele, @RequestParam(defaultValue = "5") int size) {
        List<CarResponseDto> cars = carService.searchCarsByModele(modele, size);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/categorie/{categories}")
    @Override
    public ResponseEntity<List<CarResponseDto>> getCarsByCategories(@PathVariable Categorie categories, @RequestParam(defaultValue = "5") int size) {
        List<CarResponseDto> cars = carService.getCarsByCategories(categories, size);
        return ResponseEntity.ok(cars);
    }
}
