package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.LikesTrainingsProgramEntity;
import tko.database.entity.workout.TrainingsProgramEntity;
import tko.model.dto.workout.LikesTrainingsProgramDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class LikesTrainingsProgramMapperImpl implements LikesTrainingsProgramMapper {

    @Override
    public LikesTrainingsProgramEntity toEntity(LikesTrainingsProgramDTO dto) {
        if ( dto == null ) {
            return null;
        }

        LikesTrainingsProgramEntity likesTrainingsProgramEntity = new LikesTrainingsProgramEntity();

        likesTrainingsProgramEntity.setUser( likesTrainingsProgramDTOToUsersEntity( dto ) );
        likesTrainingsProgramEntity.setTrainingsProgram( likesTrainingsProgramDTOToTrainingsProgramEntity( dto ) );
        likesTrainingsProgramEntity.setId( dto.getId() );

        return likesTrainingsProgramEntity;
    }

    @Override
    public LikesTrainingsProgramDTO toDTO(LikesTrainingsProgramEntity entity) {
        if ( entity == null ) {
            return null;
        }

        LikesTrainingsProgramDTO likesTrainingsProgramDTO = new LikesTrainingsProgramDTO();

        likesTrainingsProgramDTO.setUserId( entityUserId( entity ) );
        likesTrainingsProgramDTO.setTrainingsProgramId( entityTrainingsProgramId( entity ) );
        likesTrainingsProgramDTO.setId( entity.getId() );

        return likesTrainingsProgramDTO;
    }

    protected UsersEntity likesTrainingsProgramDTOToUsersEntity(LikesTrainingsProgramDTO likesTrainingsProgramDTO) {
        if ( likesTrainingsProgramDTO == null ) {
            return null;
        }

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId( likesTrainingsProgramDTO.getUserId() );

        return usersEntity;
    }

    protected TrainingsProgramEntity likesTrainingsProgramDTOToTrainingsProgramEntity(LikesTrainingsProgramDTO likesTrainingsProgramDTO) {
        if ( likesTrainingsProgramDTO == null ) {
            return null;
        }

        TrainingsProgramEntity trainingsProgramEntity = new TrainingsProgramEntity();

        trainingsProgramEntity.setId( likesTrainingsProgramDTO.getTrainingsProgramId() );

        return trainingsProgramEntity;
    }

    private Long entityUserId(LikesTrainingsProgramEntity likesTrainingsProgramEntity) {
        if ( likesTrainingsProgramEntity == null ) {
            return null;
        }
        UsersEntity user = likesTrainingsProgramEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityTrainingsProgramId(LikesTrainingsProgramEntity likesTrainingsProgramEntity) {
        if ( likesTrainingsProgramEntity == null ) {
            return null;
        }
        TrainingsProgramEntity trainingsProgram = likesTrainingsProgramEntity.getTrainingsProgram();
        if ( trainingsProgram == null ) {
            return null;
        }
        Long id = trainingsProgram.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
