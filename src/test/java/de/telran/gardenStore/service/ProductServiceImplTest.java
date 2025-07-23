package de.telran.gardenStore.service;

import de.telran.gardenStore.AbstractTest;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest extends AbstractTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryServiceImpl categoryService;

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
        when(categoryService.getCategoryById(productCreated.getCategory().getCategoryId())).thenReturn(productCreated.getCategory());

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


