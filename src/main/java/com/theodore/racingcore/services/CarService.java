package com.theodore.racingcore.services;

import com.theodore.racingcore.models.automobiles.cars.requests.CarModelRequestDto;
import com.theodore.racingcore.models.automobiles.cars.requests.CreateNewCarRequest;
import com.theodore.racingcore.models.automobiles.cars.requests.UpdateCarRequest;
import com.theodore.racingcore.models.automobiles.cars.responses.CarModelResponseDto;
import com.theodore.racingcore.models.automobiles.cars.responses.CarResponseDto;

public interface CarService {

    CarModelResponseDto createNewCarModel(CarModelRequestDto request);

    CarModelResponseDto updateCarModel(Long id, CarModelRequestDto request, String ifMatch);

    CarModelResponseDto findCarModelById(Long id);

    CarResponseDto insertNewCarInfo(CreateNewCarRequest request);

    CarResponseDto updateCar(Long id, UpdateCarRequest updateRequest, String ifMatch);

    CarResponseDto getCarById(Long id);
}
