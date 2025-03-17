package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.database.repository.workout.WorkoutExerciseRepository;
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
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutMapper workoutMapper, WorkoutExerciseRepository workoutExerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.workoutMapper = workoutMapper;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    public WorkoutDTO createWorkout(WorkoutDTO workoutDTO) {
        if (workoutDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }
        if(workoutDTO.getLikeCount() != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }

        WorkoutEntity workoutEntity = workoutMapper.toEntity(workoutDTO);
        workoutEntity.setCreatedAt(LocalDateTime.now());
        workoutEntity.setLikeCount(0);
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

    public WorkoutDTO updateWorkout(Long id,WorkoutDTO workoutDTO) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        if(workoutDTO.getLikeCount() != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }

        WorkoutEntity workoutEntity = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        workoutMapper.updateEntity(workoutDTO, workoutEntity);
        WorkoutEntity saveEntity = workoutRepository.save(workoutEntity);
        return workoutMapper.toDTO(saveEntity);
    }

    public WorkoutDTO deleteWorkoutById(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        WorkoutEntity deleteEntity = workoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        List<WorkoutExerciseEntity> workoutExerciseEntities = workoutExerciseRepository.findByWorkout_Id(deleteEntity.getId());

        //TODO Check Dependencies with Training Program

        workoutExerciseRepository.deleteAll(workoutExerciseEntities);
        workoutRepository.delete(deleteEntity);
        return workoutMapper.toDTO(deleteEntity);
    }

    public Page<WorkoutDTO> readWorkoutWithPageable(Pageable pageable) {
        if(pageable == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pageable must not be null");
        }
        Page<WorkoutEntity> workoutEntityList = workoutRepository.findAll(pageable);

        return (workoutEntityList.map(workoutMapper::toDTO));
    }
}
