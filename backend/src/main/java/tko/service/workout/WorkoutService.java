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
import tko.database.repository.workout.WorkoutRepository;
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

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper, WorkoutExerciseMapper workoutExerciseMapper) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
        this.workoutExerciseMapper = workoutExerciseMapper;
    }

    public WorkoutDTO createWorkout(@RequestBody WorkoutDTO workoutDTO) {
        WorkoutEntity workoutEntity = workoutMapper.toEntity(workoutDTO);
        workoutEntity.setCreatedAt(LocalDateTime.now());
        WorkoutEntity saveEntity = workoutRepository.save(workoutEntity);
        return workoutMapper.toDTO(saveEntity);
    }

    public WorkoutDTO readWorkoutById(@PathVariable Long id) {
        if(id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        WorkoutEntity workoutEntity = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return workoutMapper.toDTO(workoutEntity);
    }

    //TODO
    public WorkoutDTO updateWorkout(WorkoutDTO workoutDTO) {
        WorkoutEntity workoutEntity = workoutMapper.toEntity(workoutDTO);
        if(workoutRepository.existsById(workoutDTO.getId())) {
            WorkoutEntity saveEntity = workoutRepository.save(workoutEntity);
            return workoutMapper.toDTO(saveEntity);
        }
        throw new EntityNotFoundException("Workout not found");
    }

    public WorkoutDTO deleteWorkoutById(@PathVariable Long id) {
        if(id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        WorkoutEntity deleteEntity = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        workoutRepository.delete(deleteEntity);
        return workoutMapper.toDTO(deleteEntity);
    }

    public List<WorkoutExerciseDTO> readWorkoutExerciseById(@PathVariable Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        List<WorkoutExerciseEntity> workoutExerciseEntityList = workoutRepository.findExercisesByWorkoutId(id);

        List<WorkoutExerciseDTO> workoutExerciseDTOList = new ArrayList<>();
        for(WorkoutExerciseEntity workoutExerciseEntity : workoutExerciseEntityList) {
            workoutExerciseDTOList.add(workoutExerciseMapper.toDTO(workoutExerciseEntity));
        }
        return workoutExerciseDTOList;
    }

    public List<WorkoutDTO> readAllWorkout() {
        List<WorkoutEntity> workoutEntityList = workoutRepository.findAll();
        List<WorkoutDTO> workoutDTOList = new ArrayList<>();
        for(WorkoutEntity workoutEntity : workoutEntityList) {
            workoutDTOList.add(workoutMapper.toDTO(workoutEntity));
        }
        return workoutDTOList;
    }
}
