package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import fr.diginamic.VroomVroomCar.entity.Subscribe;
import fr.diginamic.VroomVroomCar.entity.SubscribeKey;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.SubscribeMapper;
import fr.diginamic.VroomVroomCar.repository.SubscribeRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscribeService implements ISubscribeService {
    @Autowired
    SubscribeMapper subscribeMapper;

    @Autowired
    SubscribeRepository subscribeRepository;

    @Autowired
    NotificationService notificationService;

    @Transactional(readOnly = true)
    @Override
    public List<SubscribeResponseDto> getAllSubscribes() {
        return subscribeRepository.findAll().stream()
                .map(subscribeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public SubscribeResponseDto getSubscribeById(Integer userId, Integer tripId) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(userId, "l'utilisateur");
        ValidationUtil.validateIdNotNull(tripId, "le trajet");

        SubscribeKey key = new SubscribeKey(userId, tripId);
        Subscribe subscribe = subscribeRepository.findById(key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "inscription non trouvée pour userId: " + userId + " et tripId: " + tripId));
        return subscribeMapper.toResponseDto(subscribe);
    }

    @Override
    public SubscribeResponseDto createSubscribe(SubscribeRequestDto subscribeRequestDto) throws ResourceNotFoundException {
        ValidationUtil.validateSubscribeRequestDto(subscribeRequestDto);

        Subscribe subscribe = subscribeMapper.toEntity(subscribeRequestDto);
        Subscribe saved = subscribeRepository.save(subscribe);

        Trip trip = saved.getTrip();
        User participant = saved.getUser();
        notificationService.sendNotificationToOrganisateurOnSubscribe(trip, participant);

        return subscribeMapper.toResponseDto(saved);
    }

    @Override
    public SubscribeResponseDto editSubscribe(Integer userId, Integer tripId, SubscribeRequestDto subscribeDto) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(userId, "l'utilisateur");
        ValidationUtil.validateIdNotNull(tripId, "le trajet");
        ValidationUtil.validateSubscribeRequestDto(subscribeDto);

        SubscribeKey key = new SubscribeKey(userId, tripId);
        Subscribe existingSubscribe = subscribeRepository.findById(key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "inscription non trouvée pour userId: " + userId + " et tripId: " + tripId));

        subscribeMapper.updateEntity(existingSubscribe, subscribeDto);
        return subscribeMapper.toResponseDto(subscribeRepository.save(existingSubscribe));
    }

    @Override
    public String deleteSubscribe(Integer userId, Integer tripId) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(userId, "l'utilisateur");
        ValidationUtil.validateIdNotNull(tripId, "le trajet");

        SubscribeKey key = new SubscribeKey(userId, tripId);
        Subscribe subscribe = subscribeRepository.findById(key)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "inscription non trouvée pour userId: " + userId + " et tripId: " + tripId));

        Trip trip = subscribe.getTrip();
        User participant = subscribe.getUser();
        notificationService.sendNotificationToOrganisateurOnUnsubscribe(trip, participant);

        subscribeRepository.deleteById(key);
        return "inscription supprimée";
    }

    @Transactional(readOnly = true)
    @Override
    public List<SubscribeResponseDto> findSubscribesByTrip(Integer id) {
        return subscribeRepository.findByTripId(id).stream()
                .map(subscribeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscribeResponseDto> findSubscribesByUser(Integer id) {
        return subscribeRepository.findByUserId(id).stream()
                .map(subscribeMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
