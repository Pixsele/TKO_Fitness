package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import tko.database.entity.workout.WorkoutEntity;
import tko.model.dto.workout.WorkoutDTO;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutEntity toEntity(WorkoutDTO dto);
    WorkoutDTO toDTO(WorkoutEntity entity);
}
