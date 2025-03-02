package core.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsersDTO {
    private Long id;
    private String name;
    private String login;
    private String password;
    private Integer age;
    private Double weight;
    private Double height;
    private Integer targetKcal;

    private Long currentTrainingProgramId;
    private Long currentNutritionProgramId;
    private LocalDateTime createdAt;
}
