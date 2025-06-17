package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Interface pour le contrôleur de gestion des inscriptions.
 * Définit les opérations REST pour la gestion des inscriptions.
 */
@Tag(name = "Inscription", description = "API pour la gestion des inscriptions")
public interface ISubscribeController {

    /**
     * Récupère la liste de toutes les inscriptions.
     *
     * @return une {@link ResponseEntity} contenant une liste d'objets {@link SubscribeResponseDto}.
     */
    @Operation(summary = "Récupérer tous les inscriptions", description = "Renvoie une liste de tous les inscriptions enregistrés.")
    ResponseEntity<List<SubscribeResponseDto>> findAll();

    /**
     * Recherche une inscription par son identifiant.
     *
     * @param id l'identifiant de l'inscription à rechercher.
     * @return une {@link ResponseEntity} contenant un objet {@link SubscribeResponseDto}.
     * @throws ResourceNotFoundException si aucune inscription avec cet ID n'est trouvé.
     */
    @Operation(summary = "Rechercher une inscription par ID", description = "Renvoie les informations d'une inscription à partir de son identifiant.")
    ResponseEntity<SubscribeResponseDto> findById(@RequestParam int id) throws ResourceNotFoundException;


    /**
     * Récupère la liste de toutes les inscriptions pour un trajet donné.
     *
     * @param id l'identifiant du trajet.
     * @return une {@link ResponseEntity} contenant une liste d'objets {@link SubscribeResponseDto}.
     */
    @Operation(summary = "Récupérer toutes les inscriptions pour un trajet donné", description = "Renvoie une liste de tous les inscriptions enregistrés.")
    ResponseEntity<List<SubscribeResponseDto>> findByTrip(int id);

    /**
     * Recherche la liste de toutes les inscriptions pour un utilisateur donné.
     *
     * @param id l'identifiant de l'utilisateur.
     * @return une {@link ResponseEntity} contenant une liste d'objets {@link SubscribeResponseDto}.
     */
    @Operation(summary = "Récupérer toutes les inscriptions pour un utilisateur donné", description = "Renvoie une liste de tous les inscriptions enregistrés.")
    ResponseEntity<List<SubscribeResponseDto>> findByUser(int id);
    /**
     * Ajoute une nouvelle inscription.
     *
     * @param subscribe les données de la nouvelle inscription à ajouter, encapsulées dans un {@link SubscribeRequestDto}.
     * @return une {@link ResponseEntity} contenant l'objet {@link SubscribeResponseDto} créé.
     * @throws ResourceNotFoundException si une ressource nécessaire à la création de l'inscription est manquante.
     */
    @Operation(summary = "Ajouter un nouvel inscription", description = "Crée un nouvel inscription avec les données fournies.")
    ResponseEntity<SubscribeResponseDto> addSubscribe(
            @Parameter(description = "Données du nouvel inscription") @RequestBody SubscribeRequestDto subscribe
    ) throws ResourceNotFoundException;

    /**
     * Modifie une inscription existante en se basant sur son identifiant.
     *
     * @param id l'identifiant de l'inscription à modifier.
     * @param subscribe les nouvelles données de l'inscription, encapsulées dans un {@link SubscribeRequestDto}.
     * @return une {@link ResponseEntity} contenant l'objet {@link SubscribeResponseDto} mis à jour.
     * @throws ResourceNotFoundException si aucune inscription avec cet identifiant n'est trouvé.
     */
    @Operation(summary = "Modifier une inscription par ID", description = "Met à jour une inscription existant à partir de son identifiant.")
    ResponseEntity<SubscribeResponseDto> editSubscribe(
            @Parameter(description = "ID de l'inscription à modifier") @RequestParam int id,
            @Parameter(description = "Nouvelles données de l'inscription") @RequestBody SubscribeRequestDto subscribe
    ) throws ResourceNotFoundException;


    /**
     * Supprime une inscription existant à partir de son identifiant.
     *
     * @param id l'identifiant de l'inscription à supprimer.
     * @return une {@link ResponseEntity} contenant un message de confirmation.
     * @throws ResourceNotFoundException si aucune inscription avec cet identifiant n'est trouvé.
     */
    @Operation(summary = "Supprimer une inscription", description = "Supprime une inscription existant à partir de son identifiant.")
    ResponseEntity<String> deleteSubscribe(
            @Parameter(description = "ID de l'inscription à supprimer") @RequestParam int id
    ) throws ResourceNotFoundException;
}