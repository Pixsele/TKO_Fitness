package tko.model.mapper.workout;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tko.database.entity.workout.ExerciseEntity;
import tko.model.dto.workout.ExerciseDTO;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    ExerciseEntity toEntity(ExerciseDTO dto);
    ExerciseDTO toDto(ExerciseEntity entity);

    @Mapping(target = "id", ignore = true)
    void updateEntity(ExerciseDTO dto,@MappingTarget ExerciseEntity entity);
}
