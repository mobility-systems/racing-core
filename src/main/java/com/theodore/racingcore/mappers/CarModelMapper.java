package com.theodore.racingcore.mappers;

import com.theodore.racingcore.entities.cars.CarModel;
import com.theodore.racingcore.entities.cars.Manufacturer;
import com.theodore.racingcore.models.automobiles.cars.requests.CarModelRequestDto;
import com.theodore.racingcore.models.automobiles.cars.responses.CarModelResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ManufacturerMapper.class})
public interface CarModelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "carName", source = "dto.carName")
    @Mapping(target = "modelYear", source = "dto.modelYear")
    @Mapping(target = "generation", source = "dto.generation")
    @Mapping(target = "facelift", source = "dto.facelift")
    @Mapping(target = "manufacturer", source = "manufacturer")
    @Mapping(target = "internalCode", source = "dto.internalCode")
    @Mapping(target = "doors", source = "dto.doors")
    CarModel dtoToEntity(CarModelRequestDto dto, Manufacturer manufacturer);

    CarModelResponseDto toResponse(CarModel carModel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carName", source = "dto.carName")
    @Mapping(target = "modelYear", source = "dto.modelYear")
    @Mapping(target = "generation", source = "dto.generation")
    @Mapping(target = "facelift", source = "dto.facelift")
    @Mapping(target = "manufacturer", source = "manufacturer")
    @Mapping(target = "internalCode", source = "dto.internalCode")
    @Mapping(target = "doors", source = "dto.doors")
    void updateEntity(@MappingTarget CarModel carModel,
                          CarModelRequestDto dto,
                          Manufacturer manufacturer);

}
