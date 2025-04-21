package tko.service;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.entity.workout.WorkoutEntity;
import tko.database.entity.workout.WorkoutExerciseEntity;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.database.repository.workout.WorkoutRepository;
import tko.utils.Gender;
import tko.utils.personSVG.Muscle;
import tko.utils.personSVG.PersonGeneratorSVG;

import java.util.*;

@Service
public class PersonSVGService {

    public final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    public PersonSVGService(ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository, WorkoutExerciseRepository workoutExerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }


    public List<String> getSvgToExercise(Long id, Gender gender) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id is null");
        }

        ExerciseEntity exerciseEntity = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id not found"));

        List<Muscle> muscleList = exerciseEntity.getMuscularGroup();

        try {
            return PersonGeneratorSVG.GetPersonWithChangesByIdElements(gender,muscleList);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public List<String> getSvgToWorkout(Long id, Gender gender) {
        if(id == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id is null");
        }

        if(!(workoutRepository.existsById(id))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Workout not found");
        }

        List<WorkoutExerciseEntity> listExercise = workoutExerciseRepository.findAllByWorkout_Id(id);

        Set<Muscle> set = new HashSet<>();

        for(WorkoutExerciseEntity workoutExerciseEntity : listExercise){
            ExerciseEntity exerciseEntity = exerciseRepository.findById(workoutExerciseEntity.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id not found"));

            List<Muscle> muscleList = exerciseEntity.getMuscularGroup();
            set.addAll(muscleList);
        }
        try {
            List<Muscle> list = new ArrayList<>(set);
            return PersonGeneratorSVG.GetPersonWithChangesByIdElements(gender, list);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
