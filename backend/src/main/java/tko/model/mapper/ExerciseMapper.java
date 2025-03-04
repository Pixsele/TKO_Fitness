package tko.model.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.workout.ExerciseEntity;
import tko.model.dto.workout.ExerciseDTO;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    ExerciseEntity toEntity(ExerciseDTO dto);

    ExerciseDTO toDto(ExerciseEntity entity);
}
