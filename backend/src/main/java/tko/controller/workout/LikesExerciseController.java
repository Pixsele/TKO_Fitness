package tko.controller.workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.database.entity.workout.LikesExerciseEntity;
import tko.model.dto.workout.LikesExerciseDTO;
import tko.service.workout.LikesExerciseService;

@RestController
@RequestMapping("/like_exercise")
public class LikesExerciseController {

    private final LikesExerciseService likesExerciseService;

    @Autowired
    public LikesExerciseController(LikesExerciseService likesExerciseService) {
        this.likesExerciseService = likesExerciseService;
    }

    @PostMapping
    public ResponseEntity<LikesExerciseDTO> like(@RequestBody LikesExerciseDTO likesExerciseDTO) {
        LikesExerciseDTO createdLike = likesExerciseService.createLikesExercise(likesExerciseDTO);
        return new ResponseEntity<>(createdLike,HttpStatus.CREATED);
    }
}
