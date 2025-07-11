package fr.diginamic.VroomVroomCar.util;

import fr.diginamic.VroomVroomCar.dto.request.CarRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.entity.Car;
import fr.diginamic.VroomVroomCar.entity.CompanyCar;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;
import fr.diginamic.VroomVroomCar.repository.CarRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Cette classe utilitaire fournit des méthodes statiques pour valider divers types de données.
 * Elle est conçue pour être utilisée dans toute l'application afin de garantir la validité des entrées.
 */
@NoArgsConstructor
@Component
public final class ValidationUtil {

    /**
     * Valide un CarRequestDto pour s'assurer qu'il n'est pas null et que l'ID de l'utilisateur est présent.
     *
     * @param carRequestDto Le DTO de requête de voiture à valider.
     * @throws IllegalArgumentException si le DTO est null ou si l'ID de l'utilisateur est null.
     */
    public static void validateCarRequestDto(CarRequestDto carRequestDto) {
        if (carRequestDto == null) {
            throw new IllegalArgumentException("Le DTO de requête de voiture ne peut pas être null.");
        }
        if (carRequestDto.getUtilisateurId() == null) {
            throw new IllegalArgumentException("L'ID de l'utilisateur ne peut pas être null.");
        }
    }

    /**
     * Valide un CarRequestDto pour s'assurer qu'il n'est pas null et que l'ID de l'utilisateur est présent.
     *
     * @param userRequestDto Le DTO de requête d'utilisateur à valider.
     * @throws IllegalArgumentException si le DTO est null.
     */
    public static void validateUserRequestDto(UserRequestDto userRequestDto) {
        if (userRequestDto == null) {
            throw new IllegalArgumentException("Le DTO de requête d'utilisateur ne peut pas être null.");
        }
    }



    /**
     * Valide un identifiant d'utilisateur pour s'assurer qu'il n'est pas null.
     *
     * @param userId L'identifiant de l'utilisateur à valider.
     * @throws IllegalArgumentException si l'ID de l'utilisateur est null.
     */
    public static void validateUserId(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("L'ID de l'utilisateur ne peut pas être null.");
        }
    }

    /**
     * Valide une limite pour s'assurer qu'elle est supérieure à zéro.
     *
     * @param limit La limite à valider.
     * @throws IllegalArgumentException si la limite est inférieure ou égale à zéro.
     */
    public static void validateLimit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("La limite doit être supérieure à zéro.");
        }
    }

    /**
     * Valide une chaîne de caractères pour s'assurer qu'elle n'est pas vide ou null.
     * Ex : "Le champ voiture ne peut pas être vide".
     *
     * @param value La chaîne de caractères à valider.
     * @param fieldName Le nom du champ à utiliser dans le message d'erreur.
     * @throws IllegalArgumentException si la chaîne est vide ou null.
     */
    public static void validateStringNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Le champ " + fieldName + " ne peut pas être vide.");
        }
    }

    /**
     * Valide un identifiant pour s'assurer qu'il n'est pas null.
     * Ex : "L'ID de Voiture ne peut pas être null".
     *
     * @param id L'identifiant à valider.
     * @param entityName Le nom de l'entité à utiliser dans le message d'erreur.
     * @throws IllegalArgumentException si l'identifiant est null.
     *
     */
    public static void validateIdNotNull(Integer id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID de " + entityName + " ne peut pas être null.");
        }
    }

    /**
     * Valide un champ pour s'assurer qu'il n'est pas null.
     * Ex : "Categorie ne peut pas être null"
     *
     * @param fieldName Le nom du champ à valider.
     * @throws IllegalArgumentException si le champ est null.
     */
    public static void validateNotNull(String fieldName) {
        if (fieldName == null) {
            throw new IllegalArgumentException(fieldName + " ne peut pas être null.");
        }
    }
    public static void validateSubscribeRequestDto(SubscribeRequestDto subscribeRequestDto) {
        if (subscribeRequestDto == null) {
            throw new IllegalArgumentException("Le DTO de requête d'inscription ne peut pas être null.");
        }
    }
    public  static  void validateUserPassword(String password) {
        if (password == null || !password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial");
        }
    }

    public static void validateUserMail(String email){
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est requis");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
    }

    /**
     * Valide qu'une date de fin est postérieure à la date de début.
     */
    public void validateEndDateBeforeStartDate(Date dateDebut, Date dateFin) throws FunctionnalException {
        if (dateDebut != null && dateFin != null && dateFin.before(dateDebut)) {
            throw new FunctionnalException("La date de fin doit être postérieure à la date de début.");
        }
    }

    public boolean estVehiculeDeService(Integer idCar, CarRepository carRepository) throws FunctionnalException {
        Car car = carRepository.findById(idCar)
                .orElseThrow(() -> new FunctionnalException("Véhicule non trouvé"));

        return car instanceof CompanyCar;
    }


}

