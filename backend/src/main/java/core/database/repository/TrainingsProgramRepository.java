package core.database.repository;

import core.database.entity.TrainingsProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingsProgramRepository extends JpaRepository<TrainingsProgramEntity, Long> {
}