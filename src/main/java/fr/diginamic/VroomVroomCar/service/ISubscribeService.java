package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * Interface définissant les opérations de service pour la gestion des inscriptions
 * d'utilisateurs à des trajets.
 */
public interface ISubscribeService {
    /**
     * Récupère la liste de toutes les inscriptions existantes.
     *
     * @return une liste d'objets {@link SubscribeResponseDto} représentant toutes les inscriptions.
     */
    public List<SubscribeResponseDto> getAllSubscribes();
    /**
     * Récupère une inscription spécifique à partir de son identifiant.
     *
     * @param userId l'identifiant du User inscrit.
     * @param tripId l'identifiant du trajet concerné.
     * @return l'objet {@link SubscribeResponseDto} correspondant à l'inscription.
     * @throws ResourceNotFoundException si aucune inscription n'est trouvée avec cet identifiant.
     */
    public SubscribeResponseDto getSubscribeById(Integer userId, Integer tripId) throws ResourceNotFoundException;
    /**
     * Crée une nouvelle inscription d'un utilisateur à un trajet en fonction des données fournies.
     *
     * @param subscribe les informations de l'inscription à créer.
     * @return l'objet {@link SubscribeResponseDto} représentant l'inscription créée.
     * @throws ResourceNotFoundException si les entités liées (utilisateur ou trajet) ne sont pas trouvées.
     */
    public SubscribeResponseDto createSubscribe(SubscribeRequestDto subscribe) throws ResourceNotFoundException;
    /**
     * Modifie une inscription existante à un trajet à partir de son identifiant.
     *
     * @param userId l'identifiant du User inscrit.
     * @param tripId l'identifiant du trajet concerné.
     * @param subscribe les nouvelles données de l'inscription.
     * @return l'objet {@link SubscribeResponseDto} mis à jour.
     * @throws ResourceNotFoundException si l'inscription ou les entités liées ne sont pas trouvées.
     */
    public SubscribeResponseDto editSubscribe(Integer userId, Integer tripId, SubscribeRequestDto subscribe) throws ResourceNotFoundException;
    /**
     * Supprime une inscription à un trajet à partir de son identifiant.
     *
     * @param userId l'identifiant du User inscrit.
     * @param tripId l'identifiant du trajet concerné.
     * @return un message de confirmation de suppression.
     * @throws ResourceNotFoundException si aucune inscription n'est trouvée avec cet identifiant.
     */
    public String deleteSubscribe(Integer userId, Integer tripId) throws ResourceNotFoundException;

    /**
     * Récupère les inscriptions pour un trajet donné
     *
     * @param id        l'identifiant du trajet
     * @return          Une liste d'objets {@link SubscribeResponseDto} représentant toutes les inscriptions pour un trajet donné.
     */
    public List<SubscribeResponseDto> findSubscribesByTrip(Integer id);
    /**
     * Récupère les inscriptions pour un utilisateur donné
     *
     * @param id        l'identifiant de l'utilisateur
     * @return          Une liste d'objets {@link SubscribeResponseDto} représentant toutes les inscriptions pour un utilisateur donné.
     */
    public List<SubscribeResponseDto> findSubscribesByUser(Integer id);
}
