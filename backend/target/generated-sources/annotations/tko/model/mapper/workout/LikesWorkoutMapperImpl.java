package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.LikesWorkoutEntity;
import tko.database.entity.workout.WorkoutEntity;
import tko.model.dto.workout.LikesWorkoutDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class LikesWorkoutMapperImpl implements LikesWorkoutMapper {

    @Override
    public LikesWorkoutEntity toEntity(LikesWorkoutDTO dto) {
        if ( dto == null ) {
            return null;
        }

        LikesWorkoutEntity likesWorkoutEntity = new LikesWorkoutEntity();

        likesWorkoutEntity.setUser( likesWorkoutDTOToUsersEntity( dto ) );
        likesWorkoutEntity.setWorkout( likesWorkoutDTOToWorkoutEntity( dto ) );
        likesWorkoutEntity.setId( dto.getId() );

        return likesWorkoutEntity;
    }

    @Override
    public LikesWorkoutDTO toDTO(LikesWorkoutEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LikesWorkoutDTO likesWorkoutDTO = new LikesWorkoutDTO();

        likesWorkoutDTO.setUserId( entityUserId( entity ) );
        likesWorkoutDTO.setWorkoutId( entityWorkoutId( entity ) );
        likesWorkoutDTO.setId( entity.getId() );

        return likesWorkoutDTO;
    }

    protected UsersEntity likesWorkoutDTOToUsersEntity(LikesWorkoutDTO likesWorkoutDTO) {
        if ( likesWorkoutDTO == null ) {
            return null;
        }

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId( likesWorkoutDTO.getUserId() );

        return usersEntity;
    }

    protected WorkoutEntity likesWorkoutDTOToWorkoutEntity(LikesWorkoutDTO likesWorkoutDTO) {
        if ( likesWorkoutDTO == null ) {
            return null;
        }

        WorkoutEntity workoutEntity = new WorkoutEntity();

        workoutEntity.setId( likesWorkoutDTO.getWorkoutId() );

        return workoutEntity;
    }

    private Long entityUserId(LikesWorkoutEntity likesWorkoutEntity) {
        if ( likesWorkoutEntity == null ) {
            return null;
        }
        UsersEntity user = likesWorkoutEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityWorkoutId(LikesWorkoutEntity likesWorkoutEntity) {
        if ( likesWorkoutEntity == null ) {
            return null;
        }
        WorkoutEntity workout = likesWorkoutEntity.getWorkout();
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
