package routine.log.dto.routine;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

import lombok.Data;

import java.time.DayOfWeek;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class RoutineCreateRequestDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "위치는 필수입니다.")
    private String location;

    @NotBlank
    private Integer latitude;

    @NotBlank
    private Integer longitude;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;


    @Size(min = 1, message = "요일을 1개 이상 선택해야 합니다.")
    private Set<DayOfWeek> dayofWeekSet;









}
