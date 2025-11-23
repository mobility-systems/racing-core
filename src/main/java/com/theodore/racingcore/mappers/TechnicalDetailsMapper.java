package com.theodore.racingcore.mappers;

import com.theodore.racingcore.entities.cars.CarSpecification;
import com.theodore.racingcore.entities.cars.TechnicalDetails;
import com.theodore.racingcore.models.automobiles.cars.requests.IceTechnicalDetailsDto;
import com.theodore.racingcore.models.automobiles.cars.requests.TechnicalDetailsDto;
import com.theodore.racingcore.models.automobiles.cars.responses.TechnicalDetailsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TechnicalDetailsMapper {

    @Mapping(target = "electricRange", ignore = true)
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "zeroToOneHundred", source = "technicalDetailsDto.zeroToOneHundred")
    @Mapping(target = "topSpeed", source = "technicalDetailsDto.topSpeed")
    @Mapping(target = "cityConsumption", source = "iceTechnicalDetailsDto.cityConsumption")
    @Mapping(target = "highwayConsumption", source = "iceTechnicalDetailsDto.highwayConsumption")
    @Mapping(target = "weight", source = "technicalDetailsDto.weight")
    @Mapping(target = "wheelsDriven", source = "technicalDetailsDto.wheelsDriven")
    @Mapping(target = "drivetrainPlace", source = "technicalDetailsDto.drivetrainPlace")
    @Mapping(target = "carSpecification", source = "carSpecs")
    TechnicalDetails newIceCarDtoToEntity(TechnicalDetailsDto technicalDetailsDto,
                                          IceTechnicalDetailsDto iceTechnicalDetailsDto,
                                          CarSpecification carSpecs);

    @Mapping(target = "electricRange", ignore = true)
    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "carSpecification", ignore = true)
    @Mapping(target = "zeroToOneHundred", source = "technicalDetailsDto.zeroToOneHundred")
    @Mapping(target = "topSpeed", source = "technicalDetailsDto.topSpeed")
    @Mapping(target = "cityConsumption", source = "iceTechnicalDetailsDto.cityConsumption")
    @Mapping(target = "highwayConsumption", source = "iceTechnicalDetailsDto.highwayConsumption")
    @Mapping(target = "weight", source = "technicalDetailsDto.weight")
    @Mapping(target = "wheelsDriven", source = "technicalDetailsDto.wheelsDriven")
    @Mapping(target = "drivetrainPlace", source = "technicalDetailsDto.drivetrainPlace")
    void updateIceCarEntity(@MappingTarget TechnicalDetails target,
                            TechnicalDetailsDto technicalDetailsDto,
                            IceTechnicalDetailsDto iceTechnicalDetailsDto);

    TechnicalDetailsResponseDto toResponse(TechnicalDetails technicalDetails);

}
