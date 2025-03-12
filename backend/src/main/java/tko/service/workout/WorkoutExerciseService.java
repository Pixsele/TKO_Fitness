package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.model.dto.workout.WorkoutExerciseDTO;
import tko.model.mapper.workout.WorkoutExerciseMapper;

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
        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseMapper.toEntity(workoutExerciseDTO);
        WorkoutExerciseEntity result = workoutExerciseRepository.save(workoutExerciseEntity);
        return workoutExerciseMapper.toDTO(result);
    }

    public WorkoutExerciseDTO read(Long id) {
        if(id==null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Id not found"));
        return workoutExerciseMapper.toDTO(workoutExerciseEntity);
    }

    public WorkoutExerciseDTO update(WorkoutExerciseDTO workoutExerciseDTO) {
        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseMapper.toEntity(workoutExerciseDTO);
        if(workoutExerciseRepository.existsById(workoutExerciseDTO.getId())) {
            WorkoutExerciseEntity saveEntity = workoutExerciseRepository.save(workoutExerciseEntity);
            return workoutExerciseMapper.toDTO(saveEntity);
        }
        throw new EntityNotFoundException("Workout not found");
    }

    public WorkoutExerciseDTO delete(Long id) {
        if(id==null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        WorkoutExerciseEntity deleteEnity = workoutExerciseRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Id not found"));
        workoutExerciseRepository.delete(deleteEnity);
        return workoutExerciseMapper.toDTO(deleteEnity);
    }
    @Transactional
    public WorkoutExerciseDTO addToWorkout(WorkoutExerciseDTO workoutExerciseDTO, Long workoutId) {
        if(workoutId==null) {
            throw new IllegalArgumentException("workoutId cannot be null");
        }
        WorkoutEntity workoutEntity = workoutRepository.findByIdWithExercises(workoutId).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        ExerciseEntity exerciseEntity = exerciseRepository.findById(workoutExerciseDTO.getExerciseId()).orElseThrow(()-> new EntityNotFoundException("ExerciseId not found"));

        WorkoutExerciseEntity workoutExerciseEntity = new WorkoutExerciseEntity();

        workoutExerciseEntity.setExercise(exerciseEntity);
        workoutExerciseEntity.setWorkout(workoutEntity);
        workoutExerciseEntity.setReps(workoutExerciseDTO.getReps());
        workoutExerciseEntity.setSets(workoutExerciseDTO.getSets());
        workoutExerciseEntity.setDuration(workoutExerciseDTO.getDuration());
        workoutExerciseEntity.setDistance(workoutExerciseDTO.getDistance());
        workoutExerciseEntity.setRestTime(workoutExerciseDTO.getRestTime());
        workoutExerciseRepository.save(workoutExerciseEntity);
        return workoutExerciseMapper.toDTO(workoutExerciseEntity);
    }
}
