package tko.controller.workout;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.workout.ExerciseDTO;
import tko.model.dto.workout.ExerciseForPageDTO;
import tko.model.dto.workout.ExerciseMediaDTO;
import tko.model.dto.workout.PersonSVGDTO;
import tko.service.PersonSVGService;
import tko.service.workout.ExerciseService;
import tko.utils.Gender;

import java.util.List;

/**
 * Контроллер управления упражнениями.
 * <p>
 * Методы:
 * <ul>
 *     <li>{@link #createExercise(ExerciseDTO)} — создание нового упражнения</li>
 *     <li>{@link #readExercise(Long)} — получение упражнения по идентификатору</li>
 *     <li>{@link #updateExercise(Long, ExerciseDTO)} — обновление данных упражнения</li>
 *     <li>{@link #deleteExercise(Long)} — удаление упражнения</li>
 *     <li>{@link #readPage(Pageable)} — получение списка упражнений постранично</li>
 *     <li>{@link #readMetaData(Long)} — получение медиафайлов к упражнению</li>
 *     <li>{@link #update(Long, ExerciseMediaDTO)} — обновление медиафайлов упражнения</li>
 *     <li>{@link #getImage(Long)} — получение изображения упражнения</li>
 *     <li>{@link #getVideo(Long)} — получение видео упражнения</li>
 *     <li>{@link #getSvg(Long, Gender)} — получение SVG-модели тела по полу</li>
 * </ul>
 */

@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService exerciseService;
    private final PersonSVGService personSVGService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService, PersonSVGService personSVGService) {
        this.exerciseService = exerciseService;
        this.personSVGService = personSVGService;
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@Valid @RequestBody ExerciseDTO exerciseDTO) {
        ExerciseDTO createdExercise = exerciseService.create(exerciseDTO);
        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> readExercise(@PathVariable Long id) {
        ExerciseDTO exerciseDTO = exerciseService.read(id);
        return new ResponseEntity<>(exerciseDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> updateExercise(@PathVariable Long id, @Valid @RequestBody ExerciseDTO exerciseDTO) {
        ExerciseDTO updatedExercise = exerciseService.update(id,exerciseDTO);
        return new ResponseEntity<>(updatedExercise, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExerciseDTO> deleteExercise(@PathVariable Long id) {
        ExerciseDTO deletedExercise = exerciseService.delete(id);
        return new ResponseEntity<>(deletedExercise, HttpStatus.OK);
    }

    @GetMapping("/page")
    public PagedModel<ExerciseForPageDTO> readPage(Pageable pageable) {
        Page<ExerciseForPageDTO> exerciseDTOList = exerciseService.readPageable(pageable);

        return new PagedModel<>(exerciseDTOList);
    }

    @GetMapping("meta-data/{id}")
    public ResponseEntity<ExerciseMediaDTO> readMetaData(@PathVariable Long id) {
        ExerciseMediaDTO dto = exerciseService.getMetaData(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/meta-data/{id}")
    public ResponseEntity<ExerciseMediaDTO> update(@PathVariable Long id, @Valid @RequestBody ExerciseMediaDTO dto) {
        ExerciseDTO updatedto = exerciseService.updateMedia(id,dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
        Resource file = exerciseService.getImage(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<Resource> getVideo(@PathVariable Long id) {
        Resource file = exerciseService.getVideo(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf("video/mp4")).body(file);
    }

    @GetMapping("/svg/{id}")
    public ResponseEntity<PersonSVGDTO> getSvg(@PathVariable Long id, @Valid @RequestParam Gender gender) {
        List<String> svgList = personSVGService.getSvgToExercise(id, gender);
        PersonSVGDTO dto = new PersonSVGDTO();
        dto.setFront(svgList.get(0));
        dto.setBack(svgList.get(1));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}



