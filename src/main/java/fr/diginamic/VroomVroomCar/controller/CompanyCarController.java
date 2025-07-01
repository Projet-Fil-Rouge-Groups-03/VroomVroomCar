package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.CompanyCarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.CompanyCarService;
import fr.diginamic.VroomVroomCar.util.DateUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company-cars")
public class CompanyCarController implements ICompanyCarController {

    private final CompanyCarService companyCarService;

    // GET par id

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<CompanyCarResponseDto> getCompanyCarById(@PathVariable Integer id) throws ResourceNotFoundException {
        CompanyCarResponseDto car = companyCarService.getCompanyCarById(id);
        return ResponseEntity.ok(car);
    }

    // GET all

    @GetMapping
    @Override
    public ResponseEntity<List<CompanyCarResponseDto>> getAllCars() {
        List<CompanyCarResponseDto> cars = companyCarService.getAllCompanyCars();
        return ResponseEntity.ok(cars);
    }

    // POST

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<CompanyCarResponseDto> createCar(@Valid @RequestBody CompanyCarRequestDto companyCarRequestDto) throws ResourceNotFoundException, FunctionnalException {
        CompanyCarResponseDto createdCar = companyCarService.createCompanyCar(companyCarRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    // UPDATE

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<CompanyCarResponseDto> updateCar(@PathVariable Integer id, @Valid @RequestBody CompanyCarRequestDto companyCarRequestDto) throws ResourceNotFoundException {
        CompanyCarResponseDto updatedCar = companyCarService.updateCar(id, companyCarRequestDto);
        return ResponseEntity.ok(updatedCar);
    }

    // DELETE

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<String> deleteCar(@PathVariable Integer id) throws ResourceNotFoundException {
        companyCarService.deleteCar(id);
        return ResponseEntity.ok("Voiture de service supprimée avec succès");
    }

    // GET + spécifiques

    @GetMapping("/search/marque")
    @Override
    public ResponseEntity<List<CompanyCarResponseDto>> searchCarsByMarque(@RequestParam String marque, @RequestParam(defaultValue = "5") int size) {
        List<CompanyCarResponseDto> cars = companyCarService.searchCarsByMarque(marque, size);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/search/modele")
    @Override
    public ResponseEntity<List<CompanyCarResponseDto>> searchCarsByModele(@RequestParam String modele, @RequestParam(defaultValue = "5") int size) {
        List<CompanyCarResponseDto> cars = companyCarService.searchCarsByModele(modele, size);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/categorie/{categories}")
    @Override
    public ResponseEntity<List<CompanyCarResponseDto>> getCarsByCategories(@PathVariable Categorie categories, @RequestParam(defaultValue = "5") int size) {
        List<CompanyCarResponseDto> cars = companyCarService.getCarsByCategories(categories, size);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/search/immatriculation")
    @Override
    public ResponseEntity<List<CompanyCarResponseDto>> searchCarsByImmatriculation(@RequestParam String immatriculation, @RequestParam(defaultValue = "5") int size) {
        List<CompanyCarResponseDto> cars = companyCarService.searchCarsByModele(immatriculation, size);
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CompanyCarResponseDto>> searchCompanyCar(
            @RequestParam(required = false) String marque,
            @RequestParam(required = false) String modele,
            @RequestParam(required = false, defaultValue = "0") int nbDePlaces,
            @RequestParam(required = false) String dateDebutStr,
            @RequestParam(required = false) String dateFinStr) {
        try {
            Date dateDebut = DateUtil.convertStringToDate(dateDebutStr);
            Date dateFin = DateUtil.convertStringToDate(dateFinStr);
            List<CompanyCarResponseDto> companyCars = companyCarService.searchCompanyCar(
                    marque, modele, nbDePlaces, dateDebut, dateFin);
            return ResponseEntity.ok(companyCars);
        } catch (IllegalArgumentException | FunctionnalException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
