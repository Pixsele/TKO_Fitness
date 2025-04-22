package tko.model.mapper.nutrition;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tko.database.entity.nutrition.KcalProductEntity;
import tko.database.entity.nutrition.KcalTrackerEntity;
import tko.database.entity.nutrition.ProductEntity;
import tko.model.dto.nutrition.KcalProductDTO;
import tko.utils.MealType;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T16:01:06+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class KcalProductMapperImpl implements KcalProductMapper {

    @Override
    public KcalProductDTO toDto(KcalProductEntity entity) {
        if ( entity == null ) {
            return null;
        }

        KcalProductDTO kcalProductDTO = new KcalProductDTO();

        kcalProductDTO.setKcalTrackerId( entityKcalTrackerId( entity ) );
        kcalProductDTO.setProductId( entityProductId( entity ) );
        kcalProductDTO.setId( entity.getId() );
        kcalProductDTO.setCount( entity.getCount() );
        if ( entity.getTypeMeal() != null ) {
            kcalProductDTO.setTypeMeal( entity.getTypeMeal().name() );
        }

        return kcalProductDTO;
    }

    @Override
    public KcalProductEntity toEntity(KcalProductDTO dto) {
        if ( dto == null ) {
            return null;
        }

        KcalProductEntity kcalProductEntity = new KcalProductEntity();

        kcalProductEntity.setKcalTracker( kcalProductDTOToKcalTrackerEntity( dto ) );
        kcalProductEntity.setProduct( kcalProductDTOToProductEntity( dto ) );
        kcalProductEntity.setId( dto.getId() );
        kcalProductEntity.setCount( dto.getCount() );
        if ( dto.getTypeMeal() != null ) {
            kcalProductEntity.setTypeMeal( Enum.valueOf( MealType.class, dto.getTypeMeal() ) );
        }

        return kcalProductEntity;
    }

    @Override
    public void updateEntity(KcalProductDTO dto, KcalProductEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setId( dto.getId() );
        entity.setCount( dto.getCount() );
        if ( dto.getTypeMeal() != null ) {
            entity.setTypeMeal( Enum.valueOf( MealType.class, dto.getTypeMeal() ) );
        }
        else {
            entity.setTypeMeal( null );
        }
    }

    private Long entityKcalTrackerId(KcalProductEntity kcalProductEntity) {
        if ( kcalProductEntity == null ) {
            return null;
        }
        KcalTrackerEntity kcalTracker = kcalProductEntity.getKcalTracker();
        if ( kcalTracker == null ) {
            return null;
        }
        Long id = kcalTracker.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long entityProductId(KcalProductEntity kcalProductEntity) {
        if ( kcalProductEntity == null ) {
            return null;
        }
        ProductEntity product = kcalProductEntity.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected KcalTrackerEntity kcalProductDTOToKcalTrackerEntity(KcalProductDTO kcalProductDTO) {
        if ( kcalProductDTO == null ) {
            return null;
        }

        KcalTrackerEntity kcalTrackerEntity = new KcalTrackerEntity();

        kcalTrackerEntity.setId( kcalProductDTO.getKcalTrackerId() );

        return kcalTrackerEntity;
    }

    protected ProductEntity kcalProductDTOToProductEntity(KcalProductDTO kcalProductDTO) {
        if ( kcalProductDTO == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setId( kcalProductDTO.getProductId() );

        return productEntity;
    }
}
