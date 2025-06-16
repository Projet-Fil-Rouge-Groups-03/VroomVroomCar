package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.CompanyCarRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Interface pour le service de gestion des voitures de société.
 */
public interface ICompanyCarService {

    /**
     * Récupère une voiture de société par son ID.
     *
     * @param id l'ID de la voiture de société à récupérer.
     * @return un DTO de réponse contenant les détails de la voiture de société.
     * @throws ResourceNotFoundException si la voiture n'est pas trouvée.
     */
    CompanyCarResponseDto getCompanyCarById(Integer id) throws ResourceNotFoundException;

    /**
     * Récupère toutes les voitures de société.
     *
     * @return une liste de DTOs de réponse contenant les détails des voitures de société.
     */
    @Transactional(readOnly = true)
    List<CompanyCarResponseDto> getAllCompanyCars();

    /**
     * Crée une nouvelle voiture de société.
     *
     * @param companyCarRequestDto le DTO de requête contenant les détails de la voiture de société à créer.
     * @return un DTO de réponse contenant les détails de la voiture de société créée.
     * @throws ResourceNotFoundException si l'utilisateur n'est pas trouvé.
     * @throws FunctionnalException si une voiture avec la même immatriculation existe déjà.
     */
    CompanyCarResponseDto createCompanyCar(CompanyCarRequestDto companyCarRequestDto) throws ResourceNotFoundException, FunctionnalException;

    /**
     * Met à jour une voiture de société existante.
     *
     * @param id l'ID de la voiture de société à mettre à jour.
     * @param companyCarRequestDto le DTO de requête contenant les nouveaux détails de la voiture de société.
     * @return un DTO de réponse contenant les détails de la voiture de société mise à jour.
     * @throws ResourceNotFoundException si la voiture ou l'utilisateur n'est pas trouvé.
     */
    CompanyCarResponseDto updateCar(Integer id,CompanyCarRequestDto companyCarRequestDto) throws ResourceNotFoundException;

    /**
     * Supprime une voiture de société par son ID.
     *
     * @param id l'ID de la voiture de société à supprimer.
     * @throws ResourceNotFoundException si la voiture n'est pas trouvée.
     */
    void deleteCar(Integer id) throws ResourceNotFoundException;

    /**
     * Recherche des voitures de société par marque.
     *
     * @param marque la marque des voitures à rechercher.
     * @param limit le nombre maximum de résultats à retourner.
     * @return une liste de DTOs de réponse contenant les détails des voitures de société.
     */
    @Transactional(readOnly = true)
    List<CompanyCarResponseDto> searchCarsByMarque(String marque,int limit);

    /**
     * Recherche des voitures de société par modèle.
     *
     * @param modele le modèle des voitures à rechercher.
     * @param limit le nombre maximum de résultats à retourner.
     * @return une liste de DTOs de réponse contenant les détails des voitures de société.
     */
    @Transactional(readOnly = true)
    List<CompanyCarResponseDto> searchCarsByModele(String modele,int limit);

    /**
     * Récupère des voitures de société par catégorie.
     *
     * @param categorie la catégorie des voitures à récupérer.
     * @param limit le nombre maximum de résultats à retourner.
     * @return une liste de DTOs de réponse contenant les détails des voitures de société.
     */
    @Transactional(readOnly = true)
    List<CompanyCarResponseDto> getCarsByCategorie(Categorie categorie,int limit);

    /**
     * Recherche des voitures de société par immatriculation.
     *
     * @param immatriculation l'immatriculation des voitures à rechercher.
     * @param limit le nombre maximum de résultats à retourner.
     * @return une liste de DTOs de réponse contenant les détails des voitures de société.
     */
    @Transactional(readOnly = true)
    List<CompanyCarResponseDto> searchCarsByImmatriculation(String immatriculation,int limit);
}
