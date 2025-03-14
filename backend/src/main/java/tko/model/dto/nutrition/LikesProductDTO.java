package tko.model.dto.nutrition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikesProductDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
    private Integer likeCount;
}