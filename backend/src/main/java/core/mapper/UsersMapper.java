package core.mapper;

import core.database.entity.TrainingsProgramEntity;
import core.database.entity.UsersEntity;
import core.dto.UsersDTO;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {
    public static UsersDTO toUsersDTO(UsersEntity usersEntity) {
        UsersDTO usersDTO = new UsersDTO();

        usersDTO.setId(usersEntity.getId());
        usersDTO.setName(usersEntity.getName());
        usersDTO.setLogin(usersEntity.getLogin());
        usersDTO.setPassword(usersEntity.getPassword());
        usersDTO.setAge(usersEntity.getAge());
        usersDTO.setWeight(usersEntity.getWeight());
        usersDTO.setHeight(usersEntity.getHeight());
        usersDTO.setTargetKcal(usersEntity.getTargetKcal());
        usersDTO.setCreatedAt(usersEntity.getCreatedAt());

        return usersDTO;
    }

    public static UsersEntity toUsersEntity(UsersDTO usersDTO) {
        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId(usersDTO.getId());
        usersEntity.setName(usersDTO.getName());
        usersEntity.setLogin(usersDTO.getLogin());
        usersEntity.setPassword(usersDTO.getPassword());
        usersEntity.setAge(usersDTO.getAge());
        usersEntity.setWeight(usersDTO.getWeight());
        usersEntity.setHeight(usersDTO.getHeight());
        usersEntity.setTargetKcal(usersDTO.getTargetKcal());
        usersEntity.setCreatedAt(usersDTO.getCreatedAt());

        return usersEntity;
    }
}
