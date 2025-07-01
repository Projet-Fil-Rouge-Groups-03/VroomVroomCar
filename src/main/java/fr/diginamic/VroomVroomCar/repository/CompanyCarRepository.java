package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.dto.response.CompanyCarResponseDto;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Cette interface est un dépôt Spring Data JPA pour l'entité CompanyCar.
 * Elle fournit des méthodes pour effectuer des opérations de base de données sur les entités CompanyCar
 * avec support pour la pagination et l'optimisation des requêtes.
 */
@Repository
public interface CompanyCarRepository extends JpaRepository<CompanyCar, Integer> {


    /**
     * Trouve toutes les voitures associées à un identifiant d'utilisateur donné, avec pagination.
     *
     * @param userId L'identifiant de l'utilisateur dont on veut trouver les voitures.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures appartenant à l'utilisateur spécifié.
     */
    Page<CompanyCar> findByUserId(Integer userId, Pageable pageable);

    /**
     * Trouve toutes les voitures dont la marque contient la chaîne de caractères spécifiée,
     * en ignorant la casse, avec pagination.
     *
     * @param marque La chaîne de caractères à rechercher dans les marques de voitures.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures dont la marque contient la chaîne de caractères spécifiée.
     */
    Page<CompanyCar> findByMarqueContainingIgnoreCase(String marque, Pageable pageable);

    /**
     * Trouve toutes les voitures dont le modèle contient la chaîne de caractères spécifiée,
     * en ignorant la casse, avec pagination.
     *
     * @param modele La chaîne de caractères à rechercher dans les modèles de voitures.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures dont le modèle contient la chaîne de caractères spécifiée.
     */
    Page<CompanyCar> findByModeleContainingIgnoreCase(String modele, Pageable pageable);

    /**
     * Trouve toutes les voitures appartenant à une catégorie spécifiée, avec pagination.
     *
     * @param categories La catégorie de voitures à rechercher.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures appartenant à la catégorie spécifiée.
     */
    Page<CompanyCar> findByCategories(Categorie categories, Pageable pageable);


    /**
     * Trouve toutes les voitures appartenant à une catégorie spécifiée, avec pagination.
     *
     * @param immatriculation L'immatriculation de voitures à rechercher.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures triées par l'immatriculation.
     */
    Page<CompanyCar> findByImmatriculationContainingIgnoreCase(String immatriculation, Pageable pageable);

    /**
     * Vérifie si une voiture de service existe déjà avec l'immatriculation donnée.
     *
     * @param immatriculation L'immatriculation à vérifier.
     * @return true si une voiture avec cette immatriculation existe, sinon false.
     */
    boolean existsByImmatriculation(String immatriculation);

    @Query("SELECT cc FROM CompanyCar cc " +
            "WHERE (:marque IS NULL OR cc.marque = :marque) " +
            "AND (:modele IS NULL OR cc.modele = :modele) " +
            "AND (:nbDePlaces = 0 OR cc.nbDePlaces >= :nbDePlaces) " +
            "AND (:dateDebut IS NULL OR :dateFin IS NULL OR " +
            "     NOT EXISTS (" +
            "         SELECT 1 FROM Reservation r " +
            "         WHERE r.companyCar.id = cc.id " +
            "         AND r.dateFin >= :dateDebut " +
            "         AND r.dateDebut <= :dateFin" +
            "     ))")
    List<CompanyCarResponseDto> findCompanyCarWithFilters(
            @Param("marque") String marque,
            @Param("modele") String modele,
            @Param("nbDePlaces") int nbDePlaces,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin
    );
}
