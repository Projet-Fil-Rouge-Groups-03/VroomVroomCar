package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.NotificationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.NotificationResponseDto;
import fr.diginamic.VroomVroomCar.entity.Notification;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.NotificationMapper;
import fr.diginamic.VroomVroomCar.repository.NotificationRepository;
import fr.diginamic.VroomVroomCar.repository.UserRepository;
import fr.diginamic.VroomVroomCar.util.ValidationUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;


    // GET by User ID

    @Transactional(readOnly = true)
    @Override
    public List<NotificationResponseDto> getUserNotifications(Integer userId, int limit) throws ResourceNotFoundException {
        ValidationUtil.validateIdNotNull(userId, "l'utilisateur");
        Pageable pageable = PageRequest.of(0, limit);
        return notificationRepository.findByUserIdOrderByDateDesc(userId, pageable).stream()
                .map(notificationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // CREATE

    @Override
    public NotificationResponseDto createNotification(NotificationRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + dto.getUserId()));
        Notification notification = notificationMapper.toEntity(dto, user);
        Notification savedNotification = notificationRepository.save(notification);
        return notificationMapper.toResponseDto(savedNotification);
    }

    // DELETE (au cas où)

    @Override
    public void deleteNotification(Integer id) {
        notificationRepository.deleteById(id);
    }

}
