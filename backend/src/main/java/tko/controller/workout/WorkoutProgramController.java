package tko.controller.workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.WorkoutProgramDTO;
import tko.service.workout.WorkoutProgramService;

@RestController
@RequestMapping("workout-program")
public class WorkoutProgramController {
    private final WorkoutProgramService workoutProgramService;

    @Autowired
    public WorkoutProgramController(WorkoutProgramService workoutProgramService) {
        this.workoutProgramService = workoutProgramService;
    }

    @PostMapping
    public ResponseEntity<WorkoutProgramDTO> createWorkoutProgram(@RequestBody WorkoutProgramDTO workoutProgramDTO) {
        WorkoutProgramDTO createdWorkoutProgram = workoutProgramService.create(workoutProgramDTO);
        return ResponseEntity.ok(createdWorkoutProgram);
    }
}
