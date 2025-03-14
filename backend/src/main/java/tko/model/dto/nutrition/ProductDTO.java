package tko.model.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Integer kcal;
    private String unit;
    private String photoUrl;
    private String createdBy;
    private LocalDateTime createdAt;
}
