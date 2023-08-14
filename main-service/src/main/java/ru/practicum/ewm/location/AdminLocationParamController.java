package ru.practicum.ewm.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.location.dto.LocationParamDtoRequest;
import ru.practicum.ewm.location.dto.LocationParamDtoResponse;
import ru.practicum.ewm.valid.Marker;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/location")
@Slf4j
@RequiredArgsConstructor
@Validated
public class AdminLocationParamController {
    private final LocationParamService locationParamService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public LocationParamDtoResponse add(@RequestBody @Validated(Marker.OnCreate.class) LocationParamDtoRequest dto) {
        log.info("Получен запрос POST /admin/location с параметрами dto = {}", dto);
        LocationParam location = locationParamService.add(LocationParamMapper.toLocation(dto));
        return LocationParamMapper.toLocationDtoResponse(location);
    }

    @PatchMapping("/{id}")
    public LocationParamDtoResponse update(@PathVariable("id") long locationParamId,
                                           @RequestBody @Validated(Marker.OnUpdate.class) LocationParamDtoRequest dto) {
        log.info("Получен запрос PATCH /admin/location/{id} с параметрами id = {}, dto = {}", locationParamId, dto);
        LocationParam locationParam = locationParamService.update(locationParamId, LocationParamMapper.toLocation(dto));
        return LocationParamMapper.toLocationDtoResponse(locationParam);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") long locationParamId) {
        log.info("Получен запрос DELETE /admin/location/{id} с параметрами id = {}", locationParamId);
        locationParamService.deleteById(locationParamId);
    }

    @DeleteMapping
    public void deleteAll() {
        log.info("Получен запрос DELETE /admin/location");
        locationParamService.deleteAll();
    }

    @GetMapping("/{id}")
    public LocationParamDtoResponse getById(@PathVariable("id") long locationParamId) {
        log.info("Получен запрос GET /admin/location/{id} с параметрами id = {}", locationParamId);
        LocationParam locationParam = locationParamService.getById(locationParamId);
        return LocationParamMapper.toLocationDtoResponse(locationParam);
    }

    @GetMapping
    public List<LocationParamDtoResponse> getAll() {
        log.info("Получен запрос GET /admin/location");
        List<LocationParam> locationParam = locationParamService.getAll();
        return locationParam.stream().map(LocationParamMapper::toLocationDtoResponse).collect(Collectors.toList());
    }
}
