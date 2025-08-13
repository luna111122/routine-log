package routine.log.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import routine.log.domain.Routine;
import routine.log.domain.User;
import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.dto.routine.RoutineDeleteRequestDto;
import routine.log.dto.routine.RoutinesGetResponseDto;
import routine.log.exception.user.UserNotFoundException;
import routine.log.repository.UserRepository;
import routine.log.service.RoutineService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RoutineController {

    final private UserRepository userRepository;
    final private RoutineService routineService;


    @PostMapping("/routines")
    ResponseEntity<Long> createRoutine(@RequestBody @Valid RoutineCreateRequestDto requestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {


        String username = userDetails.getUsername();
        log.info("POST /routines by user={}, title={}, location={}", username, requestDto.getTitle(), requestDto.getLocation());
        Optional<User> user = userRepository.findByUsername(username);


        Long id = routineService.createRoutine(requestDto, user);
        log.info("POST /routines success id={}", id);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);

    }

    @DeleteMapping("/routines")
    ResponseEntity<?> deleteRoutine(@RequestBody @Valid RoutineDeleteRequestDto requestDto) {

        log.info("DELETE /routines id={}", requestDto.getRoutineId());
        routineService.deleteRoutine(requestDto.getRoutineId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/routines")
    ResponseEntity<?> getAllRoutines(@AuthenticationPrincipal UserDetails userDetails){

        String username = userDetails.getUsername();
        log.info("GET /routines username={}",username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다. username=" + username));

        List<Routine> allRoutines = routineService.getAllRoutines(user);

        List<RoutinesGetResponseDto> routines = allRoutines.stream()
                .map(r -> new RoutinesGetResponseDto(r.getTitle(), r.getLocation()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(routines);
    }
}
