package com.theodore.racingcore.mappers;

import com.theodore.racingcore.entities.cars.ElectricMotor;
import com.theodore.racingcore.entities.cars.Manufacturer;
import com.theodore.racingcore.models.automobiles.drivetrain.requests.CreateElectricMotorRequestDto;
import com.theodore.racingcore.models.automobiles.drivetrain.responses.ElectricMotorResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ElectricMotorMapper {

    @Mapping(target = "batteryType", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "horsepower", source = "electricMotorDto.horsepower")
    @Mapping(target = "torque", source = "electricMotorDto.torque")
    @Mapping(target = "manufacturer", source = "manufacturer")
    @Mapping(target = "motorCode", source = "electricMotorDto.motorCode")
    @Mapping(target = "batteryCapacity", source = "electricMotorDto.batteryCapacity")
    ElectricMotor toEntity(CreateElectricMotorRequestDto electricMotorDto, Manufacturer manufacturer);

    ElectricMotorResponseDto toResponse(ElectricMotor electricMotor);

}
