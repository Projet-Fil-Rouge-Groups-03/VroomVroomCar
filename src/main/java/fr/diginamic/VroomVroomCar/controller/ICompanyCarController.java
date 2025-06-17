package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.CompanyCarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * Interface pour le contrôleur de gestion des voitures de société.
 */
@Tag(name = "Company Car Controller", description = "Contrôleur pour la gestion des voitures de société")
public interface ICompanyCarController {

    /**
     * Récupère une voiture de société par son ID.
     *
     * @param id l'ID de la voiture de société à récupérer.
     * @return une réponse contenant les détails de la voiture de société.
     * @throws ResourceNotFoundException si la voiture n'est pas trouvée.
     */
    @Operation(summary = "Récupérer une voiture de société par son ID")
    @GetMapping("/{id}")
    ResponseEntity<CompanyCarResponseDto> getCompanyCarById(
            @Parameter(description = "ID de la voiture de société", required = true)
            @PathVariable Integer id) throws ResourceNotFoundException;

    /**
     * Récupère toutes les voitures de société.
     *
     * @return une réponse contenant une liste de voitures de société.
     */
    @Operation(summary = "Récupérer toutes les voitures de société")
    @GetMapping
    ResponseEntity<List<CompanyCarResponseDto>> getAllCars();

    /**
     * Crée une nouvelle voiture de société.
     *
     * @param companyCarRequestDto le DTO de requête contenant les détails de la voiture de société à créer.
     * @return une réponse contenant les détails de la voiture de société créée.
     * @throws ResourceNotFoundException si l'utilisateur n'est pas trouvé.
     * @throws FunctionnalException si une voiture avec la même immatriculation existe déjà.
     */
    @Operation(summary = "Créer une nouvelle voiture de société")
    @PostMapping("/create")
    ResponseEntity<CompanyCarResponseDto> createCar(
            @Parameter(description = "DTO de requête contenant les détails de la voiture de société", required = true)
            @Valid @RequestBody CompanyCarRequestDto companyCarRequestDto) throws ResourceNotFoundException, FunctionnalException;

    /**
     * Met à jour une voiture de société existante.
     *
     * @param id l'ID de la voiture de société à mettre à jour.
     * @param companyCarRequestDto le DTO de requête contenant les nouveaux détails de la voiture de société.
     * @return une réponse contenant les détails de la voiture de société mise à jour.
     * @throws ResourceNotFoundException si la voiture ou l'utilisateur n'est pas trouvé.
     */
    @Operation(summary = "Mettre à jour une voiture de société existante")
    @PutMapping("/update/{id}")
    ResponseEntity<CompanyCarResponseDto> updateCar(
            @Parameter(description = "ID de la voiture de société à mettre à jour", required = true)
            @PathVariable Integer id,
            @Parameter(description = "DTO de requête contenant les nouveaux détails de la voiture de société", required = true)
            @Valid @RequestBody CompanyCarRequestDto companyCarRequestDto) throws ResourceNotFoundException;

    /**
     * Supprime une voiture de société par son ID.
     *
     * @param id l'ID de la voiture de société à supprimer.
     * @return une réponse confirmant la suppression.
     * @throws ResourceNotFoundException si la voiture n'est pas trouvée.
     */
    @Operation(summary = "Supprimer une voiture de société par son ID")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteCar(
            @Parameter(description = "ID de la voiture de société à supprimer", required = true)
            @PathVariable Integer id) throws ResourceNotFoundException;

    /**
     * Recherche des voitures de société par marque.
     *
     * @param marque la marque des voitures à rechercher.
     * @param size le nombre maximum de résultats à retourner.
     * @return une réponse contenant une liste de voitures de société.
     */
    @Operation(summary = "Rechercher des voitures de société par marque")
    @GetMapping("/search/marque")
    ResponseEntity<List<CompanyCarResponseDto>> searchCarsByMarque(
            @Parameter(description = "Marque des voitures à rechercher", required = true)
            @RequestParam String marque,
            @Parameter(description = "Nombre maximum de résultats à retourner")
            @RequestParam(defaultValue = "5") int size);

    /**
     * Recherche des voitures de société par modèle.
     *
     * @param modele le modèle des voitures à rechercher.
     * @param size le nombre maximum de résultats à retourner.
     * @return une réponse contenant une liste de voitures de société.
     */
    @Operation(summary = "Rechercher des voitures de société par modèle")
    @GetMapping("/search/modele")
    ResponseEntity<List<CompanyCarResponseDto>> searchCarsByModele(
            @Parameter(description = "Modèle des voitures à rechercher", required = true)
            @RequestParam String modele,
            @Parameter(description = "Nombre maximum de résultats à retourner")
            @RequestParam(defaultValue = "5") int size);

    /**
     * Récupère des voitures de société par catégorie.
     *
     * @param categories la catégorie des voitures à récupérer.
     * @param size le nombre maximum de résultats à retourner.
     * @return une réponse contenant une liste de voitures de société.
     */
    @Operation(summary = "Récupérer des voitures de société par catégorie")
    @GetMapping("/categorie/{categorie}")
    ResponseEntity<List<CompanyCarResponseDto>> getCarsByCategories(
            @Parameter(description = "Catégorie des voitures à récupérer", required = true)
            @PathVariable Categorie categories,
            @Parameter(description = "Nombre maximum de résultats à retourner")
            @RequestParam(defaultValue = "5") int size);

    /**
     * Recherche des voitures de société par immatriculation.
     *
     * @param immatriculation l'immatriculation des voitures à rechercher.
     * @param size le nombre maximum de résultats à retourner.
     * @return une réponse contenant une liste de voitures de société.
     */
    @Operation(summary = "Rechercher des voitures de société par immatriculation")
    @GetMapping("/search/immatriculation")
    ResponseEntity<List<CompanyCarResponseDto>> searchCarsByImmatriculation(
            @Parameter(description = "Immatriculation des voitures à rechercher", required = true)
            @RequestParam String immatriculation,
            @Parameter(description = "Nombre maximum de résultats à retourner")
            @RequestParam(defaultValue = "5") int size);
}
