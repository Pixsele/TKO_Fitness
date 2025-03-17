package tko.service.workout;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.LikesExerciseEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.LikesExerciseRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.LikesExerciseDTO;
import tko.model.mapper.workout.ExerciseMapper;
import tko.model.mapper.workout.LikesExerciseMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikesExerciseService {

    private final LikesExerciseRepository likesExerciseRepository;
    private final LikesExerciseMapper likesExerciseMapper;
    private final ExerciseRepository exerciseRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public LikesExerciseService(LikesExerciseRepository likesExerciseRepository, LikesExerciseMapper likesExerciseMapper, ExerciseRepository exerciseRepository, UsersRepository usersRepository) {
        this.likesExerciseRepository = likesExerciseRepository;
        this.likesExerciseMapper = likesExerciseMapper;
        this.exerciseRepository = exerciseRepository;
        this.usersRepository = usersRepository;
    }

    public LikesExerciseDTO createLikesExercise(LikesExerciseDTO likesExerciseDTO) {
        if(likesExerciseDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(!(exerciseRepository.existsById(likesExerciseDTO.getExerciseId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise does not exist");
        }
        if(!(usersRepository.existsById(likesExerciseDTO.getUserId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        if(likesExerciseRepository.existsByUser_IdAndExercise_Id(likesExerciseDTO.getUserId(), likesExerciseDTO.getExerciseId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like in this exercise by this user is already exists");
        }

        LikesExerciseEntity likesExerciseEntity =  likesExerciseMapper.toEntity(likesExerciseDTO);
        likesExerciseEntity.setCreatedAt(LocalDateTime.now());
        likesExerciseRepository.save(likesExerciseEntity);
        return likesExerciseMapper.toDTO(likesExerciseEntity);
    }

    public LikesExerciseDTO readLikesExercise(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        LikesExerciseEntity likesExerciseEntity = likesExerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return likesExerciseMapper.toDTO(likesExerciseEntity);
    }

    @Transactional
    public LikesExerciseDTO updateLikesExercise(Long id, LikesExerciseDTO likesExerciseDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if(!(usersRepository.existsById(likesExerciseDTO.getUserId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
        if(!(exerciseRepository.existsById(likesExerciseDTO.getExerciseId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise does not exist");
        }

        if(likesExerciseRepository.existsByUser_IdAndExercise_Id(likesExerciseDTO.getUserId(), likesExerciseDTO.getExerciseId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like in this exercise by this user is already exists");
        }

        LikesExerciseEntity likesExerciseEntity = likesExerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        likesExerciseMapper.updateEntity(likesExerciseDTO, likesExerciseEntity);
        return likesExerciseMapper.toDTO(likesExerciseEntity);
    }

    public LikesExerciseDTO deleteLikesExercise(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        LikesExerciseEntity likesExerciseEntity = likesExerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        likesExerciseRepository.delete(likesExerciseEntity);
        return likesExerciseMapper.toDTO(likesExerciseEntity);
    }

    public Page<LikesExerciseDTO> readLikesExerciseByUserId(Long id,Pageable pageable) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        Page<LikesExerciseEntity> likesExerciseEntityPage = likesExerciseRepository.findByUser_Id(id,pageable);
        return likesExerciseEntityPage.map(likesExerciseMapper::toDTO);
    }

}
