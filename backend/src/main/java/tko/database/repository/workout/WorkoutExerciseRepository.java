package tko.database.repository.workout;

import tko.database.entity.workout.WorkoutExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, Long> {

    boolean existsByExercise_Id(Long exerciseId);


    Integer countByWorkout_Id(Long workoutId);

    List<WorkoutExerciseEntity> findAllByWorkout_Id(Long workoutId);

    List<WorkoutExerciseEntity> findByWorkout_Id(Long workoutId);
}