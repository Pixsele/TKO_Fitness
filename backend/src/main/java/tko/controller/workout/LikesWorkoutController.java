package tko.controller.workout;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.LikesWorkoutDTO;
import tko.service.workout.LikesWorkoutService;

/**
 * Контроллер управления лайками пользователей на тренировки.
 * <p>
 * Методы:
 * <ul>
 *     <li>{@link #createLikesWorkout(LikesWorkoutDTO)} — добавление лайка на тренировку</li>
 *     <li>{@link #readLikesWorkoutById(Long)} — получение лайка по идентификатору</li>
 *     <li>{@link #updateLikesWorkout(Long, LikesWorkoutDTO)} — обновление лайка</li>
 *     <li>{@link #deleteLikesWorkout(LikesWorkoutDTO)} — удаление лайка</li>
 *     <li>{@link #getLikesWorkouts(Long, Pageable)} — получение лайков по идентификатору пользователя постранично</li>
 * </ul>
 */

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
    //@PreAuthorize("@likesWorkoutService.isLikeOwner(#id,authentication.name)")
    public ResponseEntity<LikesWorkoutDTO> readLikesWorkoutById(@PathVariable Long id) {
        LikesWorkoutDTO readLike = likesWorkoutService.readLikesWorkout(id);
        return new ResponseEntity<>(readLike, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("@likesWorkoutService.isLikeOwner(#id,authentication.name)")
    public ResponseEntity<LikesWorkoutDTO> updateLikesWorkout(@PathVariable Long id, @Valid @RequestBody LikesWorkoutDTO workout) {
        LikesWorkoutDTO updatedLike = likesWorkoutService.updateLikesWorkout(id, workout);
        return new ResponseEntity<>(updatedLike,HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<LikesWorkoutDTO> deleteLikesWorkout(@Valid @RequestBody LikesWorkoutDTO workoutDTO) {
        LikesWorkoutDTO deletedLike = likesWorkoutService.deleteLikesWorkout(workoutDTO);
        return new ResponseEntity<>(deletedLike,HttpStatus.OK);
    }

    @GetMapping("/page/{id}")
    //@PreAuthorize("@likesWorkoutService.isLikeOwner(#id,authentication.name)")
    public PagedModel<LikesWorkoutDTO> getLikesWorkouts(@PathVariable Long id, Pageable pageable) {
        Page<LikesWorkoutDTO> likesWorkoutDTOS = likesWorkoutService.readLikesWorkoutByUserId(id, pageable);
        return new PagedModel<>(likesWorkoutDTOS);
    }
}
