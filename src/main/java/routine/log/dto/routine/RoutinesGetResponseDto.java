package routine.log.dto.routine;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cglib.core.Local;
import routine.log.State;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class RoutinesGetResponseDto {


    private Long id;


    private String title;



    private String location;


    private LocalTime startTime;


    private LocalTime endTime;


    private State status;

}
