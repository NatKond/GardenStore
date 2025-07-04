package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private static Product fertilizer;
    private static Product flowerPot;

    @BeforeAll
    static void setUp() {
        fertilizer = Product.builder()
                .productId(1L)
                .name("Universal Fertilizer")
                .price(BigDecimal.valueOf(15.99))
                .discountPrice(BigDecimal.valueOf(12.99))
                .categoryId(1L)
                .description("High-quality universal fertilizer for all plants.")
                .imageUrl("http://example.com/fertilizer.png")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        flowerPot = Product.builder()
                .productId(2L)
                .name("Ceramic Flower Pot")
                .price(BigDecimal.valueOf(9.49))
                .discountPrice(BigDecimal.valueOf(7.99))
                .categoryId(2L)
                .description("Decorative ceramic flower pot.")
                .imageUrl("http://example.com/flowerpot.png")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllProducts_shouldReturnProducts() {
        when(productRepository.findAll()).thenReturn(List.of(fertilizer, flowerPot));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2).containsExactly(fertilizer, flowerPot);
        verify(productRepository).findAll();
    }

    @Test
    void getProductById_shouldReturnProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(fertilizer));

        Product result = productService.getProductById(1L);

        assertThat(result).isEqualTo(fertilizer);
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_shouldThrow_whenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99L));
        verify(productRepository).findById(99L);
    }

    @Test
    void createProduct_shouldSaveProduct() {
        Product gardenShovel = Product.builder()
                .name("Garden Shovel")
                .price(BigDecimal.valueOf(25.00))
                .discountPrice(BigDecimal.valueOf(20.00))
                .categoryId(3L)
                .description("Durable steel garden shovel.")
                .imageUrl("http://example.com/shovel.png")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product savedShovel = Product.builder()
                .productId(3L)
                .name("Garden Shovel")
                .price(BigDecimal.valueOf(25.00))
                .discountPrice(BigDecimal.valueOf(20.00))
                .categoryId(3L)
                .description("Durable steel garden shovel.")
                .imageUrl("http://example.com/shovel.png")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productRepository.save(gardenShovel)).thenReturn(savedShovel);

        Product result = productService.createProduct(gardenShovel);

        assertThat(result).isEqualTo(savedShovel);
        verify(productRepository).save(gardenShovel);
    }

    @Test
    void deleteProductById_shouldDeleteProduct_whenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(fertilizer));

        productService.deleteProductById(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }
}


