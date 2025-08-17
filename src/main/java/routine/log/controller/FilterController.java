package routine.log.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import routine.log.State;
import routine.log.domain.*;
import routine.log.dto.filter.CurrentRequestDto;
import routine.log.dto.filter.FilterRequestDto;
import routine.log.exception.NotFoundException;
import routine.log.repository.RoutineRepository;
import routine.log.repository.RoutineStateRepository;
import routine.log.repository.UserRepository;
import routine.log.service.PlaceService;
import routine.log.service.TimeService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class FilterController {

    final private PlaceService placeService;
    final private RoutineRepository routineRepository;
    final private TimeService timeService;
    final private RoutineStateRepository routineStateRepository;
    final private UserRepository userRepository;

    @GetMapping("/currentFilter/{routineId}")
    public ResponseEntity<?> checkRoutine(@RequestParam Long routineId,
                                         @Valid @RequestBody FilterRequestDto dto){

        Routine routine = routineRepository.findById(routineId).orElseThrow(
                () -> new NotFoundException(
                        "Routine을 찾을 수 없습니다. routineId: " + routineId)
        );


        Place place = routine.getPlace();



        Time time = routine.getTime();

        boolean placeRange = placeService.inRange(dto, place.getCenter());


        boolean timeRange = timeService.inRange(time.getStartTime(), time.getEndTime(), dto.getCurrentTime());

        if(!placeRange) {

            throw new RuntimeException("가능한 위치가 아닙니다.");
        }

        if(!timeRange){

                throw new RuntimeException("가능한 시간이 아닙니다.");

        }



        RoutineState routineState = RoutineState.builder()
                .routine(routine)
                .status(State.DONE)
                .occurenceDate(dto.getCurrentDate())
                .build();

        routineStateRepository.save(routineState);



        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/current")
    public ResponseEntity<?> current(@Valid @RequestBody CurrentRequestDto dto,
                                       @AuthenticationPrincipal UserDetails userDetails){

        String username = userDetails.getUsername();
        log.info("GET /routines username={}",username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. username=" + username));


        List<Routine> dailyRoutines = routineRepository.findDaily(user.getId(), dto.getCurrentDate().getDayOfWeek(), dto.getCurrentDate());




        List<Routine> matched = dailyRoutines.stream()
                .filter(routine -> {
                    boolean timeMatch = timeService.inRange(
                            routine.getTime().getStartTime(),
                            routine.getTime().getEndTime(),
                            dto.getCurrentTime()
                    );

                    boolean placeMatch = placeService.inRange(dto, routine.getPlace().getCenter());

                    return timeMatch && placeMatch;
                })
                .toList();

        log.info("가능한 루틴의 갯수 : {}", matched.size());


        if (matched.isEmpty()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok(matched);
        }




    }
}
