package tko.model.mapper.nutrition;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.nutrition.KcalTrackerEntity;
import tko.database.entity.user.UsersEntity;
import tko.model.dto.nutrition.KcalTrackerDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class KcalTrackerMapperImpl implements KcalTrackerMapper {

    @Override
    public KcalTrackerDTO toDto(KcalTrackerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        KcalTrackerDTO kcalTrackerDTO = new KcalTrackerDTO();

        kcalTrackerDTO.setUserId( entityUserId( entity ) );
        kcalTrackerDTO.setId( entity.getId() );
        kcalTrackerDTO.setDate( entity.getDate() );

        return kcalTrackerDTO;
    }

    @Override
    public KcalTrackerEntity toEntity(KcalTrackerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        KcalTrackerEntity kcalTrackerEntity = new KcalTrackerEntity();

        kcalTrackerEntity.setUser( kcalTrackerDTOToUsersEntity( dto ) );
        kcalTrackerEntity.setId( dto.getId() );
        kcalTrackerEntity.setDate( dto.getDate() );

        return kcalTrackerEntity;
    }

    @Override
    public void updateEntity(KcalTrackerDTO dto, KcalTrackerEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setId( dto.getId() );
        entity.setDate( dto.getDate() );
    }

    private Long entityUserId(KcalTrackerEntity kcalTrackerEntity) {
        if ( kcalTrackerEntity == null ) {
            return null;
        }
        UsersEntity user = kcalTrackerEntity.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected UsersEntity kcalTrackerDTOToUsersEntity(KcalTrackerDTO kcalTrackerDTO) {
        if ( kcalTrackerDTO == null ) {
            return null;
        }

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId( kcalTrackerDTO.getUserId() );

        return usersEntity;
    }
}
