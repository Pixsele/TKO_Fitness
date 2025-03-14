package tko.database.repository.workout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tko.database.entity.workout.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tko.database.entity.workout.WorkoutExerciseEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
    @Query("SELECT w FROM WorkoutEntity w LEFT JOIN FETCH w.workoutExercises WHERE w.id = :id")
    Optional<WorkoutEntity> findByIdWithExercises(@Param("id") Long id);

    @Query("SELECT w.workoutExercises FROM WorkoutEntity w WHERE w.id = :workoutId")
    List<WorkoutExerciseEntity> findExercisesByWorkoutId(@Param("workoutId") Long workoutId);
}