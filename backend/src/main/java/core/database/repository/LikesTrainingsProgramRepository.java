package core.database.repository;

import core.database.entity.LikesTrainingsProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesTrainingsProgramRepository extends JpaRepository<LikesTrainingsProgramEntity, Long> {
}
