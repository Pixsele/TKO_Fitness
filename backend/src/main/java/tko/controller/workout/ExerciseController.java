package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.ExerciseCreateDTO;
import tko.model.dto.workout.ExerciseDTO;
import tko.service.workout.ExerciseService;

import java.util.List;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@Valid @RequestBody ExerciseCreateDTO exerciseDTO) {
        ExerciseDTO createdExercise = exerciseService.create(exerciseDTO);
        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> readExercise(@PathVariable Long id) {
        ExerciseDTO exerciseDTO = exerciseService.read(id);
        return new ResponseEntity<>(exerciseDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ExerciseDTO> updateExercise(@Valid @RequestBody ExerciseDTO exerciseDTO) {
        ExerciseDTO updatedExercise = exerciseService.update(exerciseDTO);
        return new ResponseEntity<>(updatedExercise, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExerciseDTO> deleteExercise(@PathVariable Long id) {
        ExerciseDTO deletedExercise = exerciseService.delete(id);
        return new ResponseEntity<>(deletedExercise, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExerciseDTO>> readAllExercises() {
        List<ExerciseDTO> exerciseDTOList = exerciseService.readAll();
        return new ResponseEntity<>(exerciseDTOList, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<List<ExerciseDTO>> readPage(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        List<ExerciseDTO> exerciseDTOList = exerciseService.readPageable(page, size);
        return new ResponseEntity<>(exerciseDTOList, HttpStatus.OK);
    }
}
