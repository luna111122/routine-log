package routine.log.service;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import routine.log.domain.Place;
import routine.log.domain.Routine;
import routine.log.domain.Time;
import routine.log.domain.User;
import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.exception.CreationException;
import routine.log.exception.NotFoundException;
import routine.log.repository.RoutineRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RoutineService {

    final private RoutineRepository routineRepository;

    @Transactional
    public Routine createRoutine(RoutineCreateRequestDto requestDto, User user, Place place, Time time, LocalDate date) {



        Routine routine = Routine.builder()
                .title(requestDto.getTitle())
                .location(requestDto.getLocation())
                .user(user)
                .place(place)
                .time(time)
                .startDate(date)
                .endDate(null)
                .build();

        routine.setWeekdays(requestDto.getDayofWeekSet());




        try {
            return routineRepository.save(routine);
        } catch (DataAccessException e) {
            String msg = String.format(
                    "Routine 생성 실패: DB 저장 중 예외 발생 (title=%s, location=%s, userId=%d, placeId=%d, timeId=%d, startDate=%s)",
                    requestDto.getTitle(),
                    requestDto.getLocation(),
                    user.getId(),
                    place.getId(),
                    time.getId(),
                    date
            );
            log.error(msg, e);
            throw new CreationException(msg, e);
        }



    }

    @Transactional
    public void deleteRoutine(Long routineId,LocalDate date){


        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new NotFoundException("루틴이 존재하지 않습니다. id=" + routineId));

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
    public List<Routine> getTodayRoutines(User user, @Nullable LocalDate date) {

        // 만약 date 가 null 이면 현재 날짜를 사용한다
        LocalDate target = (date != null)
                ? date
                : ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();


        DayOfWeek day = target.getDayOfWeek();

        // 특정 사용자의 타겟 날짜 / 요일에 유효한 루틴들을, 시작 시간순서로 가져옴
        return routineRepository.findDaily(user.getId(), day, target);
    }
}
