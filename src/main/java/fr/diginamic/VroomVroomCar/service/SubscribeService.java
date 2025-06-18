package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import fr.diginamic.VroomVroomCar.entity.Subscribe;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.SubscribeMapper;
import fr.diginamic.VroomVroomCar.repository.SubscribeRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    public SubscribeResponseDto getSubscribeById(int id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "l'inscription");
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("inscription non trouvée avec l'ID: " + id));
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
    public SubscribeResponseDto editSubscribe(int id, SubscribeRequestDto subscribe) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "l'inscription");
        ValidationUtil.validateSubscribeRequestDto(subscribe);

        Subscribe existingSubscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("inscription non trouvée avec l'ID: " + id));

        subscribeMapper.updateEntity(existingSubscribe, subscribe);
        return subscribeMapper.toResponseDto(subscribeRepository.save(existingSubscribe));
    }

    @Override
    public String deleteSubscribe(int id) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(id, "l'inscription");
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("inscription non trouvée avec l'ID: " + id));

        Trip trip = subscribe.getTrip();
        User participant = subscribe.getUser();
        notificationService.sendNotificationToOrganisateurOnUnsubscribe(trip, participant);

        subscribeRepository.deleteById(id);
        return "inscription supprimée";
    }
    @Transactional(readOnly = true)
    @Override
    public List<SubscribeResponseDto> findSubscribesByTrip(int id) {
        return subscribeRepository.findByTripId(id).stream()
                .map(subscribeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscribeResponseDto> findSubscribesByUser(int id) {
        return subscribeRepository.findByUserId(id).stream()
                .map(subscribeMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
