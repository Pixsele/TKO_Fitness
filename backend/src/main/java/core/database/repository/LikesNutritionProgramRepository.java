package core.database.repository;

import core.database.entity.LikesNutritionProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesNutritionProgramRepository extends JpaRepository<LikesNutritionProgramEntity, Long> {
}