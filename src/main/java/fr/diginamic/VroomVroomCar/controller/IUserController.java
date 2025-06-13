package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Interface pour le contrôleur de gestion des utilisateurs.
 * Définit les opérations REST pour la gestion des utilisateurs.
 */
@Tag(name = "Utilisateur", description = "API pour la gestion des utilisateurs")
public interface IUserController {
    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return une {@link ResponseEntity} contenant une liste d'objets {@link UserResponseDto}.
     */
    @Operation(summary = "Récupérer tous les utilisateurs", description = "Renvoie une liste de tous les utilisateurs enregistrés.")
    public ResponseEntity<List<UserResponseDto>> findAll();

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à rechercher.
     * @return une {@link ResponseEntity} contenant un objet {@link UserResponseDto}.
     * @throws ResourceNotFoundException si aucun utilisateur avec ce nom n'est trouvé.
     */
    @Operation(summary = "Rechercher un utilisateur par id", description = "Renvoie les informations d'un utilisateur à partir de son id.")
    public ResponseEntity<UserResponseDto> findById(@RequestParam int id) throws ResourceNotFoundException;
    /**
     * Recherche un utilisateur par son nom.
     *
     * @param nom le nom de l'utilisateur à rechercher.
     * @return une {@link ResponseEntity} contenant un objet {@link UserResponseDto}.
     * @throws ResourceNotFoundException si aucun utilisateur avec ce nom n'est trouvé.
     */
    @Operation(summary = "Rechercher un utilisateur par nom", description = "Renvoie les informations d'un utilisateur à partir de son nom.")
    public ResponseEntity<UserResponseDto> findByNom(String nom) throws ResourceNotFoundException;
    /**
     * Ajoute un nouvel utilisateur.
     *
     * @param user les données du nouvel utilisateur à ajouter, encapsulées dans un {@link UserRequestDto}.
     * @return une {@link ResponseEntity} contenant l'objet {@link UserResponseDto} créé.
     * @throws ResourceNotFoundException si une ressource nécessaire à la création de l'utilisateur est manquante.
     */
    @Operation(summary = "Ajouter un nouvel utilisateur", description = "Crée un nouvel utilisateur avec les données fournies.")
    public ResponseEntity<UserResponseDto> addUser(
            @Parameter(description = "Données du nouvel utilisateur") @RequestBody UserRequestDto user
    ) throws ResourceNotFoundException;
    /**
     * Modifie un utilisateur existant en se basant sur son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à modifier.
     * @param user les nouvelles données de l'utilisateur, encapsulées dans un {@link UserRequestDto}.
     * @return une {@link ResponseEntity} contenant l'objet {@link UserResponseDto} mis à jour.
     * @throws ResourceNotFoundException si aucun utilisateur avec cet identifiant n'est trouvé.
     */
    @Operation(summary = "Modifier un utilisateur par ID", description = "Met à jour un utilisateur existant à partir de son identifiant.")
    ResponseEntity<UserResponseDto> editUser(
            @Parameter(description = "ID de l'utilisateur à modifier") @RequestParam int id,
            @Parameter(description = "Nouvelles données de l'utilisateur") @RequestBody UserRequestDto user
    ) throws ResourceNotFoundException;
    /**
     * Modifie un utilisateur existant en se basant sur son nom.
     *
     * @param nom le nom de l'utilisateur à modifier.
     * @param user les nouvelles données de l'utilisateur, encapsulées dans un {@link UserRequestDto}.
     * @return une {@link ResponseEntity} contenant l'objet {@link UserResponseDto} mis à jour.
     * @throws ResourceNotFoundException si aucun utilisateur avec ce nom n'est trouvé.
     */
    @Operation(summary = "Modifier un utilisateur par nom", description = "Met à jour un utilisateur existant à partir de son nom.")
    ResponseEntity<UserResponseDto> editUser(
            @Parameter(description = "Nom de l'utilisateur à modifier") @RequestParam String nom,
            @Parameter(description = "Nouvelles données de l'utilisateur") @RequestBody UserRequestDto user
    ) throws ResourceNotFoundException;

    /**
     * Supprime un utilisateur existant à partir de son identifiant.
     *
     * @param id l'identifiant de l'utilisateur à supprimer.
     * @return une {@link ResponseEntity} contenant un message de confirmation.
     * @throws ResourceNotFoundException si aucun utilisateur avec cet identifiant n'est trouvé.
     */
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur existant à partir de son identifiant.")
    ResponseEntity<String> deleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer") @RequestParam int id
    ) throws ResourceNotFoundException;


}
