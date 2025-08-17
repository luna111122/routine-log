package routine.log.dto.filter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@AllArgsConstructor
public class CurrentRequestDto {

    @NotNull
    private Integer latitude;

    @NotNull
    private Integer longitude;

    @NotNull
    private LocalTime currentTime;

    @NotNull
    private LocalDate currentDate;
}
