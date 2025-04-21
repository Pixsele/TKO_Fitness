package tko.controller.workout;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.PersonSVGDTO;
import tko.model.dto.workout.WorkoutDTO;
import tko.model.dto.workout.WorkoutForPageDTO;
import tko.service.PersonSVGService;
import tko.service.workout.WorkoutService;
import tko.utils.Gender;

import java.util.List;


@RestController
@RequestMapping("/workout")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final PersonSVGService personSVGService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, PersonSVGService personSVGService) {
        this.workoutService = workoutService;
        this.personSVGService = personSVGService;
    }

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(@Valid @RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO createdWorkout = workoutService.createWorkout(workoutDTO);
        return new ResponseEntity<>(createdWorkout,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> readWorkout(@PathVariable Long id) {
        WorkoutDTO workoutDTO = workoutService.readWorkoutById(id);
        return new ResponseEntity<>(workoutDTO,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDTO> updateWorkout(@PathVariable Long id, @Valid @RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO updatedWorkout = workoutService.updateWorkout(id,workoutDTO);
        return new ResponseEntity<>(updatedWorkout,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WorkoutDTO> deleteWorkout(@PathVariable Long id) {
        WorkoutDTO deletedWorkout = workoutService.deleteWorkoutById(id);
        return new ResponseEntity<>(deletedWorkout,HttpStatus.OK);
    }

    @GetMapping("/page")
    public PagedModel<WorkoutForPageDTO> readWorkoutPage(Pageable pageable) {
        Page<WorkoutForPageDTO> workoutDTOPage = workoutService.readWorkoutWithPageable(pageable);

        return new PagedModel<>(workoutDTOPage);
    }

    @GetMapping("/svg/{id}")
    public ResponseEntity<PersonSVGDTO> getSvg(@PathVariable Long id, @Valid @RequestParam Gender gender) {
        List<String> svgList = personSVGService.getSvgToWorkout(id, gender);
        PersonSVGDTO dto = new PersonSVGDTO();
        dto.setFront(svgList.get(0));
        dto.setBack(svgList.get(1));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
