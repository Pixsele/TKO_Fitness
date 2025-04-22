package tko.model.mapper.workout;

import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.workout.ExerciseEntity;
import tko.model.dto.workout.ExerciseDTO;
import tko.utils.DifficultyLevel;
import tko.utils.ExerciseType;
import tko.utils.personSVG.Muscle;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ExerciseMapperImpl implements ExerciseMapper {

    @Override
    public ExerciseEntity toEntity(ExerciseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ExerciseEntity exerciseEntity = new ExerciseEntity();

        exerciseEntity.setId( dto.getId() );
        exerciseEntity.setInstruction( dto.getInstruction() );
        exerciseEntity.setName( dto.getName() );
        if ( dto.getType() != null ) {
            exerciseEntity.setType( Enum.valueOf( ExerciseType.class, dto.getType() ) );
        }
        if ( dto.getDifficult() != null ) {
            exerciseEntity.setDifficult( Enum.valueOf( DifficultyLevel.class, dto.getDifficult() ) );
        }
        exerciseEntity.setRequiresEquipment( dto.getRequiresEquipment() );
        exerciseEntity.setMuscularGroup( mapStringToMuscles( dto.getMuscularGroup() ) );
        exerciseEntity.setLikeCount( dto.getLikeCount() );

        return exerciseEntity;
    }

    @Override
    public ExerciseDTO toDto(ExerciseEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ExerciseDTO exerciseDTO = new ExerciseDTO();

        exerciseDTO.setId( entity.getId() );
        exerciseDTO.setInstruction( entity.getInstruction() );
        exerciseDTO.setName( entity.getName() );
        if ( entity.getDifficult() != null ) {
            exerciseDTO.setDifficult( entity.getDifficult().name() );
        }
        exerciseDTO.setMuscularGroup( mapMusclesToString( entity.getMuscularGroup() ) );
        exerciseDTO.setLikeCount( entity.getLikeCount() );
        exerciseDTO.setRequiresEquipment( entity.getRequiresEquipment() );
        if ( entity.getType() != null ) {
            exerciseDTO.setType( entity.getType().name() );
        }

        return exerciseDTO;
    }

    @Override
    public void updateEntity(ExerciseDTO dto, ExerciseEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setInstruction( dto.getInstruction() );
        entity.setName( dto.getName() );
        if ( dto.getType() != null ) {
            entity.setType( Enum.valueOf( ExerciseType.class, dto.getType() ) );
        }
        else {
            entity.setType( null );
        }
        if ( dto.getDifficult() != null ) {
            entity.setDifficult( Enum.valueOf( DifficultyLevel.class, dto.getDifficult() ) );
        }
        else {
            entity.setDifficult( null );
        }
        entity.setRequiresEquipment( dto.getRequiresEquipment() );
        if ( entity.getMuscularGroup() != null ) {
            List<Muscle> list = mapStringToMuscles( dto.getMuscularGroup() );
            if ( list != null ) {
                entity.getMuscularGroup().clear();
                entity.getMuscularGroup().addAll( list );
            }
            else {
                entity.setMuscularGroup( null );
            }
        }
        else {
            List<Muscle> list = mapStringToMuscles( dto.getMuscularGroup() );
            if ( list != null ) {
                entity.setMuscularGroup( list );
            }
        }
    }
}
