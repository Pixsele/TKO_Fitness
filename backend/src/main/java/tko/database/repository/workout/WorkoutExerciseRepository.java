package tko.database.repository.workout;

import tko.database.entity.workout.WorkoutExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tko.model.dto.workout.WorkoutExerciseDTO;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, Long> {
    List<WorkoutExerciseEntity> findByWorkout_Id(Long workoutId);

    boolean existsByExercise_Id(Long exerciseId);

    List<WorkoutExerciseEntity> findAllByWorkout_Id(Long workoutId);
}