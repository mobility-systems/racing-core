package com.theodore.racingcore.controllers;

import com.theodore.racingcore.models.automobiles.manufacturers.requests.NewManufacturerRequestDto;
import com.theodore.racingcore.models.automobiles.manufacturers.requests.SearchManufacturersRequest;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.BasicManufacturerInfoResponseDto;
import com.theodore.racingcore.models.automobiles.manufacturers.responses.ManufacturerResponseDto;
import com.theodore.racingcore.services.ManufacturerService;
import com.theodore.racingcore.utils.Utils;
import com.theodore.infrastructure.common.models.SearchResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturersController {

    private final ManufacturerService manufacturerService;

    public ManufacturersController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<ManufacturerResponseDto> insertNewManufacturer(@RequestBody @Valid NewManufacturerRequestDto request) {
        var response = manufacturerService.createNewManufacturer(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/by-id/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('SYS_ADMIN')")
    public ResponseEntity<ManufacturerResponseDto> updateManufacturer(@PathVariable Long id,
                                                                      @RequestBody @Valid NewManufacturerRequestDto request,
                                                                      @RequestHeader(value = "If-Match") String ifMatch) {
        var response = manufacturerService.updateManufacturer(id, request, ifMatch);

        return ResponseEntity.ok()
                .eTag(Utils.toEtag(response.version()))
                .body(response);
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<ManufacturerResponseDto> fetchManufacturerById(@PathVariable Long id) {
        var response = manufacturerService.fetchManufacturerById(id);
        return ResponseEntity.ok()
                .eTag(Utils.toEtag(response.version()))
                .body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResponse<BasicManufacturerInfoResponseDto>> searchManufacturers(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestBody SearchManufacturersRequest searchRequest
    ) {
        var response = manufacturerService.searchManufacturers(searchRequest, page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
