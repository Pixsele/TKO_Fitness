package tko.service.workout;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tko.database.entity.workout.LikesExerciseEntity;
import tko.database.repository.workout.LikesExerciseRepository;
import tko.model.dto.workout.LikesExerciseDTO;
import tko.model.mapper.workout.LikesExerciseMapper;

import java.time.LocalDateTime;

@Service
public class LikesExerciseService {

    private final LikesExerciseRepository likesExerciseRepository;
    private final LikesExerciseMapper likesExerciseMapper;

    @Autowired
    public LikesExerciseService(LikesExerciseRepository likesExerciseRepository, LikesExerciseMapper likesExerciseMapper) {
        this.likesExerciseRepository = likesExerciseRepository;
        this.likesExerciseMapper = likesExerciseMapper;
    }

    public LikesExerciseDTO createLikesExercise(LikesExerciseDTO likesExerciseDTO) {
        LikesExerciseEntity likesExerciseEntity =  likesExerciseMapper.toEntity(likesExerciseDTO);
        likesExerciseEntity.setCreatedAt(LocalDateTime.now());
        likesExerciseRepository.save(likesExerciseEntity);
        return likesExerciseMapper.toDTO(likesExerciseEntity);
    }



}
