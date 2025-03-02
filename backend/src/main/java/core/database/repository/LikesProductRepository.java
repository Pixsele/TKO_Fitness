package core.database.repository;

import core.database.entity.LikesProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesProductRepository extends JpaRepository<LikesProductEntity, Long> {
}