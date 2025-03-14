package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.repository.workout.ExerciseRepository;
import tko.model.dto.workout.ExerciseCreateDTO;
import tko.model.dto.workout.ExerciseDTO;
import tko.model.mapper.workout.ExerciseMapper;
import tko.utils.DifficultyLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    public ExerciseDTO create(ExerciseCreateDTO exerciseCreateDTO) {
        ExerciseEntity exerciseEntity = exerciseMapper.toEntity(exerciseCreateDTO);
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

    public ExerciseDTO update(ExerciseDTO exerciseDTO) {
        ExerciseEntity exerciseEntity = exerciseMapper.toEntity(exerciseDTO);
        if(exerciseRepository.existsById(exerciseDTO.getId())) {
            ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
            return exerciseMapper.toDto(saveEntity);
        }
        throw new EntityNotFoundException("Exercise not found");
    }

    public ExerciseDTO delete(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        exerciseRepository.delete(exerciseEntity);
        return exerciseMapper.toDto(exerciseEntity);
    }

    public List<ExerciseDTO> readAll() {
        List<ExerciseEntity> exerciseEntities = exerciseRepository.findAll();
        List<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        for(ExerciseEntity exerciseEntity : exerciseEntities) {
            exerciseDTOS.add(exerciseMapper.toDto(exerciseEntity));
        }
        return exerciseDTOS;
    }

    public List<ExerciseDTO> readPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ExerciseEntity> exerciseEntities = exerciseRepository.findAll(pageable);
        List<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        for (ExerciseEntity exerciseEntity : exerciseEntities.getContent()) {
            exerciseDTOS.add(exerciseMapper.toDto(exerciseEntity));
        }
        return exerciseDTOS;
    }
}
