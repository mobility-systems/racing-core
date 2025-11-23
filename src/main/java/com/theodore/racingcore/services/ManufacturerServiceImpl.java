package com.theodore.racingcore.services;

import com.theodore.racingcore.entities.cars.Manufacturer;
import com.theodore.racingcore.exceptions.InvalidETagException;
import com.theodore.racingcore.mappers.ManufacturerMapper;
import com.theodore.racingcore.models.automobiles.manufacturers.requests.NewManufacturerRequestDto;
import com.theodore.racingcore.models.automobiles.manufacturers.requests.SearchManufacturersRequest;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.BasicManufacturerInfoResponseDto;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.ManufacturerResponseDto;
import com.theodore.racingcore.repositories.ManufacturerRepository;
import com.theodore.racingcore.repositories.specifications.ManufacturerSpecification;
import com.theodore.racingcore.utils.Utils;
import com.theodore.racingmodel.exceptions.NotFoundException;
import com.theodore.racingmodel.models.SearchResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository,
                                   ManufacturerMapper manufacturerMapper) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerMapper = manufacturerMapper;
    }

    @Override
    @Transactional
    public ManufacturerResponseDto createNewManufacturer(NewManufacturerRequestDto request) {
        var manufacturer = manufacturerMapper.toNewEntity(request);
        var savedManufacturer = manufacturerRepository.save(manufacturer);
        return manufacturerMapper.toResponse(savedManufacturer);
    }

    @Override
    @Transactional
    public ManufacturerResponseDto updateManufacturer(Long id, NewManufacturerRequestDto manufacturerDto, String ifMatch) {
        var manufacturer = getManufacturerByManufacturerId(id);

        if (manufacturer.getVersion() != Utils.parseIfMatch(ifMatch)) {
            throw new InvalidETagException();
        }

        manufacturer.setName(manufacturerDto.name());
        manufacturer.setCountry(manufacturerDto.country());

        var updatedManufacturer = manufacturerRepository.save(manufacturer);

        return manufacturerMapper.toResponse(updatedManufacturer);
    }

    @Override
    public ManufacturerResponseDto fetchManufacturerById(Long manufacturerId) {
        var manufacturer = getManufacturerByManufacturerId(manufacturerId);
        return manufacturerMapper.toResponse(manufacturer);
    }

    @Override
    public Manufacturer getManufacturerByManufacturerId(Long id) {
        return manufacturerRepository.getWithAllCarsById(id)
                .orElseThrow(() -> new NotFoundException("Manufacturer with id: " + id + " not found"));
    }

    @Override
    public SearchResponse<BasicManufacturerInfoResponseDto> searchManufacturers(SearchManufacturersRequest searchRequest,
                                                                                int page,
                                                                                int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name").ascending());
        Page<Manufacturer> filteredResults = manufacturerRepository.findAll(ManufacturerSpecification.filterCriteria(searchRequest), pageable);

        List<BasicManufacturerInfoResponseDto> results = filteredResults.stream()
                .map(manufacturerMapper::toBasicInfoResponse)
                .toList();

        SearchResponse<BasicManufacturerInfoResponseDto> response = new SearchResponse<>();

        response.setData(results);
        response.setPageNumber(filteredResults.getNumber());
        response.setPageSize(filteredResults.getSize());
        response.setTotalPages(filteredResults.getTotalPages());
        response.setTotalElements(filteredResults.getTotalElements());
        response.setFirst(filteredResults.isFirst());
        response.setLast(filteredResults.isLast());

        return response;
    }
}
