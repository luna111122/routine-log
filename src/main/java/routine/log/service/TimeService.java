package routine.log.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import routine.log.domain.Time;
import routine.log.dto.routine.RoutineCreateRequestDto;
import routine.log.repository.TimeRepository;

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

        return timeRepository.save(time);

    }
}
