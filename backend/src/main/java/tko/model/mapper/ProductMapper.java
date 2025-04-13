package tko.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import tko.database.entity.nutrition.ProductEntity;
import tko.model.dto.nutrition.ProductDTO;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(ProductEntity productEntity);
    ProductEntity toEntity(ProductDTO productDTO);
    void updateEntity(ProductDTO dto, @MappingTarget ProductEntity entity);
}
