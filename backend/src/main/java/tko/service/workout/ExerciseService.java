package tko.service.workout;

import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.repository.workout.ExerciseRepository;
import tko.model.dto.workout.ExerciseDTO;
import tko.model.mapper.ExerciseMapper;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    public ExerciseDTO create(ExerciseDTO exerciseDTO) {
        ExerciseEntity exerciseEntity = exerciseMapper.toEntity(exerciseDTO);
        ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
        return exerciseMapper.toDto(saveEntity);
    }

    public ExerciseDTO read(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
        return exerciseMapper.toDto(exerciseEntity);
    }

    public ExerciseDTO update(ExerciseDTO exerciseDTO) {
        ExerciseEntity exerciseEntity = exerciseMapper.toEntity(exerciseDTO);
        if(exerciseRepository.existsById(exerciseDTO.getId())) {
            ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
            return exerciseMapper.toDto(saveEntity);
        }
        throw new IllegalArgumentException("Exercise not found");
    }

    public ExerciseDTO delete(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
        exerciseRepository.delete(exerciseEntity);
        return exerciseMapper.toDto(exerciseEntity);
    }



}
