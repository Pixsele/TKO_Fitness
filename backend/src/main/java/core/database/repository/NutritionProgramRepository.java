package core.database.repository;

import core.database.entity.NutritionProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionProgramRepository extends JpaRepository<NutritionProgramEntity, Long> {
}