package tko.service.nutrition;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tko.database.entity.nutrition.ProductEntity;
import tko.database.repository.nutrition.ProductRepository;
import tko.model.dto.nutrition.ProductDTO;
import tko.model.mapper.ProductMapper;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        if(productDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(productDTO.getLikeCount() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }

        if(productDTO.getCreatedAt() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CreatedAt must be null");
        }

        productDTO.setCreatedAt(LocalDateTime.now());
        productDTO.setLikeCount(0);

        ProductEntity productEntity = productMapper.toEntity(productDTO);
        productRepository.save(productEntity);
        return productMapper.toDto(productEntity);
    }

    public ProductDTO readProduct(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return productMapper.toDto(productEntity);
    }

    public ProductDTO updateProduct(Long id,ProductDTO productDTO) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        if(productDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Id must be null");
        }

        if(productDTO.getLikeCount() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Like count must be null");
        }

        if(productDTO.getCreatedAt() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CreatedAt must be null");
        }

        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productMapper.updateEntity(productDTO, productEntity);
        productRepository.save(productEntity);
        return productMapper.toDto(productEntity);
    }

    public ProductDTO deleteProduct(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Id must not be null");
        }

        ProductEntity deletedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        //TODO Check to delete

        productRepository.delete(deletedProduct);
        return productMapper.toDto(deletedProduct);
    }

    //TODO read all

    public void addLike(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

       ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        Integer likeCount = productEntity.getLikeCount();
        likeCount = likeCount + 1;
        productEntity.setLikeCount(likeCount);
        productRepository.save(productEntity);
    }

    public void removeLike(Long id) {
        if(id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id must not be null");
        }

        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found"));
        Integer likeCount = productEntity.getLikeCount();
        likeCount = likeCount + 1;
        productEntity.setLikeCount(likeCount);
        productRepository.save(productEntity);
    }
}
