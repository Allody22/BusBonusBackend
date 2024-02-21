package ru.nsu.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.nsu.services.IRaceService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Tag(name = "6. Race Controller", description = "API для работы с рейсами, их получение, их отслеживание")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/v1/")
public class RaceController {

    private IRaceService raceService;

    @Autowired
    public RaceController(IRaceService raceService) {
        this.raceService = raceService;
    }

    @GetMapping("/countries")
    @Transactional
    public ResponseEntity<?> getCountries() {
        return ResponseEntity.ok(raceService.getCountries());
    }


    @GetMapping("/regions/{countryId}")
    @Transactional
    public ResponseEntity<?> getRegions(@Valid @NotNull @PathVariable Long countryId) {
        return ResponseEntity.ok(raceService.getRegions(countryId));
    }

    @GetMapping("/depot/{depotId}")
    @Transactional
    public ResponseEntity<?> getDepot(@Valid @NotNull @PathVariable Long depotId) {
        return ResponseEntity.ok(raceService.getDepotInfo(depotId));
    }

    @GetMapping("/dispatch_points/{regionId}")
    @Transactional
    public ResponseEntity<?> getDispatchPoints(@Valid @NotNull @PathVariable Long regionId) {
        return ResponseEntity.ok(raceService.getDispatchPoints(regionId));
    }

    @GetMapping("/arrival_points/{dispatchPointId}")
    @Transactional
    public ResponseEntity<?> getArrivalPoint(@Valid @NotNull @PathVariable Long dispatchPointId) {
        return ResponseEntity.ok(raceService.getArrivalPoints(dispatchPointId));
    }

    @GetMapping("/races/{dispatchPointId}/{arrivalPointId}/{date}")
    public ResponseEntity<?> getRaceDetails(
            @PathVariable("dispatchPointId") Long dispatchPointId,
            @PathVariable("arrivalPointId") Long arrivalPointId,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(raceService.getRacesByPointAndDay(dispatchPointId, arrivalPointId, date));
    }

    @GetMapping("/race/summary/{uid}")
    public ResponseEntity<?> getRaceFullInfo(
            @PathVariable("uid") String uid) {
        return ResponseEntity.ok(raceService.getRaceFullInfo(uid));
    }
}
