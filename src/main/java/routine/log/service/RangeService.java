package routine.log.service;

import lombok.AllArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import routine.log.dto.filter.FilterRequestDto;
import routine.log.repository.PlaceRepository;
import routine.log.repository.TimeRepository;

@Service
@AllArgsConstructor
public class RangeService {

    final private PlaceRepository placeRepository;
    final private TimeRepository timeRepository;

    public void inRange(FilterRequestDto dto, Point point){


    }
}
