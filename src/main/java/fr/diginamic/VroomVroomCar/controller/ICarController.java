package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.CarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Interface pour le contrôleur de gestion des voitures.
 * Définit les opérations REST pour la gestion des voitures.
 */
@Tag(name = "Voitures", description = "API pour la gestion des voitures")
public interface ICarController {

    /**
     * Récupère une voiture par son ID.
     *
     * @param id L'identifiant de la voiture à récupérer.
     * @return Réponse contenant les informations de la voiture.
     * @throws ResourceNotFoundException si la voiture n'est pas trouvée.
     */
    @Operation(
            summary = "Récupérer une voiture par son ID",
            responses = {
                    @ApiResponse(description = "Voiture trouvée", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CarResponseDto.class))),
                    @ApiResponse(description = "Voiture non trouvée", responseCode = "404",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<CarResponseDto> getCarById(
            @Parameter(description = "ID de la voiture à récupérer")
            @PathVariable Integer id) throws ResourceNotFoundException;

    /**
     * Récupère toutes les voitures.
     *
     * @return Réponse contenant la liste de toutes les voitures.
     */
    @Operation(
            summary = "Récupérer toutes les voitures",
            responses = {
                    @ApiResponse(description = "Liste des voitures", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CarResponseDto.class))))
            }
    )
    @GetMapping
    ResponseEntity<List<CarResponseDto>> getAllCars();

    /**
     * Crée une nouvelle voiture.
     *
     * @param carRequestDto Les données de la voiture à créer.
     * @return Réponse contenant les informations de la voiture créée.
     * @throws ResourceNotFoundException si l'utilisateur n'est pas trouvé.
     */
    @Operation(
            summary = "Créer une nouvelle voiture",
            responses = {
                    @ApiResponse(description = "Voiture créée", responseCode = "201",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CarResponseDto.class))),
                    @ApiResponse(description = "Erreur de validation", responseCode = "400",
                            content = @Content)
            }
    )
    @PostMapping
    ResponseEntity<CarResponseDto> createCar(
            @Parameter(description = "Données de la voiture à créer")
            @Valid @RequestBody CarRequestDto carRequestDto) throws ResourceNotFoundException;

    /**
     * Met à jour une voiture existante.
     *
     * @param id L'identifiant de la voiture à mettre à jour.
     * @param carRequestDto Les nouvelles données de la voiture.
     * @return Réponse contenant les informations mises à jour de la voiture.
     * @throws ResourceNotFoundException si la voiture ou l'utilisateur n'est pas trouvé.
     */
    @Operation(
            summary = "Mettre à jour une voiture",
            responses = {
                    @ApiResponse(description = "Voiture mise à jour", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CarResponseDto.class))),
                    @ApiResponse(description = "Voiture non trouvée", responseCode = "404",
                            content = @Content)
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<CarResponseDto> updateCar(
            @Parameter(description = "ID de la voiture à mettre à jour")
            @PathVariable Integer id,
            @Parameter(description = "Données de la voiture à mettre à jour")
            @Valid @RequestBody CarRequestDto carRequestDto) throws ResourceNotFoundException;

    /**
     * Supprime une voiture par son ID.
     *
     * @param id L'identifiant de la voiture à supprimer.
     * @return Réponse indiquant que la voiture a été supprimée.
     * @throws ResourceNotFoundException si la voiture n'est pas trouvée.
     */
    @Operation(
            summary = "Supprimer une voiture",
            responses = {
                    @ApiResponse(description = "Voiture supprimée", responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Voiture non trouvée", responseCode = "404",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteCar(
            @Parameter(description = "ID de la voiture à supprimer")
            @PathVariable Integer id) throws ResourceNotFoundException;

    /**
     * Récupère les voitures par ID d'utilisateur.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @param size Le nombre maximum de voitures à retourner.
     * @return Réponse contenant la liste des voitures de l'utilisateur.
     */
    @Operation(
            summary = "Récupérer les voitures par ID d'utilisateur",
            responses = {
                    @ApiResponse(description = "Liste des voitures de l'utilisateur", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CarResponseDto.class))))
            }
    )
    @GetMapping("/user/{userId}")
    ResponseEntity<List<CarResponseDto>> getCarsByUserId(
            @Parameter(description = "ID de l'utilisateur")
            @PathVariable Integer userId,
            @Parameter(description = "Nombre maximum de voitures à retourner")
            @RequestParam(defaultValue = "5") int size);

    /**
     * Recherche des voitures par marque.
     *
     * @param marque La marque à rechercher.
     * @param size Le nombre maximum de voitures à retourner.
     * @return Réponse contenant la liste des voitures de la marque spécifiée.
     */
    @Operation(
            summary = "Rechercher des voitures par marque",
            responses = {
                    @ApiResponse(description = "Liste des voitures de la marque spécifiée", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CarResponseDto.class))))
            }
    )
    @GetMapping("/search/marque")
    ResponseEntity<List<CarResponseDto>> searchCarsByMarque(
            @Parameter(description = "Marque à rechercher")
            @RequestParam String marque,
            @Parameter(description = "Nombre maximum de voitures à retourner")
            @RequestParam(defaultValue = "5") int size);

    /**
     * Recherche des voitures par modèle.
     *
     * @param modele Le modèle à rechercher.
     * @param size Le nombre maximum de voitures à retourner.
     * @return Réponse contenant la liste des voitures du modèle spécifié.
     */
    @Operation(
            summary = "Rechercher des voitures par modèle",
            responses = {
                    @ApiResponse(description = "Liste des voitures du modèle spécifié", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CarResponseDto.class))))
            }
    )
    @GetMapping("/search/modele")
    ResponseEntity<List<CarResponseDto>> searchCarsByModele(
            @Parameter(description = "Modèle à rechercher")
            @RequestParam String modele,
            @Parameter(description = "Nombre maximum de voitures à retourner")
            @RequestParam(defaultValue = "5") int size);

    /**
     * Récupère les voitures par catégorie.
     *
     * @param categories La catégorie à rechercher.
     * @param size Le nombre maximum de voitures à retourner.
     * @return Réponse contenant la liste des voitures de la catégorie spécifiée.
     */
    @Operation(
            summary = "Récupérer les voitures par catégorie",
            responses = {
                    @ApiResponse(description = "Liste des voitures de la catégorie spécifiée", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CarResponseDto.class))))
            }
    )
    @GetMapping("/categorie/{categories}")
    ResponseEntity<List<CarResponseDto>> getCarsByCategories(
            @Parameter(description = "Catégorie à rechercher")
            @PathVariable Categorie categories,
            @Parameter(description = "Nombre maximum de voitures à retourner")
            @RequestParam(defaultValue = "5") int size);
}

