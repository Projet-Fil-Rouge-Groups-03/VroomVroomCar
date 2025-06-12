package fr.diginamic.VroomVroomCar.service;

import fr.diginamic.VroomVroomCar.dto.request.TripRequestDto;
import fr.diginamic.VroomVroomCar.dto.response.TripResponseDto;
import fr.diginamic.VroomVroomCar.exception.FunctionnalException;

import java.time.LocalTime;
import java.util.List;

public interface ITripService {
    TripResponseDto createTrip(TripRequestDto tripRequestDto) throws FunctionnalException;
    List<TripResponseDto> getAllTrips();
    TripResponseDto getTripById(Integer id) throws FunctionnalException;
    TripResponseDto updateTrip(Integer id, TripRequestDto tripRequestDto) throws FunctionnalException;
    void deleteTrip(Integer id) throws FunctionnalException;
    LocalTime calculateArrivalTime(LocalTime heureDepart, String lieuDepart,
                                   String lieuArrivee, String villeDepart, String villeArrivee);
}
