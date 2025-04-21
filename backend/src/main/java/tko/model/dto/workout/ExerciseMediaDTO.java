package tko.model.dto.workout;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMediaDTO {
    private Long id;
    private String urlImage;
    private String urlVideo;
    private LocalDateTime imageUpdated;
    private LocalDateTime videoUpdated;
}
