package com.theodore.racingcore.mappers;

import com.theodore.racingcore.entities.racing.Driver;
import com.theodore.racingcore.models.racing.respones.DriverResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DriverMapper {

    @Mapping(target = "laps", ignore = true)
        //@Mapping(target = "laps", source = "")//todo
    DriverResponseDto toResponse(Driver driver);

}
