package de.telran.gardenStore.service;

import de.telran.gardenStore.dto.ProductFilter;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    private final EntityManager entityManager;

    @Override
    public List<Product> getAllProducts(ProductFilter productFilter) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (productFilter.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(productFilter.getCategoryId());
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }

        if (productFilter.getDiscount() != null) {
            predicates.add(productFilter.getDiscount() ?
                    criteriaBuilder.isNotNull(root.get("discountPrice")) :
                    criteriaBuilder.isNull(root.get("discountPrice")));
        }

        if (productFilter.getMaxPrice() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), productFilter.getMaxPrice()));
        }

        if (productFilter.getMinPrice() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), productFilter.getMinPrice()));
        }

        if (productFilter.getSortBy() != null && productFilter.getSortDirection() != null) {
            if (productFilter.getSortDirection().equals(false)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(productFilter.getSortBy())));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(productFilter.getSortBy())));
            }
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("productId")));
        }

        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(criteriaQuery.select(root)).getResultList();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));
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
        existing.setCategory(updatedProduct.getCategory());
        existing.setImageUrl(updatedProduct.getImageUrl());

        return productRepository.save(existing);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.delete(getProductById(id));
    }
}
