package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.CarRequestDto;
import fr.diginamic.VroomVroomCar.dto.request.NotificationRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.CarResponseDto;
import fr.diginamic.VroomVroomCar.dto.response.NotificationResponseDto;
import fr.diginamic.VroomVroomCar.entity.Categorie;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController implements INotificationController {

    private final NotificationService notificationService;

    // GET by User ID
    @GetMapping("/user/{userId}")
    @Override
    public ResponseEntity<List<NotificationResponseDto>> getUserNotifications(@PathVariable Integer userId, @RequestParam(defaultValue = "5") int size) {
        List<NotificationResponseDto> notifications = notificationService.getUserNotifications(userId, size);
        return ResponseEntity.ok(notifications);
    }

    // CREATE

    @PostMapping("/create")
    @Override
    public ResponseEntity<NotificationResponseDto> createNotification(@Valid @RequestBody NotificationRequestDto dto) throws ResourceNotFoundException {
        NotificationResponseDto createdNotification = notificationService.createNotification(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }


    // DELETE
    @DeleteMapping("/delete/{id}")
    @Override
    public ResponseEntity<String> deleteNotification(@PathVariable Integer id) throws ResourceNotFoundException {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification supprimée avec succès");
    }

}
