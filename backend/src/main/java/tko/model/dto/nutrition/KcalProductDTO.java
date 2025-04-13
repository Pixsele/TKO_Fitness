package tko.model.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.utils.MealType;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KcalProductDTO {
    private Long id;
    private Long kcalTrackerId;
    private Long productId;
    private Integer count;
    private MealType typeMeal;
    private LocalDate date;
}
