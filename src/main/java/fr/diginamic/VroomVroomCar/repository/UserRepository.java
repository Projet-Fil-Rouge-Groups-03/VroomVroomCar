package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByNom(String nom);
}
