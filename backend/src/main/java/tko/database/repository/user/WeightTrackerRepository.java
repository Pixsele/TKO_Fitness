package tko.database.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import tko.database.entity.user.UsersEntity;
import tko.database.entity.user.WeightTrackerEntity;
import tko.model.dto.user.WeightTrackerDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WeightTrackerRepository extends JpaRepository<WeightTrackerEntity,Long> {
    boolean existsByTimeDateAndUser_Id(LocalDateTime timeDate, Long userId);

    List<WeightTrackerEntity> findAllByUser_IdOrderByTimeDateDesc(Long userId);

    List<WeightTrackerEntity> findAllByUser_IdAndTimeDateBetween(Long userId, LocalDateTime timeDateAfter, LocalDateTime timeDateBefore);
}
