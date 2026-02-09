package com.theodore.racingcore.mappers;

import com.theodore.racingcore.entities.racing.Track;
import com.theodore.racingcore.models.racing.requests.CreateNewTrackRequestDto;
import com.theodore.racingcore.models.racing.respones.TrackResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TrackMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    Track toEntity(CreateNewTrackRequestDto newTrackRequestDto);

    TrackResponseDto toResponse(Track track);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "dateCreated",  ignore = true)
    @Mapping(target = "dateUpdated",  ignore = true)
    void updateEntity(@MappingTarget Track track, CreateNewTrackRequestDto dto);

}
