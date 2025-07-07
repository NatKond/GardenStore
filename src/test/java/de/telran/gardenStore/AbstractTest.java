package de.telran.gardenStore;

import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.AppUser;
import de.telran.gardenStore.entity.Category;
import de.telran.gardenStore.entity.Favorite;
import de.telran.gardenStore.entity.Product;
import de.telran.gardenStore.enums.Role;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class AbstractTest {

    protected Category category1;
    protected Category category2;
    protected Category category3;
    protected Category categoryToCreate;
    protected Category categoryCreated;

    protected Product product1;
    protected Product product2;
    protected Product product3;
    protected Product productToCreate;
    protected Product productCreated;

    protected AppUser user1;
    protected AppUser user2;
    protected AppUser userToCreate;
    protected AppUser userCreated;

    protected Favorite favorite1;
    protected Favorite favorite2;
    protected Favorite favoriteToCreate;
    protected Favorite favoriteCreated;

    protected CategoryResponseDto categoryResponseDto1;
    protected CategoryResponseDto categoryResponseDto2;
    protected CategoryResponseDto categoryResponseDto3;
    protected CategoryCreateRequestDto categoryCreateRequestDto;
    protected CategoryResponseDto categoryResponseCreatedDto;

    protected ProductResponseDto productResponseDto1;
    protected ProductResponseDto productResponseDto2;
    protected ProductCreateRequestDto productCreateRequestDto;
    protected ProductResponseDto productResponseCreatedDto;

    protected UserResponseDto userResponseDto1;
    protected UserResponseDto userResponseDto2;
    protected UserCreateRequestDto userCreateRequestDto;
    protected UserResponseDto userResponseCreatedDto;

    protected FavoriteResponseDto favoriteResponseDto1;
    protected FavoriteResponseDto favoriteResponseDto2;
    protected FavoriteCreateRequestDto favoriteCreateRequestDto;
    protected FavoriteResponseDto favoriteResponseCreatedDto;

    @BeforeEach
    void setUp() {
        initEntities();
        initCategoryDtos();
        initProductDtos();
        initUserDtos();
        initFavoriteDtos();
    }

    private void initEntities() {
        category1 = Category.builder()
                .categoryId(1L)
                .name("Fertilizer")
                .build();

        category2 = Category.builder()
                .categoryId(2L)
                .name("Protective products and septic tanks")
                .build();

        category3 = Category.builder()
                .categoryId(3L)
                .name("Planting material")
                .build();

        categoryToCreate = Category.builder()
                .name("Pots and planters")
                .build();

        categoryCreated = categoryToCreate.toBuilder()
                .categoryId(4L)
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

        product3 = Product.builder()
                .productId(3L)
                .name("Slug & Snail Barrier Pellets")
                .discountPrice(new BigDecimal("5.75"))
                .price(new BigDecimal("7.50"))
                .category(category2)
                .description("Pet-safe barrier pellets to protect plants from slugs")
                .imageUrl("https://example.com/images/protection_slug_pellets.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productToCreate = Product.builder()
                .name("Garden Tool Set (5 pcs)")
                .discountPrice(new BigDecimal("19.99"))
                .price(new BigDecimal("24.99"))
                .category(category3)
                .description("Essential hand tools set for everyday gardening")
                .imageUrl("https://example.com/images/garden_tool_set.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productCreated = productToCreate.toBuilder()
                .productId(4L)
                .build();

        user1 = AppUser.builder()
                .userId(1L)
                .name("Alice Johnson")
                .email("alice.johnson@example.com")
                .phoneNumber("+1234567890")
                .passwordHash("12345")
                .role(Role.ROLE_USER)
                .build();

        user2 = AppUser.builder()
                .userId(2L)
                .name("Bob Smith")
                .email("bob.smith@example.com")
                .phoneNumber("+1987654321")
                .passwordHash("12345")
                .role(Role.ROLE_USER)
                .build();

        userToCreate = AppUser.builder()
                .name("Charlie Brown")
                .email("charlie.brown@example.com")
                .phoneNumber("+1122334455")
                .passwordHash("12345")
                .role(Role.ROLE_USER)
                .build();

        userCreated = userToCreate.toBuilder()
                .userId(3L)
                .build();

        favorite1 = Favorite.builder()
                .favoriteId(1L)
                .user(user1)
                .product(product1)
                .build();

        favorite2 = Favorite.builder()
                .favoriteId(2L)
                .user(user1)
                .product(product2)
                .build();

        favoriteToCreate = Favorite.builder()
                .user(user1)
                .product(product3)
                .build();

        favoriteCreated = favoriteToCreate.toBuilder()
                .favoriteId(3L)
                .build();
    }

    private void initCategoryDtos() {
        categoryResponseDto1 = CategoryResponseDto.builder()
                .categoryId(category1.getCategoryId())
                .name(category1.getName())
                .build();

        categoryResponseDto2 = CategoryResponseDto.builder()
                .categoryId(category2.getCategoryId())
                .name(category2.getName())
                .build();

        categoryResponseDto3 = CategoryResponseDto.builder()
                .categoryId(category3.getCategoryId())
                .name(category3.getName())
                .build();

        categoryCreateRequestDto = CategoryCreateRequestDto.builder()
                .name(categoryToCreate.getName())
                .build();

        categoryResponseCreatedDto = CategoryResponseDto.builder()
                .categoryId(categoryCreated.getCategoryId())
                .name(categoryCreated.getName())
                .build();
    }

    private void initProductDtos() {
        productResponseDto1 = ProductResponseDto.builder()
                .productId(product1.getProductId())
                .name(product1.getName())
                .price(product1.getPrice())
                .discountPrice(product1.getDiscountPrice())
                .categoryId(product1.getCategory().getCategoryId())
                .description(product1.getDescription())
                .imageUrl(product1.getImageUrl())
                .build();

        productResponseDto2 = ProductResponseDto.builder()
                .productId(product2.getProductId())
                .name(product2.getName())
                .price(product2.getPrice())
                .discountPrice(product2.getDiscountPrice())
                .categoryId(product2.getCategory().getCategoryId())
                .description(product2.getDescription())
                .imageUrl(product2.getImageUrl())
                .build();

        productCreateRequestDto = ProductCreateRequestDto.builder()
                .name(productToCreate.getName())
                .price(productToCreate.getPrice())
                .discountPrice(productToCreate.getDiscountPrice())
                .categoryId(productToCreate.getCategory().getCategoryId())
                .description(productToCreate.getDescription())
                .imageUrl(productToCreate.getImageUrl())
                .build();

        productResponseCreatedDto = ProductResponseDto.builder()
                .productId(productCreated.getProductId())
                .name(productCreated.getName())
                .price(productCreated.getPrice())
                .discountPrice(productCreated.getDiscountPrice())
                .categoryId(productCreated.getCategory().getCategoryId())
                .description(productCreated.getDescription())
                .imageUrl(productCreated.getImageUrl())
                .build();
    }

    private void initUserDtos() {
        userResponseDto1 = UserResponseDto.builder()
                .userId(user1.getUserId())
                .name(user1.getName())
                .email(user1.getEmail())
                .phoneNumber(user1.getPhoneNumber())
                .role(user1.getRole().name())
                .build();

        userResponseDto2 = UserResponseDto.builder()
                .userId(user2.getUserId())
                .name(user2.getName())
                .email(user2.getEmail())
                .phoneNumber(user2.getPhoneNumber())
                .role(user2.getRole().name())
                .build();

        userCreateRequestDto = UserCreateRequestDto.builder()
                .name(userToCreate.getName())
                .email(userToCreate.getEmail())
                .phoneNumber(userToCreate.getPhoneNumber())
                .password(userToCreate.getPasswordHash())
                .build();

        userResponseCreatedDto = UserResponseDto.builder()
                .userId(userCreated.getUserId())
                .name(userCreated.getName())
                .email(userCreated.getEmail())
                .phoneNumber(userCreated.getPhoneNumber())
                .role(userCreated.getRole().name())
                .build();
    }

    private void initFavoriteDtos() {
        favoriteResponseDto1 = FavoriteResponseDto.builder()
                .favoriteId(favorite1.getFavoriteId())
                .productId(favorite1.getProduct().getProductId())
                .userId(favorite1.getUser().getUserId())
                .build();

        favoriteResponseDto2 = FavoriteResponseDto.builder()
                .favoriteId(favorite2.getFavoriteId())
                .productId(favorite2.getProduct().getProductId())
                .userId(favorite2.getUser().getUserId())
                .build();

        favoriteCreateRequestDto = FavoriteCreateRequestDto.builder()
                .userId(favoriteToCreate.getUser().getUserId())
                .productId(favoriteToCreate.getProduct().getProductId())
                .build();

        favoriteResponseCreatedDto = FavoriteResponseDto.builder()
                .favoriteId(favoriteCreated.getFavoriteId())
                .productId(favoriteCreated.getProduct().getProductId())
                .userId(favoriteCreated.getUser().getUserId())
                .build();
    }
}