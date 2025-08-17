package routine.log.service;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import routine.log.State;
import routine.log.domain.Place;
import routine.log.domain.Routine;
import routine.log.domain.Time;
import routine.log.domain.User;
import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.exception.routine.RoutineNotFoundException;
import routine.log.repository.RoutineRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RoutineService {

    final private RoutineRepository routineRepository;

    @Transactional
    public Long createRoutine(RoutineCreateRequestDto requestDto, Optional<User> user, Place place, Time time, LocalDate date) {



        Routine routine = Routine.builder()
                .title(requestDto.getTitle())
                .location(requestDto.getLocation())
                .user(user.orElseThrow(() -> new IllegalArgumentException("User가 존재하지 않습니다")))
                .place(place)
                .time(time)
                .startDate(date)
                .endDate(null)
                .build();

        routine.setWeekdays(requestDto.getDayofWeekSet());

        Routine saved = routineRepository.save(routine);

        log.info("루틴 생성 id={}", saved.getId());

        return saved.getId();

    }

    @Transactional
    public void deleteRoutine(Long routineId,LocalDate date){


        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RoutineNotFoundException("루틴이 존재하지 않습니다. id=" + routineId));

        if (date.isBefore(routine.getStartDate())) {
            date = routine.getStartDate();
        }

        routine.setEndDate(date);




        log.info("루틴 소프트 종료 id={}, endDate={}", routineId, date);
    }


    public List<Routine> getAllRoutines(User user){

        Long id = user.getId();

        log.info("유저 ID ={} 루틴 목록 조회",id);

        List<Routine> routines = routineRepository.findAllByUserId(id);

        log.info("유저 ID={}의 루틴 {}건 조회됨", id, routines.size());

        return routines;
    }

    @Transactional
    public List<Routine> getTodayRoutines(User user, @Nullable LocalDate date){
        LocalDate target = (date != null)
                ? date
                : ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();
        DayOfWeek day = target.getDayOfWeek();
        return routineRepository.findDaily(user.getId(), day,target);
    }
}
