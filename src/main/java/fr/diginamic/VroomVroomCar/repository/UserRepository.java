package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByNom(String nom);
    Optional<User> findByMail(String email);

    Page<User> findByNomContainingIgnoreCase(String nom, Pageable pageable);
}
