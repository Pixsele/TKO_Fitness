package core.database.repository;

import core.database.entity.WorkoutProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutProgramRepository extends JpaRepository<WorkoutProgramEntity, Long> {
}
