package tko.model.mapper.nutrition;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.nutrition.ProductEntity;
import tko.model.dto.nutrition.ProductDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toDto(ProductEntity productEntity) {
        if ( productEntity == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setId( productEntity.getId() );
        productDTO.setName( productEntity.getName() );
        productDTO.setKcal( productEntity.getKcal() );
        productDTO.setUnit( productEntity.getUnit() );
        productDTO.setGrams( productEntity.getGrams() );
        productDTO.setPortion( productEntity.getPortion() );
        productDTO.setCreatedAt( productEntity.getCreatedAt() );
        productDTO.setFats( productEntity.getFats() );
        productDTO.setCarbs( productEntity.getCarbs() );
        productDTO.setProteins( productEntity.getProteins() );

        return productDTO;
    }

    @Override
    public ProductEntity toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setId( productDTO.getId() );
        productEntity.setName( productDTO.getName() );
        if ( productDTO.getKcal() != null ) {
            productEntity.setKcal( productDTO.getKcal() );
        }
        productEntity.setUnit( productDTO.getUnit() );
        productEntity.setGrams( productDTO.getGrams() );
        productEntity.setPortion( productDTO.getPortion() );
        productEntity.setFats( productDTO.getFats() );
        productEntity.setCarbs( productDTO.getCarbs() );
        productEntity.setProteins( productDTO.getProteins() );
        productEntity.setCreatedAt( productDTO.getCreatedAt() );

        return productEntity;
    }

    @Override
    public void updateEntity(ProductDTO dto, ProductEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setId( dto.getId() );
        entity.setName( dto.getName() );
        if ( dto.getKcal() != null ) {
            entity.setKcal( dto.getKcal() );
        }
        entity.setUnit( dto.getUnit() );
        entity.setGrams( dto.getGrams() );
        entity.setPortion( dto.getPortion() );
        entity.setFats( dto.getFats() );
        entity.setCarbs( dto.getCarbs() );
        entity.setProteins( dto.getProteins() );
        entity.setCreatedAt( dto.getCreatedAt() );
    }
}
