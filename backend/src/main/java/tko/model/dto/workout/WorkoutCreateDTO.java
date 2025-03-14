package tko.model.dto.workout;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutCreateDTO {
    @NotBlank
    private String description;
    @NotBlank
    private String name;
    @NotBlank
    private String difficult;
}