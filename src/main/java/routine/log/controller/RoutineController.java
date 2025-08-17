package routine.log.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;
import routine.log.State;
import routine.log.domain.*;

import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.dto.routine.RoutinesGetResponseDto;
import routine.log.exception.NotFoundException;
import routine.log.repository.RoutineStateRepository;
import routine.log.repository.UserRepository;
import routine.log.service.PlaceService;
import routine.log.service.RoutineService;
import routine.log.service.TimeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RoutineController {

    final private UserRepository userRepository;
    final private RoutineService routineService;
    final private PlaceService placeService;
    final private TimeService timeService;
    final private RoutineStateRepository routineStateRepository;


    @PostMapping("/routines")
    ResponseEntity<?> createRoutine(@RequestBody @Valid RoutineCreateRequestDto requestDto,
                                    @RequestParam(value = "startDate")
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

                                    @AuthenticationPrincipal UserDetails userDetails) {


        String username = userDetails.getUsername();
        log.info("POST /routines by user={}, title={}, location={}, latitude={}, longitude={}",
                username, requestDto.getTitle(), requestDto.getLocation(), requestDto.getLatitude(),requestDto.getLongitude());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(
                        "사용자를 찾을 수 없습니다. username=" + username));



        Place place = placeService.createPlace(requestDto);
        log.info("POST /routines success place id={}", place.getId());



        Time time = timeService.createTime(requestDto);
        log.info("POST /routines success Time id={}", time.getId());


        Routine routine = routineService.createRoutine(requestDto, user,place,time, startDate);
        log.info("POST /routines success id={}", routine.getId());


        Map<String, Long> response = Map.of("routineId", routine.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("/routines/{routineId}")
    ResponseEntity<?> deleteRoutine(@PathVariable Long routineId,
                                    @RequestParam("effectiveDate")
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate) {

        log.info("DELETE /routines id={}", routineId);
        routineService.deleteRoutine(routineId,effectiveDate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/routines")
    ResponseEntity<?> getAllRoutines(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam(value = "date")
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

        String username = userDetails.getUsername();
        log.info("GET /routines username={}",username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. username=" + username));


        List<Routine> today = routineService.getTodayRoutines(user,date);

        log.info("GET /routines 조회된 루틴의 갯수={}",today.size());


        List<Long> ids = today.stream().map(Routine::getId).toList();
        Map<Long, State> statusById = routineStateRepository
                .findByRoutineIdsAndDate(ids, date).stream()
                .collect(Collectors.toMap(
                        s -> s.getRoutine().getId(),
                        RoutineState::getStatus
                ));


        List<RoutinesGetResponseDto> routines = today.stream()
                .map(r -> new RoutinesGetResponseDto(
                        r.getId(),
                        r.getTitle(),
                        r.getLocation(),
                        r.getTime() != null ? r.getTime().getStartTime() : null,
                        r.getTime() != null ? r.getTime().getEndTime()   : null,
                        statusById.getOrDefault(r.getId(), State.MISSED)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(routines);
    }
}
