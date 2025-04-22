package tko.model.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.user.WeightTrackerEntity;
import tko.model.dto.user.WeightTrackerDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class WeightTrackerMapperImpl implements WeightTrackerMapper {

    @Override
    public WeightTrackerDTO toDto(WeightTrackerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        WeightTrackerDTO weightTrackerDTO = new WeightTrackerDTO();

        weightTrackerDTO.setUserId( entityUserId( entity ) );
        weightTrackerDTO.setId( entity.getId() );
        weightTrackerDTO.setWeight( entity.getWeight() );
        weightTrackerDTO.setDate( entity.getDate() );

        return weightTrackerDTO;
    }

    @Override
    public WeightTrackerEntity toEntity(WeightTrackerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        WeightTrackerEntity weightTrackerEntity = new WeightTrackerEntity();

        weightTrackerEntity.setUser( weightTrackerDTOToUsersEntity( dto ) );
        weightTrackerEntity.setId( dto.getId() );
        weightTrackerEntity.setWeight( dto.getWeight() );
        weightTrackerEntity.setDate( dto.getDate() );

        return weightTrackerEntity;
    }

    private Long entityUserId(WeightTrackerEntity weightTrackerEntity) {
        if ( weightTrackerEntity == null ) {
            return null;
        }
        UsersEntity user = weightTrackerEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected UsersEntity weightTrackerDTOToUsersEntity(WeightTrackerDTO weightTrackerDTO) {
        if ( weightTrackerDTO == null ) {
            return null;
        }

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId( weightTrackerDTO.getUserId() );

        return usersEntity;
    }
}
