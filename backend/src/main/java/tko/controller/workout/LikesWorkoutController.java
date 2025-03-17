package tko.controller.workout;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.LikesWorkoutDTO;
import tko.service.workout.LikesWorkoutService;

@RestController
@RequestMapping("/like-workout")
public class LikesWorkoutController {

    private final LikesWorkoutService likesWorkoutService;

    @Autowired
    public LikesWorkoutController(LikesWorkoutService likesWorkoutService) {
        this.likesWorkoutService = likesWorkoutService;
    }

    @PostMapping
    public ResponseEntity<LikesWorkoutDTO> createLikesWorkout(@Valid @RequestBody LikesWorkoutDTO workoutDTO) {
        LikesWorkoutDTO createdLike = likesWorkoutService.createLikesWorkout(workoutDTO);
        return new ResponseEntity<>(createdLike, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LikesWorkoutDTO> readLikesWorkoutById(@PathVariable Long id) {
        LikesWorkoutDTO readLike = likesWorkoutService.readLikesWorkout(id);
        return new ResponseEntity<>(readLike, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LikesWorkoutDTO> updateLikesWorkout(@PathVariable Long id, @Valid @RequestBody LikesWorkoutDTO workout) {
        LikesWorkoutDTO updatedLike = likesWorkoutService.updateLikesWorkout(id, workout);
        return new ResponseEntity<>(updatedLike,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LikesWorkoutDTO> deleteLikesWorkout(@PathVariable Long id) {
        LikesWorkoutDTO deletedLike = likesWorkoutService.deleteLikesWorkout(id);
        return new ResponseEntity<>(deletedLike,HttpStatus.OK);
    }

    @GetMapping("/page/{id}")
    public PagedModel<LikesWorkoutDTO> getLikesWorkouts(@PathVariable Long id, Pageable pageable) {
        Page<LikesWorkoutDTO> likesWorkoutDTOS = likesWorkoutService.readLikesWorkoutByUserId(id, pageable);
        return new PagedModel<>(likesWorkoutDTOS);
    }
}
