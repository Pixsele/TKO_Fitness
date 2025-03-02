package core.database.repository;

import core.database.entity.LikesWorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesWorkoutRepository extends JpaRepository<LikesWorkoutEntity, Long> {
}