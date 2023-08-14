package ru.practicum.ewm.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        oldLocationParam.setLat(newLocationParam.getLat() == null ? oldLocationParam.getLat() : newLocationParam.getLat());
        oldLocationParam.setLon(newLocationParam.getLon() == null ? oldLocationParam.getLon() : newLocationParam.getLon());
        oldLocationParam.setRadius(newLocationParam.getRadius() == null ? oldLocationParam.getRadius() : newLocationParam.getRadius());
        oldLocationParam.setName(newLocationParam.getName() == null ? oldLocationParam.getName() : newLocationParam.getName());
    }

    public LocationParam getById(long id) {
        return locationParamRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Локация с id = " + id + " не существует");
        });
    }

    @Override
    public List<LocationParam> getAll() {
        return locationParamRepository.findAll();
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
