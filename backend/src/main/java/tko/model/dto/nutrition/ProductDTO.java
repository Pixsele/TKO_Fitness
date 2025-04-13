package tko.model.dto.nutrition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer kcal;
    @NotBlank
    private String unit;

    private LocalDateTime createdAt;

    private Integer likeCount;
    @NotNull
    private BigDecimal fats;
    @NotNull
    private BigDecimal carbs;
    @NotNull
    private BigDecimal proteins;
}
