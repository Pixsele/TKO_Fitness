package tko.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.user.WeightTrackerEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.user.WeightTrackerRepository;
import tko.model.dto.user.WeightTrackerDTO;
import tko.model.mapper.WeightTrackerMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Сервис для отслеживания веса пользователей.
 * <p>
 * Методы:
 * <ul>
 *     <li>{@link #create(WeightTrackerDTO)} — создание записи о весе</li>
 *     <li>{@link #read(Long)} — получение записи о весе по идентификатору</li>
 *     <li>{@link #update(Long, WeightTrackerDTO)} — обновление записи о весе</li>
 *     <li>{@link #delete(Long)} — удаление записи о весе по идентификатору</li>
 *     <li>{@link #readByDate(Long, LocalDateTime, LocalDateTime)} — получение записей веса за период</li>
 * </ul>
 */

@Service
public class WeightTrackerService {
    private final WeightTrackerRepository weightTrackerRepository;
    private final UsersRepository usersRepository;
    private final WeightTrackerMapper weightTrackerMapper;
    private final UsersService usersService;

    @Autowired
    public WeightTrackerService(WeightTrackerRepository weightTrackerRepository, UsersRepository usersRepository, WeightTrackerMapper weightTrackerMapper, UsersService usersService) {
        this.weightTrackerRepository = weightTrackerRepository;
        this.usersRepository = usersRepository;
        this.weightTrackerMapper = weightTrackerMapper;
        this.usersService = usersService;
    }

    public WeightTrackerDTO create(WeightTrackerDTO weightTrackerDTO) {
        if(weightTrackerDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(!(usersRepository.existsById(weightTrackerDTO.getUserId()))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        if(weightTrackerRepository.existsByTimeDateAndUser_Id(weightTrackerDTO.getTimeDate(), weightTrackerDTO.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Weight tracker already exists by this date");
        }

        if(!Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(),usersRepository.findById(weightTrackerDTO.getUserId()).get().getLogin() )) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied");
        }


        WeightTrackerEntity weightTrackerEntity = weightTrackerMapper.toEntity(weightTrackerDTO);
        weightTrackerRepository.save(weightTrackerEntity);

        if(Objects.equals(weightTrackerDTO.getTimeDate(), LocalDate.now())) {
            usersService.updateWeight(weightTrackerDTO.getWeight(), weightTrackerDTO.getUserId());
        }

        return weightTrackerMapper.toDto(weightTrackerEntity);
    }

    public WeightTrackerDTO read(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }
        WeightTrackerEntity weightTrackerEntity = weightTrackerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return weightTrackerMapper.toDto(weightTrackerEntity);
    }

    public WeightTrackerDTO update(Long id,WeightTrackerDTO weightTrackerDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        UsersEntity usersEntity = usersRepository.findById(weightTrackerDTO.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));

        if(weightTrackerRepository.existsByTimeDateAndUser_Id(weightTrackerDTO.getTimeDate(), weightTrackerDTO.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Weight tracker already exists by this date");
        }

        WeightTrackerEntity weightTrackerEntity = weightTrackerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        weightTrackerEntity.setWeight(weightTrackerDTO.getWeight());
        weightTrackerEntity.setTimeDate(weightTrackerDTO.getTimeDate());
        weightTrackerEntity.setUser(usersEntity);
        WeightTrackerEntity updatedWeightTrackerEntity = weightTrackerRepository.save(weightTrackerEntity);
        return weightTrackerMapper.toDto(updatedWeightTrackerEntity);
    }

    public WeightTrackerDTO delete(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        WeightTrackerEntity weightTrackerEntity = weightTrackerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id not found"));

        weightTrackerRepository.delete(weightTrackerEntity);
        return weightTrackerMapper.toDto(weightTrackerEntity);
    }

    public List<WeightTrackerDTO> readByDate(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserId must not be null");
        }

        if(!(usersRepository.existsById(userId))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        if (startDate == null || endDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date and end date must not be null");
        }

        List<WeightTrackerEntity> weightTrackerEntityListList = weightTrackerRepository.findAllByUser_IdAndTimeDateBetween(userId, startDate, endDate);


        List<WeightTrackerDTO> weightTrackerDTOList = weightTrackerEntityListList.stream()
                .map(entity -> new WeightTrackerDTO(
                        entity.getId(),
                        entity.getUser().getId(),
                        entity.getWeight(),
                        entity.getTimeDate()
                ))
                .collect(Collectors.toList());

        if(weightTrackerDTOList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No weight records found for the specified period");
        }

        return weightTrackerDTOList;
    }

    public List<WeightTrackerDTO> readLast(Long userId){
        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserId must not be null");
        }

        if(!(usersRepository.existsById(userId))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        List<WeightTrackerEntity> weightTrackerEntityListList = weightTrackerRepository.findAllByUser_IdOrderByTimeDateDesc(userId);


        List<WeightTrackerEntity> latestSeven = weightTrackerEntityListList.stream().limit(7).toList();

        List<WeightTrackerDTO> weightTrackerDTOList = latestSeven.stream()
                .map(entity -> new WeightTrackerDTO(
                        entity.getId(),
                        entity.getUser().getId(),
                        entity.getWeight(),
                        entity.getTimeDate()
                ))
                .collect(Collectors.toList());

        return weightTrackerDTOList;
    }
}
