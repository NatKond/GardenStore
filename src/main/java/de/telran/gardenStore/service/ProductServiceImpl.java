package de.telran.gardenStore.service;

import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.NoDiscountedProductsException;
import de.telran.gardenStore.exception.ProductDeletionNotAllowedException;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    private final EntityManager entityManager;

    @Override
    public List<Product> getAll(Long categoryId, Boolean discount, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, Boolean sortDirection) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }

        if (discount != null) {
            predicates.add(discount ?
                    criteriaBuilder.isNotNull(root.get("discountPrice")) :
                    criteriaBuilder.isNull(root.get("discountPrice")));
        }

        if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (minPrice != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (sortBy != null && sortDirection != null) {
            if (sortDirection.equals(false)) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(sortBy)));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(sortBy)));
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
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
    }

    @Override
    public Product getProductOfTheDay() {
        Optional<Product> discountedProduct = productRepository.findProductsWithHighestDiscount();
        if (discountedProduct.isEmpty()) {
            throw new NoDiscountedProductsException("No discounted products available");
        }
        return discountedProduct.get();
    }

    @Override
    public Product create(Product product) {
        checkCategoryExists(product.getCategory().getCategoryId());
        logAttemptToSaveProduct(product);

        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product productToUpdate = getById(id);

        checkCategoryExists(product.getCategory().getCategoryId());

        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setDiscountPrice(product.getDiscountPrice());
        productToUpdate.setCategory(product.getCategory());
        productToUpdate.setImageUrl(product.getImageUrl());
        logAttemptToSaveProduct(productToUpdate);

        return productRepository.save(productToUpdate);
    }

    @Override
    public Product setDiscount(Long id, BigDecimal discountPercentage) {
        Product product = getById(id);
        BigDecimal discountAmount = product.getPrice()
                .multiply(discountPercentage)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        product.setDiscountPrice(product.getPrice().subtract(discountAmount));
        logAttemptToSaveProduct(product);

        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        if (productRepository.isInOrder(id)){
         throw new ProductDeletionNotAllowedException("Product cannot be deleted because it was added to order");
        }

        productRepository.delete(getById(id));
    }

    private void checkCategoryExists(Long categoryId) {
        categoryService.getById(categoryId);
    }

    private void logAttemptToSaveProduct(Product product) {
        log.debug("Attempt to save Product {}", product);
    }
}
