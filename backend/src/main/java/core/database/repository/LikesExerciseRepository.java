package core.database.repository;

import core.database.entity.LikesExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesExerciseRepository extends JpaRepository<LikesExerciseEntity, Long> {
}
