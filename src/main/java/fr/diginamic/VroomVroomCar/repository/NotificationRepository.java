package fr.diginamic.VroomVroomCar.repository;

import fr.diginamic.VroomVroomCar.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    /**
     * Trouve toutes les notifications d'un utilisateur par son ID, triées par date décroissante
     *
     * @param userId L'identifiant de l'utilisateur dont on veut trouver les notifications.
     * @param pageable Les informations de pagination et de tri.
     * @return Une page de notifications appartenant à l'utilisateur.
     */
    Page<Notification> findByUserIdOrderByDateDesc(Integer userId, Pageable pageable);

}
