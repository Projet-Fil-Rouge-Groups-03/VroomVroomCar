package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ISubscribeService {
    public List<SubscribeResponseDto> getAllSubscribes();
    public SubscribeResponseDto getSubscribeById();
    public SubscribeResponseDto createSubscribe(SubscribeRequestDto subscribe);
    public SubscribeResponseDto editSubscribe(SubscribeRequestDto subscribe);
    public String deleteSubscribe(int id);

    @Nullable SubscribeResponseDto getSubscribeById(int id);
}
