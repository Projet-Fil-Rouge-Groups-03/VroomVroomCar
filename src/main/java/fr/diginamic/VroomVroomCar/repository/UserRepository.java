package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
