package com.theodore.racingcore.services;

import com.theodore.racingcore.entities.cars.Manufacturer;
import com.theodore.racingcore.models.automobiles.manufacturers.requests.NewManufacturerRequestDto;
import com.theodore.racingcore.models.automobiles.manufacturers.requests.SearchManufacturersRequest;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.BasicManufacturerInfoResponseDto;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.ManufacturerResponseDto;
import com.theodore.infrastructure.common.models.SearchResponse;

public interface ManufacturerService {

    ManufacturerResponseDto createNewManufacturer(NewManufacturerRequestDto manufacturer);

    ManufacturerResponseDto updateManufacturer(Long id, NewManufacturerRequestDto manufacturer, String ifMatch);

    ManufacturerResponseDto fetchManufacturerById(Long manufacturerId);

    Manufacturer getManufacturerByManufacturerId(Long id);

    SearchResponse<BasicManufacturerInfoResponseDto> searchManufacturers(SearchManufacturersRequest searchRequest,
                                                                         int page,
                                                                         int pageSize);

}
