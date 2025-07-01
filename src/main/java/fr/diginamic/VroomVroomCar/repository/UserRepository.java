package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Recherche un utilisateur par son nom exact.
     *
     * @param nom le nom exact de l'utilisateur
     * @return un Optional contenant l'utilisateur correspondant, s'il existe
     */
    Optional<User> findByNom(String nom);

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email l'adresse email de l'utilisateur
     * @return un Optional contenant l'utilisateur correspondant, s'il existe
     */
    Optional<User> findByMail(String email);

    /**
     * Recherche une page d'utilisateurs dont le nom contient une chaîne donnée,
     * sans tenir compte de la casse (majuscule/minuscule).
     *
     * @param nom      la chaîne à rechercher dans les noms d'utilisateur
     * @param pageable l'objet Pageable pour la pagination (ex : nombre de résultats, page)
     * @return une page de User correspondant aux critères
     */
    Page<User> findByNomContainingIgnoreCase(String nom, Pageable pageable);

}
