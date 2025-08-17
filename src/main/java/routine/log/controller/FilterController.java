package routine.log.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import routine.log.domain.Place;
import routine.log.domain.Routine;
import routine.log.dto.filter.FilterRequestDto;
import routine.log.repository.RoutineRepository;
import routine.log.service.PlaceService;
import routine.log.service.RangeService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class FilterController {

    final private PlaceService placeService;
    final private RoutineRepository routineRepository;

//    @GetMapping("/currentFilter")
//    public ResponseEntity<?> withinRange(@RequestParam Long routineId,
//                                         @RequestBody FilterRequestDto dto){
//
//        Optional<Routine> routine = routineRepository.findById(routineId);
//        Place place = routine.orElseThrow().getPlace();
//
//        placeService.inRange(dto, place.getCenter());
//
//
//
//    }
}
