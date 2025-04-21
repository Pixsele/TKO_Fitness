package tko.service.workout;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.workout.ExerciseEntity;
import tko.database.entity.workout.LikesExerciseEntity;
import tko.database.repository.user.UsersRepository;
import tko.database.repository.workout.ExerciseRepository;
import tko.database.repository.workout.LikesExerciseRepository;
import tko.database.repository.workout.WorkoutExerciseRepository;
import tko.model.dto.workout.ExerciseDTO;
import tko.model.dto.workout.ExerciseForPageDTO;
import tko.model.dto.workout.ExerciseMediaDTO;
import tko.model.mapper.workout.ExerciseMapper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final LikesExerciseRepository likesExerciseRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper, WorkoutExerciseRepository workoutExerciseRepository, LikesExerciseRepository likesExerciseRepository, UsersRepository usersRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.likesExerciseRepository = likesExerciseRepository;
        this.usersRepository = usersRepository;
    }

    public ExerciseDTO create(ExerciseDTO exerciseDTO) {
        if(exerciseDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(exerciseDTO.getLikeCount() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }
        try{
            ExerciseEntity exerciseEntity = exerciseMapper.toEntity(exerciseDTO);
            exerciseEntity.setLikeCount(0);
            ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
            return exerciseMapper.toDto(saveEntity);
        }catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid exercise data");
        }
    }

    public ExerciseDTO read(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        return exerciseMapper.toDto(exerciseEntity);
    }

    public ExerciseDTO update(Long id, ExerciseDTO exerciseDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        if(exerciseDTO.getLikeCount() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }

        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        exerciseMapper.updateEntity(exerciseDTO,exerciseEntity);

        if(exerciseEntity.getMuscularGroup().contains(null)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid muscular group");
        }

        ExerciseEntity saveEntity = exerciseRepository.save(exerciseEntity);
        return exerciseMapper.toDto(saveEntity);
    }

    public ExerciseDTO delete(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        boolean hasDependencies = workoutExerciseRepository.existsByExercise_Id(id);

        if(hasDependencies) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete exercise: it is used in workout");
        }

        List<LikesExerciseEntity> likesExerciseEntities = likesExerciseRepository.findAllByExercise_Id(id);

        likesExerciseRepository.deleteAll(likesExerciseEntities);
        exerciseRepository.delete(exerciseEntity);
        return exerciseMapper.toDto(exerciseEntity);
    }

    public Page<ExerciseForPageDTO> readPageable(Pageable pageable) {
        if(pageable == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pageable must not be null");
        }
        Page<ExerciseEntity> exerciseEntities = exerciseRepository.findAll(pageable);

        List<ExerciseForPageDTO> list = new ArrayList<>();
        for(ExerciseEntity exerciseEntity : exerciseEntities) {
            ExerciseForPageDTO dto = new ExerciseForPageDTO();
            dto.setId(exerciseEntity.getId());
            dto.setName(exerciseEntity.getName());
            dto.setLikeCount(exerciseEntity.getLikeCount());
            dto.setLiked(likesExerciseRepository.existsByUser_IdAndExercise_Id(
                    usersRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName()).getId(),
                    exerciseEntity.getId()
            ));

            list.add(dto);
        }

        return new PageImpl<>(list);
    }

    public void addLike(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        Integer likeCount = exerciseEntity.getLikeCount();
        likeCount = likeCount + 1;
        exerciseEntity.setLikeCount(likeCount);
        exerciseRepository.save(exerciseEntity);
    }

    public void removeLike(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity exerciseEntity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        Integer likeCount = exerciseEntity.getLikeCount();
        likeCount = likeCount - 1;
        exerciseEntity.setLikeCount(likeCount);
        exerciseRepository.save(exerciseEntity);
    }

    public ExerciseMediaDTO getMetaData(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity entity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        ExerciseMediaDTO dto = new ExerciseMediaDTO();

        dto.setId(entity.getId());
        dto.setUrlImage(entity.getPhotoUrl());
        dto.setUrlVideo(entity.getVideoUrl());
        dto.setImageUpdated(entity.getImageUpdated());
        dto.setVideoUpdated(entity.getVideoUpdated());

        return dto;
    }

    public ExerciseDTO updateMedia(Long id, ExerciseMediaDTO exerciseMediaDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }
        ExerciseEntity entity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        if(exerciseMediaDTO.getUrlImage() == null && exerciseMediaDTO.getUrlVideo() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid url of video and image");
        }

        if(exerciseMediaDTO.getUrlImage() != null) {
            entity.setPhotoUrl(exerciseMediaDTO.getUrlImage());
            entity.setImageUpdated(LocalDateTime.now());
        }
        if(exerciseMediaDTO.getUrlVideo() != null) {
            entity.setVideoUrl(exerciseMediaDTO.getUrlVideo());
            entity.setVideoUpdated(LocalDateTime.now());
        }

        exerciseRepository.save(entity);
        return exerciseMapper.toDto(entity);
    }

    public Resource getImage(Long id) {

        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity entity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        try {
            if(entity.getPhotoUrl() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VideoUrl must not be null");
            }

            String fileName = "/app/media/image/" + entity.getPhotoUrl();

            Path file = Paths.get(fileName);
            Resource resource = new FileSystemResource(file);

            if(resource.exists() && resource.isReadable()) {
                return resource;
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found or not readable");
            }
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Resource getVideo(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ExerciseEntity entity = exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));

        try {
            if(entity.getVideoUrl() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VideoUrl must not be null");
            }

            String fileName = "/app/media/video/" + entity.getVideoUrl();


            Path file = Paths.get(fileName);
            Resource resource = new FileSystemResource(file);

            if(resource.exists() && resource.isReadable()) {
                return resource;
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found or not readable");
            }
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
