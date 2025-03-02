package core.service;


import core.database.entity.NutritionProgramEntity;
import core.database.entity.TrainingsProgramEntity;
import core.database.entity.UsersEntity;
import core.database.repository.NutritionProgramRepository;
import core.database.repository.TrainingsProgramRepository;
import core.database.repository.UsersRepository;
import core.dto.RegisterUsersDTO;
import core.dto.UsersDTO;
import core.mapper.UsersMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final TrainingsProgramRepository trainingsProgramRepository;
    private final NutritionProgramRepository nutritionProgramRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, TrainingsProgramRepository trainingsProgramRepository, NutritionProgramRepository nutritionProgramRepository) {
        this.usersRepository = usersRepository;
        this.trainingsProgramRepository = trainingsProgramRepository;
        this.nutritionProgramRepository = nutritionProgramRepository;
    }

    public UsersDTO getUser(Long id) {
        UsersEntity usersEntity = usersRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));

        return UsersMapper.toUsersDTO(usersEntity);
    }

    public UsersDTO registerUser(RegisterUsersDTO registerUsersDTO) {
        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setName(registerUsersDTO.getName());
        usersEntity.setLogin(registerUsersDTO.getLogin());
        //TODO
        usersEntity.setPassword(registerUsersDTO.getPassword());
        usersEntity.setAge(registerUsersDTO.getAge());
        usersEntity.setWeight(registerUsersDTO.getWeight());
        usersEntity.setHeight(registerUsersDTO.getHeight());
        usersEntity.setTargetKcal(1900);
        usersEntity.setCurrentTrainingProgram(null);
        usersEntity.setCurrentNutritionProgram(null);
        usersEntity.setCreatedAt(LocalDateTime.now());

        UsersEntity savedUser = usersRepository.save(usersEntity);

        return UsersMapper.toUsersDTO(savedUser);
    }

    public UsersDTO updateUser(Long id, UsersDTO usersDTO) {
        UsersEntity usersEntity = usersRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));


        if(usersDTO.getName()!=null) {
            usersEntity.setName(usersDTO.getName());
        }
        //TODO
        if(usersDTO.getPassword()!=null) {
            usersEntity.setPassword(usersDTO.getPassword());
        }
        if (usersDTO.getLogin()!=null) {
            usersEntity.setLogin(usersDTO.getLogin());
        }
        if(usersDTO.getAge()!=null){
            usersEntity.setAge(usersDTO.getAge());
        }
        if(usersDTO.getWeight()!=null){
            usersEntity.setWeight(usersDTO.getWeight());
        }
        if(usersDTO.getHeight()!=null){
            usersEntity.setHeight(usersDTO.getHeight());
        }
        if(usersDTO.getTargetKcal()!=null){
            usersEntity.setTargetKcal(usersDTO.getTargetKcal());
        }
        if(usersDTO.getCreatedAt()!=null){
            usersEntity.setCreatedAt(usersDTO.getCreatedAt());
        }

        if(usersDTO.getCurrentTrainingProgramId() != null){
            TrainingsProgramEntity trainingsProgramEntity = trainingsProgramRepository.findById(usersDTO
                    .getCurrentTrainingProgramId()).orElseThrow(()-> new EntityNotFoundException("Training program not found"));

            usersEntity.setCurrentTrainingProgram(trainingsProgramEntity);
        }

        if(usersDTO.getCurrentNutritionProgramId() != null){
            NutritionProgramEntity nutritionProgram = nutritionProgramRepository.findById(usersDTO
                    .getCurrentNutritionProgramId()).orElseThrow(()-> new EntityNotFoundException("Nutrition program not found"));
        }

        UsersEntity savedUser = usersRepository.save(usersEntity);

        return UsersMapper.toUsersDTO(savedUser);
    }

    public UsersDTO deleteUser(Long id) {
        UsersEntity usersEntity = usersRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found"));
        usersRepository.delete(usersEntity);
        return UsersMapper.toUsersDTO(usersEntity);
    }
}
