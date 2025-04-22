package tko.model.mapper.workout;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.workout.TrainingsProgramEntity;
import tko.model.dto.workout.TrainingsProgramDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class TrainingsProgramMapperImpl implements TrainingsProgramMapper {

    @Override
    public TrainingsProgramEntity toEntity(TrainingsProgramDTO trainingsProgramDTO) {
        if ( trainingsProgramDTO == null ) {
            return null;
        }

        TrainingsProgramEntity trainingsProgramEntity = new TrainingsProgramEntity();

        trainingsProgramEntity.setId( trainingsProgramDTO.getId() );
        trainingsProgramEntity.setDescription( trainingsProgramDTO.getDescription() );
        trainingsProgramEntity.setName( trainingsProgramDTO.getName() );
        trainingsProgramEntity.setDifficult( trainingsProgramDTO.getDifficult() );
        trainingsProgramEntity.setLikeCount( trainingsProgramDTO.getLikeCount() );

        return trainingsProgramEntity;
    }

    @Override
    public TrainingsProgramDTO toDto(TrainingsProgramEntity trainingsProgramEntity) {
        if ( trainingsProgramEntity == null ) {
            return null;
        }

        TrainingsProgramDTO trainingsProgramDTO = new TrainingsProgramDTO();

        trainingsProgramDTO.setId( trainingsProgramEntity.getId() );
        trainingsProgramDTO.setDescription( trainingsProgramEntity.getDescription() );
        trainingsProgramDTO.setName( trainingsProgramEntity.getName() );
        trainingsProgramDTO.setDifficult( trainingsProgramEntity.getDifficult() );
        trainingsProgramDTO.setLikeCount( trainingsProgramEntity.getLikeCount() );

        return trainingsProgramDTO;
    }

    @Override
    public void updateEntity(TrainingsProgramDTO trainingsProgramDTO, TrainingsProgramEntity trainingsProgramEntity) {
        if ( trainingsProgramDTO == null ) {
            return;
        }

        trainingsProgramEntity.setDescription( trainingsProgramDTO.getDescription() );
        trainingsProgramEntity.setName( trainingsProgramDTO.getName() );
        trainingsProgramEntity.setDifficult( trainingsProgramDTO.getDifficult() );
    }
}
