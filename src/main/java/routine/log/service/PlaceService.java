package routine.log.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import routine.log.dto.filter.CurrentRequestDto;
import routine.log.dto.filter.FilterRequestDto;
import routine.log.exception.CreationException;
import routine.log.geometry.GeoUtils;
import routine.log.domain.Place;
import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.repository.PlaceRepository;

@Service
@Slf4j
@AllArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Transactional
    public Place createPlace(@Valid RoutineCreateRequestDto dto){

        Point point = GeoUtils.toPoint(dto.getLatitude(), dto.getLongitude());

        Place place = Place.builder()
                .center(point)
                .build();

        log.info("Place 생성 완료 위도,경도 = [{},{}]", dto.getLatitude(), dto.getLongitude());



        try {
            return placeRepository.save(place);
        } catch (DataAccessException e) {
            String msg = String.format(
                    "Place 생성 실패 (location=%s, lat=%s, lng=%s)",
                    dto.getLocation(), String.valueOf(dto.getLatitude()), String.valueOf(dto.getLongitude())
            );
            log.error(msg, e);
            throw new CreationException(msg, e);
        }


    }


    public boolean inRange(FilterRequestDto dto, Point point){


        boolean inRange = GeoUtils.isWithinRadius(
                dto.getLatitude(), dto.getLongitude(),
                point.getX(), point.getY(),
                45.0
        );
        log.info("위치가 범위 안에 존재한다 = {} 루틴의 위도,경도 [{},{}], 현재 위도,경도[{},{}]", inRange, dto.getLatitude(), dto.getLongitude(), point.getX(), point.getY());

        return inRange;

    }

    public boolean inRange(CurrentRequestDto dto, Point point){


        boolean withinRange = GeoUtils.isWithinRadius(
                dto.getLatitude(), dto.getLongitude(),
                point.getX(), point.getY(),
                45.0
        );

        log.info("위치가 범위 안에 존재한다: {}",withinRange);

        return withinRange;

    }
}
