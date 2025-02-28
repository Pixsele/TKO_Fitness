package core.database.repository;

import core.database.entity.KcalProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KcalProductRepository extends JpaRepository<KcalProductEntity, Long> {
}