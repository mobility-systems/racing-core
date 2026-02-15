package com.theodore.racingcore.services;

import com.theodore.racingcore.models.automobiles.cars.requests.CarModelRequestDto;
import com.theodore.racingcore.models.automobiles.cars.requests.CreateNewCarRequest;
import com.theodore.racingcore.models.automobiles.cars.requests.UpdateCarRequest;
import com.theodore.racingcore.models.automobiles.cars.responses.CarModelResponseDto;
import com.theodore.racingcore.models.automobiles.cars.responses.CarResponseDto;

public interface CarService {

    /**
     * Creates a car model which contains basic info: manufacturer, model name , year etc.
     */
    CarModelResponseDto createNewCarModel(CarModelRequestDto request);

    /**
     * Updates a car model which contains basic info: manufacturer, model name , year etc.
     *
     * @param id      The id of the car model that will be updated
     * @param request The updated info of the car model
     * @param ifMatch This is used to prevent different simultaneous requests updating the same entry
     */
    CarModelResponseDto updateCarModel(Long id, CarModelRequestDto request, String ifMatch);

    /**
     * Deletes a car model
     *
     * @param id      The id of the car model that will be deleted
     * @param ifMatch This is used to prevent different simultaneous requests affecting the same entry
     */
    void deleteCarModel(Long id, String ifMatch);

    CarModelResponseDto findCarModelById(Long id);

    /**
     * Creates a new complete car which contains all its info: car model, drivetrain , power figures etc.
     */
    CarResponseDto insertNewCarInfo(CreateNewCarRequest request);

    /**
     * Updates a car
     *
     * @param id      The id of the car that will be updated
     * @param request The updated info of the car
     * @param ifMatch This is used to prevent different simultaneous requests updating the same entry
     */
    CarResponseDto updateCar(Long id, UpdateCarRequest request, String ifMatch);

    /**
     * Deletes a car
     *
     * @param id      The id of the car that will be deleted
     * @param ifMatch This is used to prevent different simultaneous requests affecting the same entry
     */
    void deleteCar(Long id, String ifMatch);

    CarResponseDto getCarById(Long id);
}
