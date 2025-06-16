package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Cette interface est un dépôt Spring Data JPA pour l'entité Car.
 * Elle fournit des méthodes pour effectuer des opérations de base de données sur les entités Car
 * avec support pour la pagination et l'optimisation des requêtes.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    /**
     * Trouve toutes les voitures associées à un identifiant d'utilisateur donné, avec pagination.
     *
     * @param userId L'identifiant de l'utilisateur dont on veut trouver les voitures.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures appartenant à l'utilisateur spécifié.
     */
    Page<Car> findByUserId(Integer userId, Pageable pageable);

    /**
     * Trouve toutes les voitures dont la marque contient la chaîne de caractères spécifiée,
     * en ignorant la casse, avec pagination.
     *
     * @param marque La chaîne de caractères à rechercher dans les marques de voitures.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures dont la marque contient la chaîne de caractères spécifiée.
     */
    Page<Car> findByMarqueContainingIgnoreCase(String marque, Pageable pageable);

    /**
     * Trouve toutes les voitures dont le modèle contient la chaîne de caractères spécifiée,
     * en ignorant la casse, avec pagination.
     *
     * @param modele La chaîne de caractères à rechercher dans les modèles de voitures.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures dont le modèle contient la chaîne de caractères spécifiée.
     */
    Page<Car> findByModeleContainingIgnoreCase(String modele, Pageable pageable);

    /**
     * Trouve toutes les voitures appartenant à une catégorie spécifiée, avec pagination.
     *
     * @param categories La catégorie de voitures à rechercher.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures appartenant à la catégorie spécifiée.
     */
    Page<Car> findByCategories(Categorie categories, Pageable pageable);


    /**
     * Trouve une voiture par son identifiant avec chargement optimisé de l'utilisateur associé.
     *
     * @param id L'identifiant de la voiture à rechercher.
     * @return Un Optional contenant la voiture si elle est trouvée, sinon un Optional vide.
     */
    @EntityGraph(attributePaths = {"user"})
    Optional<Car> findById(Integer id);

    /**
     * Trouve une voiture par son identifiant et l'identifiant de son utilisateur,
     * avec chargement optimisé de l'utilisateur associé.
     *
     * @param carId L'identifiant de la voiture à rechercher.
     * @param userId L'identifiant de l'utilisateur propriétaire de la voiture.
     * @return Un Optional contenant la voiture si elle est trouvée, sinon un Optional vide.
     */
    @Query("SELECT c FROM Car c WHERE c.id = :carId AND c.user.id = :userId")
    @EntityGraph(attributePaths = {"user"})
    Optional<Car> findByIdAndUserId(@Param("carId") Integer carId, @Param("userId") Integer userId);
}
