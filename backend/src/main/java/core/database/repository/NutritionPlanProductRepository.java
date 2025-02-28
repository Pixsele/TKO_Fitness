package core.database.repository;

import core.database.entity.NutritionPlanProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionPlanProductRepository extends JpaRepository<NutritionPlanProductEntity, Long> {
}
