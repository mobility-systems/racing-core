package com.theodore.racingcore.controllers;

import com.theodore.racingcore.models.automobiles.cars.requests.CarModelRequestDto;
import com.theodore.racingcore.models.automobiles.cars.requests.CreateNewCarRequest;
import com.theodore.racingcore.models.automobiles.cars.requests.UpdateCarRequest;
import com.theodore.racingcore.models.automobiles.cars.responses.CarModelResponseDto;
import com.theodore.racingcore.models.automobiles.cars.responses.CarResponseDto;
import com.theodore.racingcore.services.CarService;
import com.theodore.racingcore.utils.Utils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cars")
public class CarsController {

    private final CarService carService;

    public CarsController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/model/create")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<CarModelResponseDto> insertNewCarModel(@RequestBody @Valid CarModelRequestDto request) {
        var response = carService.createNewCarModel(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/model/by-id/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/model/update/{id}")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<CarModelResponseDto> updateCarModel(@PathVariable Long id,
                                               @RequestBody @Valid CarModelRequestDto request,
                                               @RequestHeader(value = "If-Match") String ifMatch) {
        var response = carService.updateCarModel(id, request, ifMatch);
        return ResponseEntity.ok()
                .eTag(Utils.toEtag(response.version()))
                .body(response);
    }

    @GetMapping("/model/by-id/{id}")
    public ResponseEntity<CarModelResponseDto> fetchCarModelById(@PathVariable Long id) {
        return ResponseEntity.ok().body(carService.findCarModelById(id));
    }

    @PostMapping("/create")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<CarResponseDto> insertNewCar(@RequestBody @Valid CreateNewCarRequest request) {
        var response = carService.insertNewCarInfo(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/by-id/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/update/{id}")
    //@PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<CarResponseDto> updateCar(@PathVariable Long id,
                                          @RequestBody @Valid UpdateCarRequest request,
                                          @RequestHeader(value = "If-Match") String ifMatch) {
        var response = carService.updateCar(id, request, ifMatch);
        return ResponseEntity.ok()
                .eTag(Utils.toEtag(response.version()))
                .body(response);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<CarResponseDto> fetchCarById(@PathVariable Long id) {
        return ResponseEntity.ok().body(carService.getCarById(id));
    }
}
