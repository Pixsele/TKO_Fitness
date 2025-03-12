package tko.controller.workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.WorkoutExerciseDTO;
import tko.service.workout.WorkoutExerciseService;

@RestController
@RequestMapping("/work_exercise")
public class WorkoutExerciseContoller {
    private final WorkoutExerciseService workoutExerciseService;

    @Autowired
    public WorkoutExerciseContoller(WorkoutExerciseService workoutExerciseService) {
        this.workoutExerciseService = workoutExerciseService;
    }

    @PostMapping
    public ResponseEntity<WorkoutExerciseDTO> createWorkoutExercise(@RequestBody WorkoutExerciseDTO workoutExerciseDTO) {
        WorkoutExerciseDTO createdWorkoutExercise = workoutExerciseService.create(workoutExerciseDTO);
        return new ResponseEntity<>(createdWorkoutExercise, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutExerciseDTO> readWorkoutExercise(@PathVariable Long id) {
        WorkoutExerciseDTO workoutExerciseDTO = workoutExerciseService.read(id);
        return new ResponseEntity<>(workoutExerciseDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<WorkoutExerciseDTO> updateWorkoutExercise(@RequestBody WorkoutExerciseDTO workoutExerciseDTO) {
        WorkoutExerciseDTO updatedWorkoutExercise = workoutExerciseService.update(workoutExerciseDTO);
        return new ResponseEntity<>(updatedWorkoutExercise, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<WorkoutExerciseDTO> deleteWorkoutExercise(@RequestBody WorkoutExerciseDTO workoutExerciseDTO) {
        WorkoutExerciseDTO deletedWorkoutExercise = workoutExerciseService.delete(workoutExerciseDTO.getId());
        return new ResponseEntity<>(deletedWorkoutExercise, HttpStatus.OK);
    }

    @PostMapping("/addtoworkout/{workoutId}")
    public ResponseEntity<WorkoutExerciseDTO> add(@RequestBody WorkoutExerciseDTO workoutExerciseDTO, @PathVariable Long workoutId) {
        WorkoutExerciseDTO result = workoutExerciseService.addToWorkout(workoutExerciseDTO, workoutId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
