package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;
    private Product productToCreate;
    private Product productCreated;

    @BeforeEach
     void setUp() {
        product1 = Product.builder()
                .productId(1L)
                .name("All-Purpose Plant Fertilizer")
                .discountPrice(new BigDecimal("8.99"))
                .price(new BigDecimal("11.99"))
                .categoryId(1L)
                .description("Balanced NPK formula for all types of plants")
                .imageUrl("https://example.com/images/fertilizer_all_purpose.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        product2 = Product.builder()
                .productId(2L)
                .name("Organic Tomato Feed")
                .discountPrice(new BigDecimal("10.49"))
                .price(new BigDecimal("13.99"))
                .categoryId(1L)
                .description("Organic liquid fertilizer ideal for tomatoes and vegetables")
                .imageUrl("https://example.com/images/fertilizer_tomato_feed.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productToCreate = Product.builder()
                .name("Slug & Snail Barrier Pellets")
                .discountPrice(new BigDecimal("5.75"))
                .price(new BigDecimal("7.50"))
                .categoryId(2L)
                .description("Pet-safe barrier pellets to protect plants from slugs")
                .imageUrl("https://example.com/images/protection_slug_pellets.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productCreated = productToCreate.toBuilder()
                .productId(3L)
                .build();
    }

    @DisplayName("Get all products")
    @Test
    void getAllProducts() {
        List<Product> expected = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(expected);

        List<Product> actual = productService.getAllProducts();

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
        verify(productRepository).findAll();
    }

    @DisplayName("Get product by ID : positive case")
    @Test
    void getProductByIdPositiveCase() {
        Long productId = 1L;

        Product expected = product1;

        when(productRepository.findById(productId)).thenReturn(Optional.of(product1));

        Product actual = productService.getProductById(productId);

        assertEquals(expected, actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getCategoryId(), actual.getCategoryId());
        verify(productRepository).findById(productId);
    }

    @DisplayName("Get product by ID : negative case")
    @Test
    void getProductByIdNegativeCase() {
        Long productId = 999L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
        assertEquals("Product with id " + productId + " not found", runtimeException.getMessage());
    }

    @DisplayName("Create new product")
    @Test
    void createProduct() {
        Product expected = productCreated;

        when(productRepository.save(productToCreate)).thenReturn(productCreated);

        Product actual = productService.createProduct(productToCreate);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(productRepository).save(productToCreate);
    }

    @DisplayName("Delete product by ID : positive case")
    @Test
    void deleteProductByIdPositiveCase() {
        Product deletedProduct = product1;

        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.of(deletedProduct));

        productService.deleteProductById(productId);

        verify(productRepository).findById(productId);
        verify(productRepository).delete(deletedProduct);
    }

    @DisplayName("Delete product by ID : negative case")
    @Test
    void deleteProductByIdNegativeCase() {

        Long productId = 999L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException runtimeException = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
        assertEquals("Product with id " + productId + " not found", runtimeException.getMessage());
    }
}


