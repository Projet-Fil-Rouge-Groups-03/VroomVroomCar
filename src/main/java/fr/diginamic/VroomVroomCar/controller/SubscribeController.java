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
@RequestMapping("/subscribe")
public class SubscribeController implements ISubscribeController{
    @Autowired
    SubscribeService subscribeService;

    @GetMapping
    @Override
    public ResponseEntity<List<SubscribeResponseDto>> findAll() {
        return ResponseEntity.ok(subscribeService.getAllSubscribes());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<SubscribeResponseDto> findById(@RequestParam int id) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.getSubscribeById(id));
    }

    @GetMapping("/find-by-trip/{id}")
    @Override
    public ResponseEntity<List<SubscribeResponseDto>> findByTrip(@RequestParam int id) {
        return ResponseEntity.ok(subscribeService.findSubscribesByTrip(id));
    }

    @GetMapping("/ind-by-user/{id}")
    @Override
    public ResponseEntity<List<SubscribeResponseDto>> findByUser(@RequestParam int id){
        return ResponseEntity.ok(subscribeService.findSubscribesByUser(id));
    }

    @PostMapping("/create")
    @Override
    public ResponseEntity<SubscribeResponseDto> addSubscribe(@RequestBody SubscribeRequestDto subscribe) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.createSubscribe(subscribe));
    }

    @PutMapping("/update/{id}")
    @Override
    public ResponseEntity<SubscribeResponseDto> editSubscribe(@RequestParam int id, @RequestBody SubscribeRequestDto subscribe) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.editSubscribe(id, subscribe));
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public ResponseEntity<String> deleteSubscribe(@RequestParam int id) throws ResourceNotFoundException {
        return ResponseEntity.ok(subscribeService.deleteSubscribe(id));
    }

}
