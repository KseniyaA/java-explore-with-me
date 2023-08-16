package ru.practicum.ewm.location;

import java.util.List;

public interface LocationParamService {
    LocationParam add(LocationParam locationParam);

    LocationParam update(long locationParamId, LocationParam locationParam);

    LocationParam getById(long id);

    List<LocationParam> getAll(Integer from, Integer size);

    void deleteById(long locationParamId);

    void deleteAll();
}
