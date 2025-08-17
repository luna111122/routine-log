package routine.log.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import routine.log.domain.Time;
import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.exception.CreationException;
import routine.log.repository.TimeRepository;

import java.time.LocalTime;

@Service
@Slf4j
@AllArgsConstructor
public class TimeService {

    final private TimeRepository timeRepository;

    public Time createTime(RoutineCreateRequestDto dto) {

        Time time = Time.builder()
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();


        try {
            return timeRepository.save(time);
        } catch (DataAccessException e) {
            String msg = String.format(
                    "Time 생성 실패 (startTime=%s, endTime=%s)",
                    dto.getStartTime(), dto.getEndTime()
            );
            log.error(msg, e);
            throw new CreationException(msg, e);
        }



    }

    public boolean inRange(LocalTime startTime, LocalTime endTime, LocalTime currentTime) {
        boolean withinRange = true;

        if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {

            withinRange = false;
        }

        log.info("시간이 범위 안에 존재한다 ={} 시작시간:{}, 끝시간:{}, 현재시간:{}", withinRange, startTime, endTime, currentTime);

        return withinRange;

    }
}
