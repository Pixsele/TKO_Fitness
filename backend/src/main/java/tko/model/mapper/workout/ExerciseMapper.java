package tko.model.mapper.workout;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tko.database.entity.workout.ExerciseEntity;
import tko.model.dto.workout.ExerciseCreateDTO;
import tko.model.dto.workout.ExerciseDTO;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    ExerciseEntity toEntity(ExerciseDTO dto);
    ExerciseDTO toDto(ExerciseEntity entity);

    @Mapping(target = "id", ignore = true)
    ExerciseEntity toEntity(ExerciseCreateDTO createDTO);

}
