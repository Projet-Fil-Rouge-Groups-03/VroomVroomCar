package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.dto.request.UserRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.UserResponseDto;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController implements IUserController {
    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> findAll(){
        return  ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@RequestParam int id) throws ResourceNotFoundException {
            return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/find-by-nom/{nom}")
    public ResponseEntity<UserResponseDto> findByNom(String nom) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getByNom(nom));
    }

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto user) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.createUser(user));
    }
    @PutMapping("/edit-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> editUser(@RequestParam int id ,@RequestBody UserRequestDto user) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.updateUser(id,user));
    }

    @PutMapping("/edit-user-by-nom/{nom}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> editUser(@RequestParam String nom ,@RequestBody UserRequestDto user) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.updateUser(nom,user));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@RequestParam int id) throws ResourceNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.ok("L'utilisateur à l'id : " + id + " à bien été supprimé");
    }

}
