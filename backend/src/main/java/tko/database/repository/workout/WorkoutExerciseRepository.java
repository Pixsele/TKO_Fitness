package tko.database.repository.workout;

import tko.database.entity.workout.WorkoutExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tko.model.dto.workout.WorkoutExerciseDTO;

import java.util.List;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, Long> {
    List<WorkoutExerciseDTO> findByWorkout_Id(Long workoutId);
}