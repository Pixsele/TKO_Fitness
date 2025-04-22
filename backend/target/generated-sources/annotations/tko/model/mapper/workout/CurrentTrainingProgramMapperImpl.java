package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.workout.CurrentTrainingProgramEntity;
import tko.database.entity.workout.TrainingsProgramEntity;
import tko.model.dto.workout.CurrentTrainingProgramDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class CurrentTrainingProgramMapperImpl implements CurrentTrainingProgramMapper {

    @Override
    public CurrentTrainingProgramEntity toEntity(CurrentTrainingProgramDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CurrentTrainingProgramEntity currentTrainingProgramEntity = new CurrentTrainingProgramEntity();

        currentTrainingProgramEntity.setId( dto.getId() );

        return currentTrainingProgramEntity;
    }

    @Override
    public CurrentTrainingProgramDTO toDto(CurrentTrainingProgramEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CurrentTrainingProgramDTO currentTrainingProgramDTO = new CurrentTrainingProgramDTO();

        currentTrainingProgramDTO.setUserId( entityUserId( entity ) );
        currentTrainingProgramDTO.setTrainingProgramId( entityTrainingProgramId( entity ) );
        currentTrainingProgramDTO.setId( entity.getId() );

        return currentTrainingProgramDTO;
    }

    @Override
    public void updateEntity(CurrentTrainingProgramDTO dto, CurrentTrainingProgramEntity entity) {
        if ( dto == null ) {
            return;
        }
    }

    private Long entityUserId(CurrentTrainingProgramEntity currentTrainingProgramEntity) {
        if ( currentTrainingProgramEntity == null ) {
            return null;
        }
        UsersEntity user = currentTrainingProgramEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityTrainingProgramId(CurrentTrainingProgramEntity currentTrainingProgramEntity) {
        if ( currentTrainingProgramEntity == null ) {
            return null;
        }
        TrainingsProgramEntity trainingProgram = currentTrainingProgramEntity.getTrainingProgram();
        if ( trainingProgram == null ) {
            return null;
        }
        Long id = trainingProgram.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
