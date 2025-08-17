package routine.log.dto.place;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

@AllArgsConstructor
public class PlaceCreationDto {


    @NotNull
    private Integer latitude;

    @NotNull
    private Integer longitude;

}
