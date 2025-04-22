package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.PlannedWorkoutEntity;
import tko.database.entity.workout.WorkoutEntity;
import tko.model.dto.workout.PlannedWorkoutDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class PlannedWorkoutMapperImpl implements PlannedWorkoutMapper {

    @Override
    public PlannedWorkoutEntity toEntity(PlannedWorkoutDTO dto) {
        if ( dto == null ) {
            return null;
        }

        PlannedWorkoutEntity plannedWorkoutEntity = new PlannedWorkoutEntity();

        plannedWorkoutEntity.setId( dto.getId() );
        plannedWorkoutEntity.setDate( dto.getDate() );
        if ( dto.getStatus() != null ) {
            plannedWorkoutEntity.setStatus( Enum.valueOf( PlannedWorkoutEntity.WorkoutStatus.class, dto.getStatus() ) );
        }

        return plannedWorkoutEntity;
    }

    @Override
    public PlannedWorkoutDTO toDto(PlannedWorkoutEntity entity) {
        if ( entity == null ) {
            return null;
        }

        PlannedWorkoutDTO plannedWorkoutDTO = new PlannedWorkoutDTO();

        plannedWorkoutDTO.setUserId( entityUserId( entity ) );
        plannedWorkoutDTO.setWorkoutId( entityWorkoutId( entity ) );
        plannedWorkoutDTO.setId( entity.getId() );
        plannedWorkoutDTO.setDate( entity.getDate() );
        if ( entity.getStatus() != null ) {
            plannedWorkoutDTO.setStatus( entity.getStatus().name() );
        }

        return plannedWorkoutDTO;
    }

    @Override
    public void updateEntity(PlannedWorkoutDTO dto, PlannedWorkoutEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setDate( dto.getDate() );
        if ( dto.getStatus() != null ) {
            entity.setStatus( Enum.valueOf( PlannedWorkoutEntity.WorkoutStatus.class, dto.getStatus() ) );
        }
        else {
            entity.setStatus( null );
        }
    }

    private Long entityUserId(PlannedWorkoutEntity plannedWorkoutEntity) {
        if ( plannedWorkoutEntity == null ) {
            return null;
        }
        UsersEntity user = plannedWorkoutEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityWorkoutId(PlannedWorkoutEntity plannedWorkoutEntity) {
        if ( plannedWorkoutEntity == null ) {
            return null;
        }
        WorkoutEntity workout = plannedWorkoutEntity.getWorkout();
        if ( workout == null ) {
            return null;
        }
        Long id = workout.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
