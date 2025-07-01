package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscribe")
public class SubscribeController implements ISubscribeController{
    @Autowired
    SubscribeService subscribeService;

    @GetMapping
    @Override
    public ResponseEntity<List<SubscribeResponseDto>> findAll() {
        return ResponseEntity.ok(subscribeService.getAllSubscribes());
    }

    @GetMapping("/{userId}/{tripId}")
    @Override
    public ResponseEntity<SubscribeResponseDto> findById(@PathVariable Integer userId, @PathVariable Integer tripId) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.getSubscribeById(userId, tripId));
    }

    @GetMapping("/find-by-trip/{id}")
    @Override
    public ResponseEntity<List<SubscribeResponseDto>> findByTrip(@PathVariable Integer id) {
        return ResponseEntity.ok(subscribeService.findSubscribesByTrip(id));
    }

    @GetMapping("/ind-by-user/{id}")
    @Override
    public ResponseEntity<List<SubscribeResponseDto>> findByUser(@PathVariable Integer id){
        return ResponseEntity.ok(subscribeService.findSubscribesByUser(id));
    }

    @PostMapping("/create")
    @Override
    public ResponseEntity<SubscribeResponseDto> addSubscribe(@RequestBody SubscribeRequestDto subscribe) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.createSubscribe(subscribe));
    }

    @PutMapping("/update/{userId}/{tripId}")
    @Override
    public ResponseEntity<SubscribeResponseDto> editSubscribe(@PathVariable Integer userId, @PathVariable Integer tripId, @RequestBody SubscribeRequestDto subscribe) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.editSubscribe(userId, tripId, subscribe));
    }

    @DeleteMapping("/delete/{userId}/{tripId}")
    @Override
    public ResponseEntity<String> deleteSubscribe(@PathVariable Integer userId, @PathVariable Integer tripId) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.deleteSubscribe(userId, tripId));
    }

}
