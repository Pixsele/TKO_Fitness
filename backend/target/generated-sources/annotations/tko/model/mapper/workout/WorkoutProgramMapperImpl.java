package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.workout.TrainingsProgramEntity;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.entity.workout.WorkoutProgramEntity;
import tko.model.dto.workout.WorkoutProgramDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class WorkoutProgramMapperImpl implements WorkoutProgramMapper {

    @Override
    public WorkoutProgramEntity toEntity(WorkoutProgramDTO dto) {
        if ( dto == null ) {
            return null;
        }

        WorkoutProgramEntity workoutProgramEntity = new WorkoutProgramEntity();

        workoutProgramEntity.setWorkout( workoutProgramDTOToWorkoutEntity( dto ) );
        workoutProgramEntity.setProgram( workoutProgramDTOToTrainingsProgramEntity( dto ) );
        workoutProgramEntity.setId( dto.getId() );
        workoutProgramEntity.setWorkoutOrder( dto.getWorkoutOrder() );

        return workoutProgramEntity;
    }

    @Override
    public WorkoutProgramDTO toDTO(WorkoutProgramEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WorkoutProgramDTO workoutProgramDTO = new WorkoutProgramDTO();

        workoutProgramDTO.setWorkoutId( entityWorkoutId( entity ) );
        workoutProgramDTO.setProgramId( entityProgramId( entity ) );
        workoutProgramDTO.setId( entity.getId() );
        workoutProgramDTO.setWorkoutOrder( entity.getWorkoutOrder() );

        return workoutProgramDTO;
    }

    @Override
    public void updateEntity(WorkoutProgramDTO dto, WorkoutProgramEntity entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getWorkout() == null ) {
            entity.setWorkout( new WorkoutEntity() );
        }
        workoutProgramDTOToWorkoutEntity1( dto, entity.getWorkout() );
        if ( entity.getProgram() == null ) {
            entity.setProgram( new TrainingsProgramEntity() );
        }
        workoutProgramDTOToTrainingsProgramEntity1( dto, entity.getProgram() );
        entity.setWorkoutOrder( dto.getWorkoutOrder() );
    }

    protected WorkoutEntity workoutProgramDTOToWorkoutEntity(WorkoutProgramDTO workoutProgramDTO) {
        if ( workoutProgramDTO == null ) {
            return null;
        }

        WorkoutEntity workoutEntity = new WorkoutEntity();

        workoutEntity.setId( workoutProgramDTO.getWorkoutId() );

        return workoutEntity;
    }

    protected TrainingsProgramEntity workoutProgramDTOToTrainingsProgramEntity(WorkoutProgramDTO workoutProgramDTO) {
        if ( workoutProgramDTO == null ) {
            return null;
        }

        TrainingsProgramEntity trainingsProgramEntity = new TrainingsProgramEntity();

        trainingsProgramEntity.setId( workoutProgramDTO.getProgramId() );

        return trainingsProgramEntity;
    }

    private Long entityWorkoutId(WorkoutProgramEntity workoutProgramEntity) {
        if ( workoutProgramEntity == null ) {
            return null;
        }
        WorkoutEntity workout = workoutProgramEntity.getWorkout();
        if ( workout == null ) {
            return null;
        }
        Long id = workout.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityProgramId(WorkoutProgramEntity workoutProgramEntity) {
        if ( workoutProgramEntity == null ) {
            return null;
        }
        TrainingsProgramEntity program = workoutProgramEntity.getProgram();
        if ( program == null ) {
            return null;
        }
        Long id = program.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected void workoutProgramDTOToWorkoutEntity1(WorkoutProgramDTO workoutProgramDTO, WorkoutEntity mappingTarget) {
        if ( workoutProgramDTO == null ) {
            return;
        }

        mappingTarget.setId( workoutProgramDTO.getWorkoutId() );
    }

    protected void workoutProgramDTOToTrainingsProgramEntity1(WorkoutProgramDTO workoutProgramDTO, TrainingsProgramEntity mappingTarget) {
        if ( workoutProgramDTO == null ) {
            return;
        }

        mappingTarget.setId( workoutProgramDTO.getProgramId() );
    }
}
