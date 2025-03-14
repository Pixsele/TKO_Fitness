package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.model.dto.workout.WorkoutDTO;
import tko.model.dto.workout.WorkoutExerciseDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutEntity toEntity(WorkoutDTO dto);
    WorkoutDTO toDTO(WorkoutEntity entity);
}
