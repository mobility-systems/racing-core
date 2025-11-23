package com.theodore.racingcore.mappers;

import com.theodore.racingcore.entities.cars.Engine;
import com.theodore.racingcore.entities.cars.Manufacturer;
import com.theodore.racingcore.models.automobiles.drivetrain.requests.CreateEngineRequestDto;
import com.theodore.racingcore.models.automobiles.drivetrain.requests.UpdateEngineRequestDto;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.EngineResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = ManufacturerMapper.class)
public interface EngineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "horsepower", source = "engine.horsepower")
    @Mapping(target = "torque", source = "engine.torque")
    @Mapping(target = "manufacturer", source = "manufacturer")
    @Mapping(target = "motorCode", source = "engine.motorCode")
    @Mapping(target = "engineType", source = "engine.engineType")
    @Mapping(target = "fuelType", source = "engine.fuelType")
    Engine toEntity(CreateEngineRequestDto engine, Manufacturer manufacturer);

    @Mapping(target = "manufacturer", source = "manufacturer")
    EngineResponseDto toResponse(Engine engine);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    @Mapping(target = "engineType", ignore = true)
    @Mapping(target = "fuelType", ignore = true)
    void updateEntity(@MappingTarget Engine engine, UpdateEngineRequestDto dto);

}
