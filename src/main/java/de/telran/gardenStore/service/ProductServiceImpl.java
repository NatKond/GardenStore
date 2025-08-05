package de.telran.gardenStore.service;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.exception.NoDiscountedProductsException;
import de.telran.gardenStore.exception.ProductNotFoundException;
import de.telran.gardenStore.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
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
    public Product getById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found"));
    }

    @Override
    public Product create(Product product) {
        checkCategoryExists(product.getCategory().getCategoryId());

        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product existing = getById(id);

        checkCategoryExists(product.getCategory().getCategoryId());

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setDiscountPrice(product.getDiscountPrice());
        existing.setCategory(product.getCategory());
        existing.setImageUrl(product.getImageUrl());

        return productRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.delete(getById(id));
    }

    @Override
    public Product setDiscount(Long productId, BigDecimal discountPercentage) {
        Product product = getById(productId);
        BigDecimal discountAmount = product.getPrice()
                .multiply(discountPercentage)
                .divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
        product.setDiscountPrice(product.getPrice().subtract(discountAmount));
        return productRepository.save(product);
    }

    @Override
    public Product getProductOfTheDay() {
        List<Product> discountedProducts = productRepository.findProductsWithHighestDiscount();
        if (discountedProducts.isEmpty()) {
            throw new NoDiscountedProductsException("No discounted products available");
        }
        return discountedProducts.get(new Random().nextInt(discountedProducts.size()));
    }

    private void checkCategoryExists(Long categoryId) {
        categoryService.getById(categoryId);
    }
}
