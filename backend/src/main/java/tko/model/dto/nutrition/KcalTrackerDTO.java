package tko.model.dto.nutrition;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KcalTrackerDTO {
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private LocalDate date;
}