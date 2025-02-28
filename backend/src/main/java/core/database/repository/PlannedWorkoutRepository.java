package core.database.repository;

import core.database.entity.PlannedWorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedWorkoutRepository extends JpaRepository<PlannedWorkoutEntity, Long> {
}
