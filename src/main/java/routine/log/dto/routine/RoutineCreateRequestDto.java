package routine.log.dto.routine;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import routine.log.domain.Routine;

@Data
@AllArgsConstructor
public class RoutineCreateRequestDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "위치는 필수입니다.")
    private String location;



}
