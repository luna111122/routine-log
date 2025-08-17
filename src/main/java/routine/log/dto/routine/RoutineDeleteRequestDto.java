package routine.log.dto.routine;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoutineDeleteRequestDto {

    @NotNull
    private Long routineId;
}
