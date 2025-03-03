package core.mapper;

import core.database.entity.UsersEntity;
import core.dto.UsersDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper userMapper = Mappers.getMapper(UsersMapper.class);

    @Mapping(target = "id",ignore = true)
    UsersEntity toEntity(UsersDTO usersDTO);

    UsersDTO toDTO(UsersEntity usersEntity);
}
