package tko.database.entity.workout;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tko.utils.DifficultyLevel;
import tko.utils.personSVG.Muscle;
import tko.utils.personSVG.MuscularConverter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instruction;
    private String name;
    private String type;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficult;

    private String specialEquipment;

    @Convert(converter = MuscularConverter.class)
    private List<Muscle> muscularGroup;
    private Integer kcal;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "like_count")
    private Integer likeCount;


}