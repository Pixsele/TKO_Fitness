package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.model.dto.workout.ExerciseDTO;
import tko.model.mapper.workout.ExerciseMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper, WorkoutExerciseRepository workoutExerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    public ExerciseDTO create(ExerciseDTO exerciseDTO) {
        if(exerciseDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }
        ExerciseEntity exerciseEntity = exerciseMapper.toEntity(exerciseDTO);
        exerciseEntity.setCreatedAt(LocalDateTime.now());
        ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
        return exerciseMapper.toDto(saveEntity);
    }

    public ExerciseDTO read(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return exerciseMapper.toDto(exerciseEntity);
    }

    public ExerciseDTO update(Long id, ExerciseDTO exerciseDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        exerciseMapper.updateEntity(exerciseDTO,exerciseEntity);
        ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
        return exerciseMapper.toDto(saveEntity);
    }

    public ExerciseDTO delete(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        boolean hasDependencies = workoutExerciseRepository.existsByExercise_Id(id);

        if(hasDependencies) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete exercise: it is used in workout");
        }

        exerciseRepository.delete(exerciseEntity);
        return exerciseMapper.toDto(exerciseEntity);
    }

    public Page<ExerciseDTO> readPageable(Pageable pageable) {
        Page<ExerciseEntity> exerciseEntities = exerciseRepository.findAll(pageable);

        return (exerciseEntities.map(exerciseMapper::toDto));
    }
}
