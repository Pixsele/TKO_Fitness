package core.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String login;
    private String password;
    private int age;
    private double weight;
    private double height;
    private int targetKcal;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "current_training_program_id")
    private TrainingsProgramEntity currentTrainingProgram;

    @ManyToOne
    @JoinColumn(name = "current_nutrition_program_id")
    private NutritionProgramEntity currentNutritionProgram;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<KcalTrackerEntity> kcalTrackers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PlannedWorkoutEntity> plannedWorkouts;

    public enum Gender {
        MALE, FEMALE
    }
}