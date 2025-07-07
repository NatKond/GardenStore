package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Product> criteriaQuery;
    @Mock
    private Root<Product> root;
    @Mock
    private TypedQuery<Product> typedQuery;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category category1;
    private Category category2;

    private Product product1;
    private Product product2;
    private Product productToCreate;
    private Product productCreated;

    @BeforeEach
     void setUp() {

        category1 = Category.builder()
                .categoryId(1L)
                .name("Fertilizer")
                .build();

        category2 = Category.builder()
                .categoryId(2L)
                .name("Protective products and septic tanks")
                .build();

        product1 = Product.builder()
                .productId(1L)
                .name("All-Purpose Plant Fertilizer")
                .discountPrice(new BigDecimal("8.99"))
                .price(new BigDecimal("11.99"))
                .category(category1)
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
                .category(category1)
                .description("Organic liquid fertilizer ideal for tomatoes and vegetables")
                .imageUrl("https://example.com/images/fertilizer_tomato_feed.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productToCreate = Product.builder()
                .name("Slug & Snail Barrier Pellets")
                .discountPrice(new BigDecimal("5.75"))
                .price(new BigDecimal("7.50"))
                .category(category2)
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

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Product.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Product.class)).thenReturn(root);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expected);
        //when(productRepository.findAll()).thenReturn(expected);

        List<Product> actual = productService.getAllProducts(null, null, null, null, null, null);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
        //verify(productRepository).findAll();
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
        assertEquals(expected.getCategory(), actual.getCategory());
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


