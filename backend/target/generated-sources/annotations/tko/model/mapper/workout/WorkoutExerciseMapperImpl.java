package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.model.dto.workout.WorkoutExerciseDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class WorkoutExerciseMapperImpl implements WorkoutExerciseMapper {

    @Override
    public WorkoutExerciseEntity toEntity(WorkoutExerciseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        WorkoutExerciseEntity workoutExerciseEntity = new WorkoutExerciseEntity();

        workoutExerciseEntity.setWorkout( workoutExerciseDTOToWorkoutEntity( dto ) );
        workoutExerciseEntity.setExercise( workoutExerciseDTOToExerciseEntity( dto ) );
        workoutExerciseEntity.setId( dto.getId() );
        workoutExerciseEntity.setExerciseOrder( dto.getExerciseOrder() );
        workoutExerciseEntity.setSets( dto.getSets() );
        workoutExerciseEntity.setReps( dto.getReps() );
        workoutExerciseEntity.setDistance( dto.getDistance() );
        workoutExerciseEntity.setDuration( dto.getDuration() );
        workoutExerciseEntity.setRestTime( dto.getRestTime() );

        return workoutExerciseEntity;
    }

    @Override
    public WorkoutExerciseDTO toDTO(WorkoutExerciseEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WorkoutExerciseDTO workoutExerciseDTO = new WorkoutExerciseDTO();

        workoutExerciseDTO.setWorkoutId( entityWorkoutId( entity ) );
        workoutExerciseDTO.setExerciseId( entityExerciseId( entity ) );
        workoutExerciseDTO.setId( entity.getId() );
        workoutExerciseDTO.setSets( entity.getSets() );
        workoutExerciseDTO.setReps( entity.getReps() );
        workoutExerciseDTO.setDistance( entity.getDistance() );
        workoutExerciseDTO.setDuration( entity.getDuration() );
        workoutExerciseDTO.setRestTime( entity.getRestTime() );
        workoutExerciseDTO.setExerciseOrder( entity.getExerciseOrder() );

        return workoutExerciseDTO;
    }

    @Override
    public void updateEntity(WorkoutExerciseDTO dto, WorkoutExerciseEntity entity) {
        if ( dto == null ) {
            return;
        }

        if ( entity.getWorkout() == null ) {
            entity.setWorkout( new WorkoutEntity() );
        }
        workoutExerciseDTOToWorkoutEntity1( dto, entity.getWorkout() );
        if ( entity.getExercise() == null ) {
            entity.setExercise( new ExerciseEntity() );
        }
        workoutExerciseDTOToExerciseEntity1( dto, entity.getExercise() );
        entity.setExerciseOrder( dto.getExerciseOrder() );
        entity.setSets( dto.getSets() );
        entity.setReps( dto.getReps() );
        entity.setDistance( dto.getDistance() );
        entity.setDuration( dto.getDuration() );
        entity.setRestTime( dto.getRestTime() );
    }

    protected WorkoutEntity workoutExerciseDTOToWorkoutEntity(WorkoutExerciseDTO workoutExerciseDTO) {
        if ( workoutExerciseDTO == null ) {
            return null;
        }

        WorkoutEntity workoutEntity = new WorkoutEntity();

        workoutEntity.setId( workoutExerciseDTO.getWorkoutId() );

        return workoutEntity;
    }

    protected ExerciseEntity workoutExerciseDTOToExerciseEntity(WorkoutExerciseDTO workoutExerciseDTO) {
        if ( workoutExerciseDTO == null ) {
            return null;
        }

        ExerciseEntity exerciseEntity = new ExerciseEntity();

        exerciseEntity.setId( workoutExerciseDTO.getExerciseId() );

        return exerciseEntity;
    }

    private Long entityWorkoutId(WorkoutExerciseEntity workoutExerciseEntity) {
        if ( workoutExerciseEntity == null ) {
            return null;
        }
        WorkoutEntity workout = workoutExerciseEntity.getWorkout();
        if ( workout == null ) {
            return null;
        }
        Long id = workout.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityExerciseId(WorkoutExerciseEntity workoutExerciseEntity) {
        if ( workoutExerciseEntity == null ) {
            return null;
        }
        ExerciseEntity exercise = workoutExerciseEntity.getExercise();
        if ( exercise == null ) {
            return null;
        }
        Long id = exercise.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected void workoutExerciseDTOToWorkoutEntity1(WorkoutExerciseDTO workoutExerciseDTO, WorkoutEntity mappingTarget) {
        if ( workoutExerciseDTO == null ) {
            return;
        }

        mappingTarget.setId( workoutExerciseDTO.getWorkoutId() );
    }

    protected void workoutExerciseDTOToExerciseEntity1(WorkoutExerciseDTO workoutExerciseDTO, ExerciseEntity mappingTarget) {
        if ( workoutExerciseDTO == null ) {
            return;
        }

        mappingTarget.setId( workoutExerciseDTO.getExerciseId() );
    }
}
