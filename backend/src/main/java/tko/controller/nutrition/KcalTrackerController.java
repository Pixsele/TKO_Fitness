package tko.controller.nutrition;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tko.model.dto.nutrition.KcalTrackerDTO;
import tko.service.nutrition.KcalTrackerService;

import java.time.LocalDate;

/**
 * Контроллер управления трекером калорий.
 * <p>
 * Методы:
 * <ul>
 *     <li>{@link #createTracker(KcalTrackerDTO)} — создание нового трекера калорий</li>
 *     <li>{@link #readTracker(Long)} — получение трекера по идентификатору</li>
 *     <li>{@link #updateTracker(Long, KcalTrackerDTO)} — обновление трекера</li>
 *     <li>{@link #deleteTracker(Long)} — удаление трекера по идентификатору</li>
 *     <li>{@link #readTrackersByDate(java.time.LocalDate)} — получение трекера по дате</li>
 * </ul>
 */

@RestController
@RequestMapping("/kcal-tracker")
public class KcalTrackerController {

    private final KcalTrackerService kcalTrackerService;

    @Autowired
    public KcalTrackerController(KcalTrackerService kcalTrackerService) {
        this.kcalTrackerService = kcalTrackerService;
    }

    @PostMapping
    public ResponseEntity<KcalTrackerDTO> createTracker(@Valid @RequestBody KcalTrackerDTO kcalTrackerDTO) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.createKcalTracker(kcalTrackerDTO);
        return new ResponseEntity<>(trackerDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KcalTrackerDTO> readTracker(@PathVariable Long id) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.readKcalTracker(id);
        return new ResponseEntity<>(trackerDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KcalTrackerDTO> updateTracker(@PathVariable Long id ,@Valid @RequestBody KcalTrackerDTO kcalTrackerDTO) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.updateKcalTracker(id, kcalTrackerDTO);
        return new ResponseEntity<>(trackerDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<KcalTrackerDTO> deleteTracker(@PathVariable Long id) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.deleteKcalTracker(id);
        return new ResponseEntity<>(trackerDTO, HttpStatus.OK);
    }

    @GetMapping("/by-date/{date}")
    public ResponseEntity<KcalTrackerDTO> readTrackersByDate(@PathVariable LocalDate date) {
        KcalTrackerDTO trackerDTO = kcalTrackerService.getKcalTrackerByDate(date);
        return new ResponseEntity<>(trackerDTO, HttpStatus.OK);
    }
}
