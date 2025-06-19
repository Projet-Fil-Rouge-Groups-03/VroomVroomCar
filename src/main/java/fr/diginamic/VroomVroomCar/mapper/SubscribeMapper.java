package fr.diginamic.VroomVroomCar.mapper;

import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import fr.diginamic.VroomVroomCar.entity.Subscribe;
import fr.diginamic.VroomVroomCar.entity.SubscribeKey;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.repository.TripRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscribeMapper {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TripRepository tripRepository;
    /**
     * Convertit un SubscribeRequestDto et une inscription en une entité Subscribe.
     *
     * @param subscribeRequestDto Le DTO de requête contenant les informations sur l'utilisateur.
     * @return Une nouvelle instance de Subscribe initialisée avec les données du DTO et de l'inscription.
     */
    public Subscribe toEntity(SubscribeRequestDto subscribeRequestDto) throws ResourceNotFoundException {
        User user = userIdToUser(subscribeRequestDto.getUserId());
        Trip trip = tripIdToTrip(subscribeRequestDto.getTripId());

        Subscribe subscribe = new Subscribe();
        subscribe.setId(new SubscribeKey(subscribeRequestDto.getUserId(), subscribeRequestDto.getTripId()));
        subscribe.setUser(user);
        subscribe.setTrip(trip);
        subscribe.setDateInscription(subscribeRequestDto.getDateInscription());

        return subscribe;
    }

    /**
     * Convertit une entité Subscribe en un SubscribeResponseDto.
     *
     * @param subscribe L'entité Subscribe à convertir.
     * @return Une nouvelle instance de SubscribeResponseDto initialisée avec les données de l'entité subscribe.
     */
    public SubscribeResponseDto toResponseDto(Subscribe subscribe){
        return new SubscribeResponseDto(
                subscribe.getUser().getId(),
                subscribe.getTrip().getId(),
                subscribe.getDateInscription());
    }

    /**
     * Met à jour une entité Subscribe existante avec les données d'un SubscribeRequestDto et d'une inscription.
     *
     * @param subscribe L'entité Subscribe à mettre à jour.
     * @param subscribeRequestDto Le DTO de requête contenant les nouvelles informations sur l'utilisateur.
     */
    public void updateEntity(Subscribe subscribe, SubscribeRequestDto subscribeRequestDto) throws ResourceNotFoundException {
        subscribe.setUser(userIdToUser(subscribeRequestDto.getUserId()));
        subscribe.setTrip(tripIdToTrip(subscribeRequestDto.getTripId()));
        subscribe.setDateInscription(subscribeRequestDto.getDateInscription());

    }

    private User userIdToUser(Integer id) throws ResourceNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("L'utilisateur renseigné n'existe pas"));
    }
    private Trip tripIdToTrip(Integer id) throws ResourceNotFoundException {
        return tripRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Le trajet renseigné n'existe pas"));
    }

}
