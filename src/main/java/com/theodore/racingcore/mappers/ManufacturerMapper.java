package com.theodore.racingcore.mappers;

import com.theodore.racingcore.entities.cars.CarModel;
import com.theodore.racingcore.entities.cars.Manufacturer;
import com.theodore.racingcore.models.automobiles.cars.responses.ManufacturerCarModelResponseDto;
import com.theodore.racingcore.models.automobiles.manufacturers.requests.NewManufacturerRequestDto;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.BasicManufacturerInfoResponseDto;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.ManufacturerResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ManufacturerMapper {

    @Mapping(target = "reliabilityRating", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carModels", ignore = true)
    @Mapping(target = "version", ignore = true)
    Manufacturer toNewEntity(NewManufacturerRequestDto dto);

    ManufacturerCarModelResponseDto toManufacturerCarModelResponseDto(CarModel carModel);

    ManufacturerResponseDto toResponse(Manufacturer manufacturer);

    BasicManufacturerInfoResponseDto toBasicInfoResponse(Manufacturer manufacturer);

}
