package com.theodore.racingcore.mappers;

import com.theodore.racingcore.entities.cars.Drivetrain;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.DrivetrainResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {EngineMapper.class, ElectricMotorMapper.class})
public interface DrivetrainMapper {

    @Mapping(target = "hybridType", source = "hybridType.description")
    @Mapping(target = "transmission", source = "transmissionType.description")
    DrivetrainResponseDto toResponse(Drivetrain drivetrain);

}
