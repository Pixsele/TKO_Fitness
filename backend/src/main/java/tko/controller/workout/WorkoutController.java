package tko.controller.workout;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.WorkoutCreateDTO;
import tko.model.dto.workout.WorkoutDTO;
import tko.model.dto.workout.WorkoutExerciseDTO;
import tko.service.workout.WorkoutService;

import java.util.List;

@RestController
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(@Valid @RequestBody WorkoutCreateDTO workoutDTO) {
        WorkoutDTO createdWorkout = workoutService.createWorkout(workoutDTO);
        return new ResponseEntity<>(createdWorkout,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> readWorkout(@PathVariable Long id) {
        WorkoutDTO workoutDTO = workoutService.readWorkoutById(id);
        return new ResponseEntity<>(workoutDTO,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<WorkoutDTO> updateWorkout(@Valid @RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO updatedWorkout = workoutService.updateWorkout(workoutDTO);
        return new ResponseEntity<>(updatedWorkout,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WorkoutDTO> deleteWorkout(@PathVariable Long id) {
        WorkoutDTO deletedWorkout = workoutService.deleteWorkoutById(id);
        return new ResponseEntity<>(deletedWorkout,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<WorkoutDTO>> getAll() {
        List<WorkoutDTO> workoutDTOList = workoutService.readAllWorkout();
        return new ResponseEntity<>(workoutDTOList,HttpStatus.OK);
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<WorkoutExerciseDTO>> withWorkoutExercise(@RequestParam Long id) {
        List<WorkoutExerciseDTO> workoutExerciseDTOList = workoutService.readWorkoutExerciseById(id);
        return new ResponseEntity<>(workoutExerciseDTOList,HttpStatus.OK);
    }
}
