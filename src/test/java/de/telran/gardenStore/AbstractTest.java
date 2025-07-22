package de.telran.gardenStore;

import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.enums.Role;
import de.telran.gardenStore.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

    protected Cart cart1;
    protected Cart cart2;

    protected CartItem cartItem1;
    protected CartItem cartItem2;
    protected CartItem cartItem3;

    protected Order order1;
    protected Order order2;

    protected OrderItem orderItem1;
    protected OrderItem orderItem2;
    protected OrderItem orderItem3;

    protected OrderShortResponseDto orderShortResponseDto1;
    protected OrderShortResponseDto orderShortResponseDto2;
    protected OrderResponseDto orderResponseDto1;
    protected OrderResponseDto orderResponseCreatedDto;
    protected OrderCreateRequestDto orderCreateRequestDto;

    protected CategoryShortResponseDto categoryShortResponseDto1;
    protected CategoryShortResponseDto categoryShortResponseDto2;
    protected CategoryShortResponseDto categoryShortResponseDto3;
    protected CategoryResponseDto categoryResponseDto1;
    protected CategoryCreateRequestDto categoryCreateRequestDto;
    protected CategoryResponseDto categoryResponseCreatedDto;

    protected ProductShortResponseDto productShortResponseDto1;
    protected ProductShortResponseDto productShortResponseDto2;
    protected ProductResponseDto productResponseDto1;
    protected ProductCreateRequestDto productCreateRequestDto;
    protected ProductResponseDto productResponseCreatedDto;
    protected ProductShortResponseDto productShortResponseCreatedDto;

    protected UserShortResponseDto userShortResponseDto1;
    protected UserShortResponseDto userShortResponseDto2;
    protected UserResponseDto userResponseDto1;
    protected UserCreateRequestDto userCreateRequestDto;
    protected UserResponseDto userResponseCreatedDto;

    protected FavoriteResponseDto favoriteResponseDto1;
    protected FavoriteResponseDto favoriteResponseDto2;
    protected FavoriteResponseDto favoriteResponseCreatedDto;

    @Mock
    protected OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
        initEntities();
        initProductDtos();
        initCategoryDtos();
        initFavoriteDtos();
        initUserDtos();
        initOrderDtos();
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
                .name("Tools and equipment")
                .build();

        categoryToCreate = Category.builder()
                .name("Planting material")
                .products(new ArrayList<>())
                .build();

        categoryCreated = categoryToCreate.toBuilder()
                .categoryId(3L)
                .build();

        product1 = Product.builder()
                .productId(1L)
                .name("All-Purpose Plant Fertilizer")
                .discountPrice(BigDecimal.valueOf(8.99))
                .price(BigDecimal.valueOf(11.99))
                .category(category1)
                .description("Balanced NPK formula for all types of plants")
                .imageUrl("https://example.com/images/fertilizer_all_purpose.jpg")
                .createdAt(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
                .build();

        product2 = Product.builder()
                .productId(1L)
                .name("Organic Tomato Feed")
                .discountPrice(BigDecimal.valueOf(10.49))
                .price(BigDecimal.valueOf(13.99))
                .category(category1)
                .description("Organic liquid fertilizer ideal for tomatoes and vegetables")
                .imageUrl("https://example.com/images/fertilizer_tomato_feed.jpg")
                .createdAt(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
                .build();

        category1.setProducts(List.of(product1, product2));

        product3 = Product.builder()
                .productId(3L)
                .name("Slug & Snail Barrier Pellets")
                .discountPrice(BigDecimal.valueOf(5.75))
                .price(BigDecimal.valueOf(7.50))
                .category(category2)
                .description("Pet-safe barrier pellets to protect plants from slugs")
                .imageUrl("https://example.com/images/protection_slug_pellets.jpg")
                .createdAt(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
                .build();

        category2.setProducts(List.of(product3));

        productToCreate = Product.builder()
                .name("Garden Tool Set (5 pcs)")
                .discountPrice(BigDecimal.valueOf(19.99))
                .price(BigDecimal.valueOf(24.99))
                .category(category3)
                .description("Essential hand tools set for everyday gardening")
                .imageUrl("https://example.com/images/garden_tool_set.jpg")
                .createdAt(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 1, 0, 0, 0))
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
                .favorites(new ArrayList<>())
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

        user1.setFavorites(List.of(favorite1, favorite2));

        favoriteToCreate = Favorite.builder()
                .user(user1)
                .product(product3)
                .build();

        favoriteCreated = favoriteToCreate.toBuilder()
                .favoriteId(3L)
                .build();

        cart1 = Cart.builder()
                .user(user1)
                .build();

        cart2 = Cart.builder()
                .user(user2)
                .build();

        cartItem1 = CartItem.builder()
                .cart(cart1)
                .product(product1)
                .quantity(2)
                .build();

        cartItem2 = CartItem.builder()
                .cart(cart1)
                .product(product2)
                .quantity(1)
                .build();

        cart1.setItems(new ArrayList<>(List.of(cartItem1, cartItem2)));

        cartItem3 = CartItem.builder()
                .cart(cart2)
                .product(product1)
                .quantity(1)
                .build();

        cart2.setItems(new ArrayList<>(List.of(cartItem3)));

        order1 = Order.builder()
                .orderId(1L)
                .user(user1)
                .deliveryAddress("123 Garden Street")
                .contactPhone(user1.getPhoneNumber())
                .deliveryMethod(DeliveryMethod.COURIER)
                .status(OrderStatus.AWAITING_PAYMENT)
                .createdAt(LocalDateTime.of(2025, 7, 1, 10, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 1, 10, 30, 0))
                .build();

        order2 = Order.builder()
                .orderId(2L)
                .user(user2)
                .deliveryAddress("456 Green Ave")
                .contactPhone(user2.getPhoneNumber())
                .deliveryMethod(DeliveryMethod.PICKUP)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.of(2025, 7, 2, 12, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 2, 12, 5, 0))
                .build();

        orderItem1 = OrderItem.builder()
                .order(order1)
                .product(product1)
                .quantity(2)
                .priceAtPurchase(product1.getDiscountPrice())
                .build();

        orderItem2 = OrderItem.builder()
                .order(order1)
                .product(product2)
                .quantity(1)
                .priceAtPurchase(product2.getDiscountPrice())
                .build();

        order1.setItems(new ArrayList<>(List.of(orderItem1, orderItem2)));

        orderItem3 = OrderItem.builder()
                .order(order2)
                .product(product3)
                .quantity(1)
                .priceAtPurchase(product3.getDiscountPrice())
                .build();

        order2.setItems(new ArrayList<>(List.of(orderItem3)));
    }

    private void initCategoryDtos() {
        categoryShortResponseDto1 = CategoryShortResponseDto.builder()
                .categoryId(category1.getCategoryId())
                .name(category1.getName())
                .build();

        categoryShortResponseDto2 = CategoryShortResponseDto.builder()
                .categoryId(category2.getCategoryId())
                .name(category2.getName())
                .build();

        categoryShortResponseDto3 = CategoryShortResponseDto.builder()
                .categoryId(category3.getCategoryId())
                .name(category3.getName())
                .build();

        categoryResponseDto1 = CategoryResponseDto.builder()
                .categoryId(category1.getCategoryId())
                .name(category1.getName())
                .products(List.of(productShortResponseDto1, productShortResponseDto2))
                .build();

        categoryCreateRequestDto = CategoryCreateRequestDto.builder()
                .name(categoryToCreate.getName())
                .build();

        categoryResponseCreatedDto = CategoryResponseDto.builder()
                .categoryId(categoryCreated.getCategoryId())
                .name(categoryCreated.getName())
                .build();
    }

    private void initOrderDtos() {
        orderShortResponseDto1 = OrderShortResponseDto.builder()
                .orderId(order1.getOrderId())
                .status(order1.getStatus().name())
                .deliveryAddress(order1.getDeliveryAddress())
                .contactPhone(order1.getContactPhone())
                .deliveryMethod(order1.getDeliveryMethod().name())
                .build();

        orderShortResponseDto2 = OrderShortResponseDto.builder()
                .orderId(order2.getOrderId())
                .status(order2.getStatus().name())
                .deliveryAddress(order2.getDeliveryAddress())
                .contactPhone(order2.getContactPhone())
                .deliveryMethod(order2.getDeliveryMethod().name())
                .build();


        orderResponseDto1 = OrderResponseDto.builder()
                .orderId(order1.getOrderId())
                .userId(order1.getUser().getUserId())
                .status(order1.getStatus().name())
                .deliveryAddress(order1.getDeliveryAddress())
                .contactPhone(order1.getContactPhone())
                .deliveryMethod(order1.getDeliveryMethod().name())
                .createdAt(order1.getCreatedAt())
                .updatedAt(order1.getUpdatedAt())
                .items(List.of(
                        OrderItemResponseDto.builder()
                                .orderItemId(orderItem1.getOrderItemId())
                                .product(productShortResponseDto1)
                                .quantity(orderItem1.getQuantity())
                                .priceAtPurchase(orderItem1.getPriceAtPurchase())
                                .build(),
                        OrderItemResponseDto.builder()
                                .orderItemId(orderItem2.getOrderItemId())
                                .product(productShortResponseDto2)
                                .quantity(orderItem2.getQuantity())
                                .priceAtPurchase(orderItem2.getPriceAtPurchase())
                                .build()
                ))
                .totalAmount(orderService.getTotalAmount(order1.getOrderId()))
                .build();


        orderCreateRequestDto = OrderCreateRequestDto.builder()
                .deliveryAddress("123 Garden Street")
                .contactPhone("+1234567890")
                .deliveryMethod(DeliveryMethod.valueOf("COURIER"))
                .items(List.of(
                        OrderItemCreateRequestDto.builder()
                                .productId(product1.getProductId())
                                .quantity(2)
                                .build(),
                        OrderItemCreateRequestDto.builder()
                                .productId(product2.getProductId())
                                .quantity(1)
                                .build()
                ))
                .build();

        orderResponseCreatedDto = OrderResponseDto.builder()
                .orderId(3L)
                .userId(user1.getUserId())
                .status(OrderStatus.CREATED.name())
                .deliveryAddress("123 Garden Street")
                .contactPhone("+1234567890")
                .deliveryMethod("COURIER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(List.of(
                        OrderItemResponseDto.builder()
                                .product(productShortResponseDto1)
                                .quantity(2)
                                .priceAtPurchase(product1.getPrice())
                                .build(),
                        OrderItemResponseDto.builder()
                                .product(productShortResponseDto2)
                                .quantity(1)
                                .priceAtPurchase(product2.getPrice())
                                .build()
                ))
                .totalAmount(new BigDecimal("28.47")) // Пример суммы
                .build();
        when(orderService.getTotalAmount(anyLong())).thenReturn(new BigDecimal("28.47"));

        orderShortResponseDto1 = OrderShortResponseDto.builder()
                .orderId(order1.getOrderId())
                .status(order1.getStatus().name())
                .deliveryAddress(order1.getDeliveryAddress())
                .contactPhone(order1.getContactPhone())
                .deliveryMethod(order1.getDeliveryMethod().name())
                .build();
    }

    private void initProductDtos() {
        productShortResponseDto1 = ProductShortResponseDto.builder()
                .productId(product1.getProductId())
                .name(product1.getName())
                .price(product1.getPrice())
                .discountPrice(product1.getDiscountPrice())
                .categoryId(product1.getCategory().getCategoryId())
                .description(product1.getDescription())
                .build();

        productShortResponseDto2 = ProductShortResponseDto.builder()
                .productId(product2.getProductId())
                .name(product2.getName())
                .price(product2.getPrice())
                .discountPrice(product2.getDiscountPrice())
                .categoryId(product2.getCategory().getCategoryId())
                .description(product2.getDescription())
                .build();

        productResponseDto1 = ProductResponseDto.builder()
                .productId(product1.getProductId())
                .name(product1.getName())
                .price(product1.getPrice())
                .discountPrice(product1.getDiscountPrice())
                .categoryId(product1.getCategory().getCategoryId())
                .description(product1.getDescription())
                .imageUrl(product1.getImageUrl())
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

        productShortResponseCreatedDto = ProductShortResponseDto.builder()
                .productId(productCreated.getProductId())
                .name(productCreated.getName())
                .price(productCreated.getPrice())
                .discountPrice(productCreated.getDiscountPrice())
                .categoryId(productCreated.getCategory().getCategoryId())
                .description(productCreated.getDescription())
                .build();
    }

    private void initUserDtos() {
        userShortResponseDto1 = UserShortResponseDto.builder()
                .userId(user1.getUserId())
                .name(user1.getName())
                .email(user1.getEmail())
                .phoneNumber(user1.getPhoneNumber())
                .role(user1.getRole().name())
                .build();

        userShortResponseDto2 = UserShortResponseDto.builder()
                .userId(user2.getUserId())
                .name(user2.getName())
                .email(user2.getEmail())
                .phoneNumber(user2.getPhoneNumber())
                .role(user2.getRole().name())
                .build();

        userResponseDto1 = UserResponseDto.builder()
                .userId(user1.getUserId())
                .name(user1.getName())
                .email(user1.getEmail())
                .phoneNumber(user1.getPhoneNumber())
                .favorites(List.of(favoriteResponseDto1, favoriteResponseDto2))
                .role(user1.getRole().name())
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
                .favorites(new ArrayList<>())
                .build();
    }

    private void initFavoriteDtos() {
        favoriteResponseDto1 = FavoriteResponseDto.builder()
                .favoriteId(favorite1.getFavoriteId())
                .product(productShortResponseDto1)
                .build();

        favoriteResponseDto2 = FavoriteResponseDto.builder()
                .favoriteId(favorite2.getFavoriteId())
                .product(productShortResponseDto2)
                .build();

        favoriteResponseCreatedDto = FavoriteResponseDto.builder()
                .favoriteId(favoriteCreated.getFavoriteId())
                .product(productShortResponseCreatedDto)
                .build();
    }
}