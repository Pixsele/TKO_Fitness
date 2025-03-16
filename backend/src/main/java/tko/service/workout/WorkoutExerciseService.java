package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.WorkoutExerciseDTO;
import tko.model.mapper.workout.WorkoutExerciseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutExerciseService {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutExerciseMapper workoutExerciseMapper;

    @Autowired
    public WorkoutExerciseService(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, WorkoutExerciseRepository workoutExerciseRepository, WorkoutExerciseMapper workoutExerciseMapper) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutExerciseMapper = workoutExerciseMapper;
    }

    public WorkoutExerciseDTO create(WorkoutExerciseDTO workoutExerciseDTO) {
        if(workoutExerciseDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(!(workoutRepository.existsById(workoutExerciseDTO.getWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkoutId not found");
        }

        if(!(exerciseRepository.existsById(workoutExerciseDTO.getExerciseId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ExerciseId not found");
        }

        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseMapper.toEntity(workoutExerciseDTO);
        WorkoutExerciseEntity result = workoutExerciseRepository.save(workoutExerciseEntity);
        return workoutExerciseMapper.toDTO(result);
    }

    public WorkoutExerciseDTO read(Long id) {
        if(id==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }
        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Id not found"));
        return workoutExerciseMapper.toDTO(workoutExerciseEntity);
    }

    public WorkoutExerciseDTO update(Long id,WorkoutExerciseDTO workoutExerciseDTO) {
        if(id==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if(!(workoutRepository.existsById(workoutExerciseDTO.getWorkoutId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkoutId not found");
        }

        if(!(exerciseRepository.existsById(workoutExerciseDTO.getExerciseId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ExerciseId not found");
        }

        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Id not found"));

        workoutExerciseMapper.updateEntity(workoutExerciseDTO, workoutExerciseEntity);
        WorkoutExerciseEntity savedEntity = workoutExerciseRepository.save(workoutExerciseEntity);
        return workoutExerciseMapper.toDTO(savedEntity);
    }

    public WorkoutExerciseDTO delete(Long id) {
        if(id==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        WorkoutExerciseEntity deleteEntity = workoutExerciseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Id not found"));
        workoutExerciseRepository.delete(deleteEntity);
        return workoutExerciseMapper.toDTO(deleteEntity);
    }

    public List<WorkoutExerciseDTO> findAllByWorkoutId(Long workoutId) {
        if(workoutId==null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if(!(workoutRepository.existsById(workoutId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkoutId not found");
        }

        List<WorkoutExerciseEntity> workoutExerciseEntityList = workoutExerciseRepository.findAllByWorkout_Id(workoutId);

        return workoutExerciseEntityList.stream().map(workoutExerciseMapper::toDTO).collect(Collectors.toList());
    }
}
