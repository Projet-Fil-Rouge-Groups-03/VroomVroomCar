package fr.diginamic.VroomVroomCar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private final ResponseCookie cookie;
    private final UserResponseDto userDto;
}
