package tko.model.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.user.UsersEntity;
import tko.model.dto.user.UsersDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class UsersMapperImpl implements UsersMapper {

    @Override
    public UsersEntity toEntity(UsersDTO usersDTO) {
        if ( usersDTO == null ) {
            return null;
        }

        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId( usersDTO.getId() );
        usersEntity.setName( usersDTO.getName() );
        usersEntity.setLogin( usersDTO.getLogin() );
        usersEntity.setBirthDay( usersDTO.getBirthDay() );
        usersEntity.setWeight( usersDTO.getWeight() );
        usersEntity.setHeight( usersDTO.getHeight() );
        usersEntity.setTargetKcal( usersDTO.getTargetKcal() );
        usersEntity.setGender( usersDTO.getGender() );
        usersEntity.setPhotoUrl( usersDTO.getPhotoUrl() );
        usersEntity.setCreatedAt( usersDTO.getCreatedAt() );
        usersEntity.setRole( usersDTO.getRole() );

        return usersEntity;
    }

    @Override
    public UsersDTO toDTO(UsersEntity usersEntity) {
        if ( usersEntity == null ) {
            return null;
        }

        UsersDTO usersDTO = new UsersDTO();

        usersDTO.setId( usersEntity.getId() );
        usersDTO.setName( usersEntity.getName() );
        usersDTO.setLogin( usersEntity.getLogin() );
        usersDTO.setBirthDay( usersEntity.getBirthDay() );
        usersDTO.setWeight( usersEntity.getWeight() );
        usersDTO.setHeight( usersEntity.getHeight() );
        usersDTO.setTargetKcal( usersEntity.getTargetKcal() );
        usersDTO.setGender( usersEntity.getGender() );
        usersDTO.setPhotoUrl( usersEntity.getPhotoUrl() );
        usersDTO.setRole( usersEntity.getRole() );
        usersDTO.setCreatedAt( usersEntity.getCreatedAt() );

        return usersDTO;
    }
}
