package tko.model.mapper.workout;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.workout.PlannedWorkoutEntity;
import tko.model.dto.workout.PlannedWorkoutDTO;

@Mapper(componentModel = "spring")
public interface PlannedWorkoutMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "workoutId", target = "workout.id")
    PlannedWorkoutEntity toEntity(PlannedWorkoutDTO dto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "workout.id", target = "workoutId")
    PlannedWorkoutDTO toDto(PlannedWorkoutEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "workout", ignore = true)
    void updateEntity(PlannedWorkoutDTO dto, @MappingTarget PlannedWorkoutEntity entity);
}
