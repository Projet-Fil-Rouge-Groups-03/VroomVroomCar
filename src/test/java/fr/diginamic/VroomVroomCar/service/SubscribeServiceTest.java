package fr.diginamic.VroomVroomCar.service;

import static org.junit.jupiter.api.Assertions.*;
import fr.diginamic.VroomVroomCar.dto.request.SubscribeRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.SubscribeResponseDto;
import fr.diginamic.VroomVroomCar.entity.Subscribe;
import fr.diginamic.VroomVroomCar.entity.Trip;
import fr.diginamic.VroomVroomCar.entity.User;
import fr.diginamic.VroomVroomCar.entity.SubscribeKey;
import fr.diginamic.VroomVroomCar.exception.ResourceNotFoundException;
import fr.diginamic.VroomVroomCar.mapper.SubscribeMapper;
import fr.diginamic.VroomVroomCar.repository.SubscribeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscribeServiceTest {
    @Mock
    private SubscribeRepository subscribeRepository;

    @Mock
    private SubscribeMapper subscribeMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SubscribeService subscribeService;

    private SubscribeRequestDto createSubscribeRequestDto() {
        SubscribeRequestDto requestDto = new SubscribeRequestDto();
        requestDto.setTripId(1);
        requestDto.setUserId(1);
        return requestDto;
    }

    private Subscribe createSubscribe() {
        Subscribe subscribe = new Subscribe();
        SubscribeKey subscribeKey = new SubscribeKey(1, 1);
        subscribe.setId(subscribeKey);

        Trip trip = new Trip();
        trip.setId(1);
        User user = new User();
        user.setId(1);

        subscribe.setTrip(trip);
        subscribe.setUser(user);
        return subscribe;
    }

    private SubscribeResponseDto createSubscribeResponseDto() {
        SubscribeResponseDto responseDto = new SubscribeResponseDto();
        Trip trip = new Trip();
        trip.setId(1);
        User user = new User();
        user.setId(1);
        responseDto.setTrip(trip);
        responseDto.setUser(user);
        responseDto.setDateInscription(new Date());
        return responseDto;
    }

    @Test
    void testGetAllSubscribes() {
        Subscribe subscribe = createSubscribe();
        SubscribeResponseDto responseDto = createSubscribeResponseDto();

        when(subscribeRepository.findAll()).thenReturn(Collections.singletonList(subscribe));
        when(subscribeMapper.toResponseDto(any(Subscribe.class))).thenReturn(responseDto);

        List<SubscribeResponseDto> result = subscribeService.getAllSubscribes();

        assertFalse(result.isEmpty());
        verify(subscribeRepository, times(1)).findAll();
    }

    @Test
    void testGetSubscribeById() throws ResourceNotFoundException {
        Subscribe subscribe = createSubscribe();
        SubscribeResponseDto responseDto = createSubscribeResponseDto();

        when(subscribeRepository.findById(anyInt())).thenReturn(Optional.of(subscribe));
        when(subscribeMapper.toResponseDto(any(Subscribe.class))).thenReturn(responseDto);

        SubscribeResponseDto result = subscribeService.getSubscribeById(1);

        assertNotNull(result);
        verify(subscribeRepository, times(1)).findById(anyInt());
    }

    @Test
    void testCreateSubscribe() throws ResourceNotFoundException {
        SubscribeRequestDto requestDto = createSubscribeRequestDto();
        Subscribe subscribe = createSubscribe();
        SubscribeResponseDto responseDto = createSubscribeResponseDto();

        when(subscribeMapper.toEntity(any(SubscribeRequestDto.class))).thenReturn(subscribe);
        when(subscribeRepository.save(any(Subscribe.class))).thenReturn(subscribe);
        when(subscribeMapper.toResponseDto(any(Subscribe.class))).thenReturn(responseDto);
        doNothing().when(notificationService).sendNotificationToOrganisateurOnSubscribe(any(Trip.class), any(User.class));

        SubscribeResponseDto result = subscribeService.createSubscribe(requestDto);

        assertNotNull(result);
        verify(subscribeRepository, times(1)).save(any(Subscribe.class));
        verify(notificationService, times(1)).sendNotificationToOrganisateurOnSubscribe(any(Trip.class), any(User.class));
    }

    @Test
    void testEditSubscribe() throws ResourceNotFoundException {
        int subscribeId = 1;
        SubscribeRequestDto requestDto = createSubscribeRequestDto();
        Subscribe existingSubscribe = createSubscribe();
        SubscribeResponseDto responseDto = createSubscribeResponseDto();

        when(subscribeRepository.findById(subscribeId)).thenReturn(Optional.of(existingSubscribe));
        when(subscribeRepository.save(any(Subscribe.class))).thenReturn(existingSubscribe);
        when(subscribeMapper.toResponseDto(any(Subscribe.class))).thenReturn(responseDto);

        SubscribeResponseDto result = subscribeService.editSubscribe(subscribeId, requestDto);

        assertNotNull(result);
        verify(subscribeRepository, times(1)).findById(subscribeId);
        verify(subscribeRepository, times(1)).save(existingSubscribe);
    }

    @Test
    void testDeleteSubscribe() throws ResourceNotFoundException {
        int subscribeId = 1;
        Subscribe subscribe = createSubscribe();

        when(subscribeRepository.findById(subscribeId)).thenReturn(Optional.of(subscribe));
        doNothing().when(notificationService).sendNotificationToOrganisateurOnUnsubscribe(any(Trip.class), any(User.class));
        doNothing().when(subscribeRepository).deleteById(subscribeId);

        String result = subscribeService.deleteSubscribe(subscribeId);

        assertEquals("inscription supprimée", result);
        verify(subscribeRepository, times(1)).deleteById(subscribeId);
        verify(notificationService, times(1)).sendNotificationToOrganisateurOnUnsubscribe(any(Trip.class), any(User.class));
    }

    @Test
    void testFindSubscribesByTrip() {
        Subscribe subscribe = createSubscribe();
        SubscribeResponseDto responseDto = createSubscribeResponseDto();

        when(subscribeRepository.findByTripId(anyInt())).thenReturn(Collections.singletonList(subscribe));
        when(subscribeMapper.toResponseDto(any(Subscribe.class))).thenReturn(responseDto);

        List<SubscribeResponseDto> result = subscribeService.findSubscribesByTrip(1);

        assertFalse(result.isEmpty());
        verify(subscribeRepository, times(1)).findByTripId(anyInt());
    }

    @Test
    void testFindSubscribesByUser() {
        Subscribe subscribe = createSubscribe();
        SubscribeResponseDto responseDto = createSubscribeResponseDto();

        when(subscribeRepository.findByUserId(anyInt())).thenReturn(Collections.singletonList(subscribe));
        when(subscribeMapper.toResponseDto(any(Subscribe.class))).thenReturn(responseDto);

        List<SubscribeResponseDto> result = subscribeService.findSubscribesByUser(1);

        assertFalse(result.isEmpty());
        verify(subscribeRepository, times(1)).findByUserId(anyInt());
    }
}