package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.model.dto.workout.WorkoutExerciseDTO;

@Mapper(componentModel = "spring")
public interface WorkoutExerciseMapper {
    WorkoutExerciseEntity toEntity(WorkoutExerciseDTO dto);

    @Mapping(source = "workout.id", target = "workoutId")
    @Mapping(source = "exercise.id", target = "exerciseId")
    WorkoutExerciseDTO toDTO(WorkoutExerciseEntity entity);
}
