package tko.service.workout;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.TrainingsProgramEntity;
import tko.database.entity.workout.WorkoutProgramEntity;
import tko.database.repository.workout.TrainingsProgramRepository;
import tko.database.repository.workout.WorkoutProgramRepository;
import tko.model.dto.workout.TrainingsProgramDTO;
import tko.model.mapper.workout.TrainingsProgramMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrainingsProgramService {

    private final TrainingsProgramRepository trainingsProgramRepository;
    private final TrainingsProgramMapper trainingsProgramMapper;
    private final WorkoutProgramRepository workoutProgramRepository;

    @Autowired
    public TrainingsProgramService(TrainingsProgramRepository trainingsProgramRepository, TrainingsProgramMapper trainingsProgramMapper, WorkoutProgramRepository workoutProgramRepository) {
        this.trainingsProgramRepository = trainingsProgramRepository;
        this.trainingsProgramMapper = trainingsProgramMapper;
        this.workoutProgramRepository = workoutProgramRepository;
    }

    public TrainingsProgramDTO createTrainingsProgram(TrainingsProgramDTO trainingsProgramDTO) {
        if(trainingsProgramDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(trainingsProgramDTO.getLikeCount() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }

        TrainingsProgramEntity trainingsProgramEntity = trainingsProgramMapper.toEntity(trainingsProgramDTO);
        trainingsProgramEntity.setCreatedAt(LocalDateTime.now());
        trainingsProgramEntity.setLikeCount(0);
        TrainingsProgramEntity savedTrainingsProgramEntity = trainingsProgramRepository.save(trainingsProgramEntity);
        return trainingsProgramMapper.toDto(savedTrainingsProgramEntity);
    }

    public TrainingsProgramDTO readTrainingsProgramById(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        TrainingsProgramEntity trainingsProgramEntity = trainingsProgramRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return trainingsProgramMapper.toDto(trainingsProgramEntity);
    }

    public TrainingsProgramDTO updateTrainingsProgram(Long id,TrainingsProgramDTO trainingsProgramDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }
        if(trainingsProgramDTO.getLikeCount() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }

        TrainingsProgramEntity trainingsProgramEntity = trainingsProgramRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        trainingsProgramMapper.updateEntity(trainingsProgramDTO, trainingsProgramEntity);
        TrainingsProgramEntity savedTrainingsProgramEntity = trainingsProgramRepository.save(trainingsProgramEntity);
        return trainingsProgramMapper.toDto(savedTrainingsProgramEntity);
    }

    public TrainingsProgramDTO deleteTrainingsProgram(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        TrainingsProgramEntity deleteEntity = trainingsProgramRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        List<WorkoutProgramEntity> workoutProgramEntities = workoutProgramRepository.findAllByProgram_Id(id);
        workoutProgramRepository.deleteAll(workoutProgramEntities);
        trainingsProgramRepository.delete(deleteEntity);
        return trainingsProgramMapper.toDto(deleteEntity);
    }

    public Page<TrainingsProgramDTO> readTrainingsProgramPageable(Pageable pageable) {
        if(pageable == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pageable must not be null");
        }
        Page<TrainingsProgramEntity> trainingsProgramEntityList = trainingsProgramRepository.findAll(pageable);

        return (trainingsProgramEntityList.map(trainingsProgramMapper::toDto));
    }

}
