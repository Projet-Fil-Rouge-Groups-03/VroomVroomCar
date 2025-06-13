package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
     * @param categorie La catégorie de voitures à rechercher.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures appartenant à la catégorie spécifiée.
     */
    Page<CompanyCar> findByCategorie(Categorie categorie, Pageable pageable);


    /**
     * Trouve toutes les voitures appartenant à une catégorie spécifiée, avec pagination.
     *
     * @param immatriculation L'immatriculation de voitures à rechercher.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de voitures triées par l'immatriculation.
     */
    Optional<CompanyCar> findByImmatriculationContainingIgnoreCase(String immatriculation, Pageable pageable);

}
