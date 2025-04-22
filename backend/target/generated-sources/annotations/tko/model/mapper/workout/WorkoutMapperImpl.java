package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.workout.WorkoutEntity;
import tko.model.dto.workout.WorkoutDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class WorkoutMapperImpl implements WorkoutMapper {

    @Override
    public WorkoutEntity toEntity(WorkoutDTO dto) {
        if ( dto == null ) {
            return null;
        }

        WorkoutEntity workoutEntity = new WorkoutEntity();

        workoutEntity.setId( dto.getId() );
        workoutEntity.setDescription( dto.getDescription() );
        workoutEntity.setName( dto.getName() );
        workoutEntity.setDifficult( dto.getDifficult() );
        workoutEntity.setLikeCount( dto.getLikeCount() );

        return workoutEntity;
    }

    @Override
    public WorkoutDTO toDTO(WorkoutEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WorkoutDTO workoutDTO = new WorkoutDTO();

        workoutDTO.setId( entity.getId() );
        workoutDTO.setDescription( entity.getDescription() );
        workoutDTO.setName( entity.getName() );
        workoutDTO.setDifficult( entity.getDifficult() );
        workoutDTO.setLikeCount( entity.getLikeCount() );

        return workoutDTO;
    }

    @Override
    public void updateEntity(WorkoutDTO dto, WorkoutEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDescription( dto.getDescription() );
        entity.setName( dto.getName() );
        entity.setDifficult( dto.getDifficult() );
    }
}
