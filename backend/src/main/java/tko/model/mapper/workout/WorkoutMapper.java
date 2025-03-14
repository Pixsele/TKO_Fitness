package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.workout.WorkoutEntity;
import tko.model.dto.workout.WorkoutCreateDTO;
import tko.model.dto.workout.WorkoutDTO;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {
    WorkoutEntity toEntity(WorkoutDTO dto);
    WorkoutDTO toDTO(WorkoutEntity entity);

    @Mapping(target = "id",ignore = true)
    WorkoutEntity toEntity(WorkoutCreateDTO dto);
}
