package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Page<Product> getAllProducts(Long category, Boolean discount,
                                        BigDecimal minPrice, BigDecimal maxPrice,
                                        String[] sort, int page, int size) {
        // Создаем список для хранения всех спецификаций
        List<Specification<Product>> specifications = new ArrayList<>();

        // Фильтрация по категории
        if (category != null) {
            specifications.add((root, query, cb) ->
                    cb.equal(root.get("categoryId"), category));
        }

        // Фильтрация по наличию скидки
        if (discount != null) {
            specifications.add(discount ?
                    (root, query, cb) -> cb.isNotNull(root.get("discountPrice")) :
                    (root, query, cb) -> cb.isNull(root.get("discountPrice")));
        }

        // Фильтрация по минимальной цене
        if (minPrice != null) {
            specifications.add((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        // Фильтрация по максимальной цене
        if (maxPrice != null) {
            specifications.add((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        // Комбинируем все спецификации через AND
        Specification<Product> combinedSpec = specifications.stream()
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());

        // Парсинг параметров сортировки
        Sort sorting = parseSortParameter(sort);

        Pageable pageable = PageRequest.of(page, size, sorting);
        return productRepository.findAll(combinedSpec, pageable);
    }
    private Sort parseSortParameter(String[] sortParams) {
        if (sortParams == null || sortParams.length == 0) {
            return Sort.by(Sort.Direction.ASC, "productId");
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            String[] parts = param.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid sort parameter format. Expected 'field,direction'");
            }
            orders.add(new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]));
        }

        return Sort.by(orders);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + productId + " not found"));
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setDiscountPrice(updatedProduct.getDiscountPrice());
        existing.setCategoryId(updatedProduct.getCategoryId());
        existing.setImageUrl(updatedProduct.getImageUrl());

        return productRepository.save(existing);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.delete(getProductById(id));
    }
}
