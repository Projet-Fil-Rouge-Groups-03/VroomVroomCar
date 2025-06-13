package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController implements ISubscribeController{
    @Autowired
    SubscribeService subscribeService;

    @Override
    public ResponseEntity<List<SubscribeResponseDto>> findAll() {
        return ResponseEntity.ok(subscribeService.getAllSubscribes());
    }

    @Override
    public ResponseEntity<SubscribeResponseDto> findById(int id) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.getSubscribeById);
    }

    @Override
    public ResponseEntity<SubscribeResponseDto> addSubscribe(SubscribeRequestDto subscribe) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public ResponseEntity<SubscribeResponseDto> editSubscribe(int id, SubscribeRequestDto subscribe) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteSubscribe(int id) throws ResourceNotFoundException {
        return null;
    }
}
