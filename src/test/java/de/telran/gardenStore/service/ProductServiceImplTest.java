package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void getAllProducts_shouldReturnProducts() {
        Product p1 = Product.builder().productId(1L).name("Apple").build();
        Product p2 = Product.builder().productId(2L).name("Banana").build();
        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Product> result = productService.getAllProducts();

        assertThat(result).hasSize(2).containsExactly(p1, p2);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_shouldReturnProduct_whenExists() {
        Product p = Product.builder().productId(1L).name("Test").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        Product result = productService.getProductById(1L);

        assertThat(result).isEqualTo(p);
        verify(productRepository).findById(1L);
    }

    @Test
    void getProductById_shouldThrow_whenNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void createProduct_shouldSaveProduct() {
        Product p = Product.builder().name("New Product").build();
        Product saved = Product.builder().productId(1L).name("New Product").build();
        when(productRepository.save(p)).thenReturn(saved);

        Product result = productService.createProduct(p);

        assertThat(result).isEqualTo(saved);
        verify(productRepository).save(p);
    }

    @Test
    void deleteProductById_shouldDeleteProduct_whenExists() {
        Product p = Product.builder().productId(1L).build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        productService.deleteProductById(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).deleteById(1L);
    }
}

