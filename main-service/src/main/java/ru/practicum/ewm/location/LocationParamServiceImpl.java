package ru.practicum.ewm.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.EntityNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationParamServiceImpl implements LocationParamService {

    private final LocationParamRepository locationParamRepository;

    @Transactional
    @Override
    public LocationParam add(LocationParam locationParam) {
        return locationParamRepository.save(locationParam);
    }

    @Transactional
    @Override
    public LocationParam update(long locationParamId, LocationParam newLocationParam) {
        LocationParam oldLocationParam = getById(locationParamId);
        merge(oldLocationParam, newLocationParam);
        return locationParamRepository.save(oldLocationParam);
    }

    @Transactional
    private void merge(LocationParam oldLocationParam, LocationParam newLocationParam) {
        Double lat = newLocationParam.getLat();
        if (lat != null) {
            oldLocationParam.setLat(lat);
        }
        Double lon = newLocationParam.getLon();
        if (lon != null) {
            oldLocationParam.setLon(lon);
        }
        Double radius = newLocationParam.getRadius();
        if (radius != null) {
            oldLocationParam.setRadius(radius);
        }
        String name = newLocationParam.getName();
        if (name != null && !name.isBlank()) {
            oldLocationParam.setName(name);
        }
    }

    public LocationParam getById(long id) {
        return locationParamRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Локация с id = " + id + " не существует");
        });
    }

    @Override
    public List<LocationParam> getAll(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("id").ascending());
        Page<LocationParam> locations = locationParamRepository.findAll(page);
        return locations.getContent();
    }

    @Transactional
    @Override
    public void deleteById(long locationParamId) {
        getById(locationParamId);
        locationParamRepository.deleteById(locationParamId);
    }

    @Transactional
    @Override
    public void deleteAll() {
        locationParamRepository.deleteAll();
    }
}
