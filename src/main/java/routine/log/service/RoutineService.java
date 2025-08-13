package routine.log.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import routine.log.domain.Routine;
import routine.log.domain.User;
import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.dto.routine.RoutineDeleteRequestDto;
import routine.log.exception.routine.RoutineNotFoundException;
import routine.log.repository.RoutineRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RoutineService {

    final private RoutineRepository routineRepository;

    public Long createRoutine(RoutineCreateRequestDto requestDto, Optional<User> user) {



        Routine routine = Routine.builder()
                .title(requestDto.getTitle())
                .location(requestDto.getLocation())
                .user(user.orElseThrow(() -> new IllegalArgumentException("User가 존재하지 않습니다")))
                .build();

        Routine saved = routineRepository.save(routine);

        log.info("루틴 생성 id={}", saved.getId());

        return saved.getId();

    }

    public void deleteRoutine(Long routineId){


        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RoutineNotFoundException("루틴이 존재하지 않습니다. id=" + routineId));

        routineRepository.delete(routine);
        log.info("루틴 삭제 id={}", routineId);
    }


    public List<Routine> getAllRoutines(User user){

        Long id = user.getId();

        log.info("유저 ID ={} 루틴 목록 조회",id);

        List<Routine> routines = routineRepository.findAllByUserId(id);

        log.info("유저 ID={}의 루틴 {}건 조회됨", id, routines.size());

        return routines;
    }
}
