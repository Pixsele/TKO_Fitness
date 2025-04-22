package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.entity.workout.LikesExerciseEntity;
import tko.model.dto.workout.LikesExerciseDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class LikesExerciseMapperImpl implements LikesExerciseMapper {

    @Override
    public LikesExerciseEntity toEntity(LikesExerciseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        LikesExerciseEntity likesExerciseEntity = new LikesExerciseEntity();

        likesExerciseEntity.setUser( likesExerciseDTOToUsersEntity( dto ) );
        likesExerciseEntity.setExercise( likesExerciseDTOToExerciseEntity( dto ) );
        likesExerciseEntity.setId( dto.getId() );

        return likesExerciseEntity;
    }

    @Override
    public LikesExerciseDTO toDTO(LikesExerciseEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LikesExerciseDTO likesExerciseDTO = new LikesExerciseDTO();

        likesExerciseDTO.setUserId( entityUserId( entity ) );
        likesExerciseDTO.setExerciseId( entityExerciseId( entity ) );
        likesExerciseDTO.setId( entity.getId() );

        return likesExerciseDTO;
    }

    protected UsersEntity likesExerciseDTOToUsersEntity(LikesExerciseDTO likesExerciseDTO) {
        if ( likesExerciseDTO == null ) {
            return null;
        }

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId( likesExerciseDTO.getUserId() );

        return usersEntity;
    }

    protected ExerciseEntity likesExerciseDTOToExerciseEntity(LikesExerciseDTO likesExerciseDTO) {
        if ( likesExerciseDTO == null ) {
            return null;
        }

        ExerciseEntity exerciseEntity = new ExerciseEntity();

        exerciseEntity.setId( likesExerciseDTO.getExerciseId() );

        return exerciseEntity;
    }

    private Long entityUserId(LikesExerciseEntity likesExerciseEntity) {
        if ( likesExerciseEntity == null ) {
            return null;
        }
        UsersEntity user = likesExerciseEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityExerciseId(LikesExerciseEntity likesExerciseEntity) {
        if ( likesExerciseEntity == null ) {
            return null;
        }
        ExerciseEntity exercise = likesExerciseEntity.getExercise();
        if ( exercise == null ) {
            return null;
        }
        Long id = exercise.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
