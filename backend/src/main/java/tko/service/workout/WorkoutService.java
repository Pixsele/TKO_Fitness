package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.WorkoutCreateDTO;
import tko.model.dto.workout.WorkoutDTO;
import tko.model.dto.workout.WorkoutExerciseDTO;
import tko.model.mapper.workout.WorkoutExerciseMapper;
import tko.model.mapper.workout.WorkoutMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final WorkoutExerciseMapper workoutExerciseMapper;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper, WorkoutExerciseMapper workoutExerciseMapper, WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
        this.workoutExerciseMapper = workoutExerciseMapper;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    public WorkoutDTO createWorkout(WorkoutCreateDTO workoutCreateDTO) {
        WorkoutEntity workoutEntity = workoutMapper.toEntity(workoutCreateDTO);
        workoutEntity.setCreatedAt(LocalDateTime.now());
        WorkoutEntity saveEntity = workoutRepository.save(workoutEntity);
        return workoutMapper.toDTO(saveEntity);
    }

    public WorkoutDTO readWorkoutById(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        WorkoutEntity workoutEntity = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return workoutMapper.toDTO(workoutEntity);
    }

    public WorkoutDTO updateWorkout(WorkoutDTO workoutDTO) {
        WorkoutEntity workoutEntity = workoutMapper.toEntity(workoutDTO);
        if(workoutRepository.existsById(workoutDTO.getId())) {
            WorkoutEntity saveEntity = workoutRepository.save(workoutEntity);
            return workoutMapper.toDTO(saveEntity);
        }
        throw new EntityNotFoundException("Workout not found");
    }

    public WorkoutDTO deleteWorkoutById(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        WorkoutEntity deleteEntity = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        List<WorkoutExerciseDTO> workoutExerciseEntities = workoutExerciseRepository.findByWorkout_Id(deleteEntity.getId());

        for(WorkoutExerciseDTO workoutExerciseDTO : workoutExerciseEntities) {
            workoutExerciseRepository.deleteById(workoutExerciseDTO.getId());
        }

        workoutRepository.delete(deleteEntity);
        return workoutMapper.toDTO(deleteEntity);
    }

    public List<WorkoutDTO> readAllWorkout() {
        List<WorkoutEntity> workoutEntityList = workoutRepository.findAll();
        List<WorkoutDTO> workoutDTOList = new ArrayList<>();
        for(WorkoutEntity workoutEntity : workoutEntityList) {
            workoutDTOList.add(workoutMapper.toDTO(workoutEntity));
        }
        return workoutDTOList;
    }

    public List<WorkoutExerciseDTO> readWorkoutExerciseById(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        List<WorkoutExerciseEntity> workoutExerciseEntityList = workoutRepository.findExercisesByWorkoutId(id);

        List<WorkoutExerciseDTO> workoutExerciseDTOList = new ArrayList<>();
        for(WorkoutExerciseEntity workoutExerciseEntity : workoutExerciseEntityList) {
            workoutExerciseDTOList.add(workoutExerciseMapper.toDTO(workoutExerciseEntity));
        }
        return workoutExerciseDTOList;
    }
}
