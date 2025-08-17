package routine.log.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import routine.log.dto.filter.FilterRequestDto;
import routine.log.geometry.GeoUtils;
import routine.log.domain.Place;
import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.repository.PlaceRepository;

@Service
@Slf4j
@AllArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public Place createPlace(RoutineCreateRequestDto dto){

        Point point = GeoUtils.toPoint(dto.getLatitude(), dto.getLongitude());

        Place place = Place.builder()
                .center(point)
                .build();

        log.info("Place 생성 완료");


        return placeRepository.save(place);

    }


    public void inRange(FilterRequestDto dto, Point point){



    }
}
