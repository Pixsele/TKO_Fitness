package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.workout.CurrentTrainingProgramEntity;
import tko.database.entity.workout.PlannedWorkoutEntity;
import tko.database.entity.workout.ProgramPlannedEntity;
import tko.model.dto.workout.ProgramPlannedDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ProgramPlannedMapperImpl implements ProgramPlannedMapper {

    @Override
    public ProgramPlannedEntity toEntity(ProgramPlannedDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProgramPlannedEntity programPlannedEntity = new ProgramPlannedEntity();

        programPlannedEntity.setId( dto.getId() );

        return programPlannedEntity;
    }

    @Override
    public ProgramPlannedDTO toDto(ProgramPlannedEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProgramPlannedDTO programPlannedDTO = new ProgramPlannedDTO();

        programPlannedDTO.setCurrentTrainingProgramId( entityCurrentTrainingProgramId( entity ) );
        programPlannedDTO.setPlannedWorkoutId( entityPlannedWorkoutId( entity ) );
        programPlannedDTO.setId( entity.getId() );

        return programPlannedDTO;
    }

    @Override
    public void updateEntity(ProgramPlannedDTO dto, ProgramPlannedEntity entity) {
        if ( dto == null ) {
            return;
        }
    }

    private Long entityCurrentTrainingProgramId(ProgramPlannedEntity programPlannedEntity) {
        if ( programPlannedEntity == null ) {
            return null;
        }
        CurrentTrainingProgramEntity currentTrainingProgram = programPlannedEntity.getCurrentTrainingProgram();
        if ( currentTrainingProgram == null ) {
            return null;
        }
        Long id = currentTrainingProgram.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityPlannedWorkoutId(ProgramPlannedEntity programPlannedEntity) {
        if ( programPlannedEntity == null ) {
            return null;
        }
        PlannedWorkoutEntity plannedWorkout = programPlannedEntity.getPlannedWorkout();
        if ( plannedWorkout == null ) {
            return null;
        }
        Long id = plannedWorkout.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
