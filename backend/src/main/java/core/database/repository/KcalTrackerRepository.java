package core.database.repository;

import core.database.entity.KcalTrackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KcalTrackerRepository extends JpaRepository<KcalTrackerEntity, Long> {
}