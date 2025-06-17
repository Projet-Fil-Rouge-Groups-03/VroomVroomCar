package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.CarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Cette interface définit les opérations de service pour la gestion des voitures.
 * Elle fournit des méthodes pour récupérer, créer, mettre à jour et supprimer des voitures,
 * ainsi que pour effectuer des recherches spécifiques.
 */
public interface ICarService {

    /**
     * Récupère une voiture par son identifiant.
     *
     * @param id L'identifiant de la voiture à récupérer.
     * @return Un DTO de réponse contenant les informations de la voiture.
     * @throws ResourceNotFoundException Si aucune voiture n'est trouvée avec l'identifiant spécifié.
     */
    CarResponseDto getCarById(Integer id) throws ResourceNotFoundException;

    /**
     * Récupère toutes les voitures.
     *
     * @return Une liste de DTOs de réponse contenant les informations de toutes les voitures.
     */
    @Transactional(readOnly = true)
    List<CarResponseDto> getAllCars();

    /**
     * Crée une nouvelle voiture à partir des informations fournies dans le DTO de requête.
     *
     * @param carRequestDto Le DTO de requête contenant les informations de la voiture à créer.
     * @return Un DTO de réponse contenant les informations de la voiture nouvellement créée.
     * @throws ResourceNotFoundException Si l'utilisateur associé n'est pas trouvé.
     */
    CarResponseDto createCar(CarRequestDto carRequestDto) throws ResourceNotFoundException;

    /**
     * Met à jour les informations d'une voiture existante.
     *
     * @param id L'identifiant de la voiture à mettre à jour.
     * @param carRequestDto Le DTO de requête contenant les nouvelles informations de la voiture.
     * @return Un DTO de réponse contenant les informations mises à jour de la voiture.
     * @throws ResourceNotFoundException Si la voiture ou l'utilisateur associé n'est pas trouvé.
     */
    CarResponseDto updateCar(Integer id, CarRequestDto carRequestDto) throws ResourceNotFoundException;

    /**
     * Supprime une voiture par son identifiant.
     *
     * @param id L'identifiant de la voiture à supprimer.
     * @throws ResourceNotFoundException Si aucune voiture n'est trouvée avec l'identifiant spécifié.
     */
    void deleteCar(Integer id) throws ResourceNotFoundException;

    /**
     * Récupère les voitures associées à un identifiant d'utilisateur donné, avec une limite sur le nombre de résultats.
     *
     * @param userId L'identifiant de l'utilisateur dont on veut récupérer les voitures.
     * @param limit Le nombre maximum de voitures à retourner.
     * @return Une liste de DTOs de réponse contenant les informations des voitures de l'utilisateur.
     */
    @Transactional(readOnly = true)
    List<CarResponseDto> getCarsByUserId(Integer userId, int limit);

    /**
     * Recherche des voitures dont la marque contient une chaîne de caractères spécifiée, avec une limite sur le nombre de résultats.
     *
     * @param marque La chaîne de caractères à rechercher dans les marques de voitures.
     * @param limit Le nombre maximum de voitures à retourner.
     * @return Une liste de DTOs de réponse contenant les informations des voitures dont la marque correspond.
     */
    @Transactional(readOnly = true)
    List<CarResponseDto> searchCarsByMarque(String marque, int limit);

    /**
     * Recherche des voitures dont le modèle contient une chaîne de caractères spécifiée, avec une limite sur le nombre de résultats.
     *
     * @param modele La chaîne de caractères à rechercher dans les modèles de voitures.
     * @param limit Le nombre maximum de voitures à retourner.
     * @return Une liste de DTOs de réponse contenant les informations des voitures dont le modèle correspond.
     */
    @Transactional(readOnly = true)
    List<CarResponseDto> searchCarsByModele(String modele, int limit);

    /**
     * Récupère les voitures appartenant à une catégorie spécifiée, avec une limite sur le nombre de résultats.
     *
     * @param categories La catégorie de voitures à rechercher.
     * @param limit Le nombre maximum de voitures à retourner.
     * @return Une liste de DTOs de réponse contenant les informations des voitures de la catégorie spécifiée.
     */
    @Transactional(readOnly = true)
    List<CarResponseDto> getCarsByCategories(Categorie categories, int limit);
}