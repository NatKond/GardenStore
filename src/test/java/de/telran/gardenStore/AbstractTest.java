package de.telran.gardenStore;

import de.telran.gardenStore.dto.*;
import de.telran.gardenStore.entity.*;
import de.telran.gardenStore.enums.DeliveryMethod;
import de.telran.gardenStore.enums.OrderStatus;
import de.telran.gardenStore.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractTest {

    protected Authentication authentication;
    protected SecurityContext context;

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
    protected Order order3;
    protected Order order4;
    protected Order orderToCreate;

    protected OrderItem orderItem1;
    protected OrderItem orderItem2;
    protected OrderItem orderItem3;
    protected OrderItem orderItem4;
    protected OrderItem orderItem5;
    protected OrderItem orderItemToCreate1;
    protected OrderItem orderItemToCreate2;

    protected CategoryShortResponseDto categoryShortResponseDto1;
    protected CategoryShortResponseDto categoryShortResponseDto2;
    protected CategoryShortResponseDto categoryShortResponseDto3;
    protected CategoryResponseDto categoryResponseDto1;
    protected CategoryCreateRequestDto categoryCreateRequestDto;
    protected CategoryResponseDto categoryResponseCreatedDto;

    protected ProductShortResponseDto productShortResponseDto1;
    protected ProductShortResponseDto productShortResponseDto2;
    protected ProductShortResponseDto productShortResponseDto3;
    protected ProductResponseDto productResponseDto1;
    protected ProductResponseDto productResponseDto2;
    protected ProductResponseDto productResponseDto3;
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

    protected CartResponseDto cartResponseDto1;
    protected CartResponseDto cartResponseDto2;
    protected CartItemResponseDto cartItemResponseDto1;
    protected CartItemResponseDto cartItemResponseDto2;
    protected CartItemResponseDto cartItemResponseDto3;

    protected OrderShortResponseDto orderShortResponseDto1;
    protected OrderShortResponseDto orderShortResponseDto2;
    protected OrderShortResponseDto orderShortResponseDto3;
    protected OrderShortResponseDto orderShortResponseDto4;
    protected OrderResponseDto orderResponseDto1;
    protected OrderResponseDto orderResponseDto2;
    protected OrderResponseDto orderResponseDto3;
    protected OrderResponseDto orderResponseCreatedDto;
    protected OrderCreateRequestDto orderCreateRequestDto;
    protected OrderItemResponseDto orderItemResponseDto1;
    protected OrderItemResponseDto orderItemResponseDto2;
    protected OrderItemResponseDto orderItemResponseDto3;
    protected OrderItemResponseDto orderItemResponseDto4;
    protected OrderItemCreateRequestDto orderItemCreateRequestDto1;
    protected OrderItemCreateRequestDto orderItemCreateRequestDto2;
    protected OrderItemResponseDto orderItemResponseDtoCreated1;
    protected OrderItemResponseDto orderItemResponseDtoCreated2;

    @BeforeEach
    protected void setUp() {
        initEntities();
        initSecurityContext();
        initProductDtos();
        initCategoryDtos();
        initFavoriteDtos();
        initUserDtos();
        initCartDtos();
        initOrderDtos();
    }

    private void initSecurityContext() {
        authentication = new UsernamePasswordAuthenticationToken(user1.getEmail(), null);
        context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
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
                .productId(2L)
                .name("Organic Tomato Feed")
                .discountPrice(BigDecimal.valueOf(9.49))
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
                .roles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN))
                .build();

        user2 = AppUser.builder()
                .userId(2L)
                .name("Bob Smith")
                .email("bob.smith@example.com")
                .phoneNumber("+1987654321")
                .passwordHash("12345")
                .favorites(new ArrayList<>())
                .roles(Set.of(Role.ROLE_USER))
                .build();

        userToCreate = AppUser.builder()
                .name("Charlie Brown")
                .email("charlie.brown@example.com")
                .phoneNumber("+1122334455")
                .passwordHash("12345")
                .roles(Set.of(Role.ROLE_USER))
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
                .cartId(1L)
                .user(user1)
                .build();

        user1.setCart(cart1);

        cartItem1 = CartItem.builder()
                .cartItemId(1L)
                .cart(cart1)
                .product(product1)
                .quantity(2)
                .build();

        cartItem2 = CartItem.builder()
                .cartItemId(2L)
                .cart(cart1)
                .product(product2)
                .quantity(1)
                .build();

        cart1.setItems(new ArrayList<>(List.of(cartItem1, cartItem2)));

        cart2 = Cart.builder()
                .cartId(2L)
                .user(user2)
                .build();

        user2.setCart(cart2);

        cartItem3 = CartItem.builder()
                .cartItemId(3L)
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

        orderItem1 = OrderItem.builder()
                .orderItemId(1L)
                .order(order1)
                .product(product1)
                .quantity(2)
                .priceAtPurchase(product1.getDiscountPrice())
                .build();

        orderItem2 = OrderItem.builder()
                .orderItemId(3L)
                .order(order1)
                .product(product3)
                .quantity(1)
                .priceAtPurchase(product3.getDiscountPrice())
                .build();

        order1.setItems(new ArrayList<>(List.of(orderItem1, orderItem2)));
        order1.setTotalAmount(orderItem1.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem1.getQuantity()))
                .add(orderItem2.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem2.getQuantity()))));

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

        orderItem3 = OrderItem.builder()
                .orderItemId(3L)
                .order(order2)
                .product(product3)
                .quantity(1)
                .priceAtPurchase(product3.getDiscountPrice())
                .build();

        order2.setItems(new ArrayList<>(List.of(orderItem3)));
        order2.setTotalAmount(orderItem3.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem3.getQuantity())));

        order3 = Order.builder()
                .orderId(3L)
                .user(user1)
                .deliveryAddress("123 Garden Street")
                .contactPhone(user1.getPhoneNumber())
                .deliveryMethod(DeliveryMethod.COURIER)
                .status(OrderStatus.DELIVERED)
                .createdAt(LocalDateTime.of(2025, 5, 3, 17, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 5, 5, 17, 0, 0))
                .build();

        orderItem4 = OrderItem.builder()
                .orderItemId(4L)
                .order(order3)
                .product(product3)
                .quantity(2)
                .priceAtPurchase(product3.getDiscountPrice())
                .build();

        order3.setItems(new ArrayList<>(List.of(orderItem4)));
        order3.setTotalAmount(orderItem4.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem4.getQuantity())));

        order4 = Order.builder()
                .orderId(4L)
                .user(user2)
                .deliveryAddress("456 Green Ave")
                .contactPhone(user2.getPhoneNumber())
                .deliveryMethod(DeliveryMethod.PICKUP)
                .status(OrderStatus.CANCELLED)
                .createdAt(LocalDateTime.of(2025, 7, 1, 11, 45, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 2, 2, 9, 10))
                .build();

        orderItem5 = OrderItem.builder()
                .orderItemId(5L)
                .order(order4)
                .product(product1)
                .quantity(1)
                .priceAtPurchase(product1.getDiscountPrice())
                .build();

        order4.setItems(new ArrayList<>(List.of(orderItem5)));
        order4.setTotalAmount(orderItem5.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem5.getQuantity())));

        orderToCreate = Order.builder()
                .user(user1)
                .deliveryAddress("123 Garden Street")
                .contactPhone(user1.getPhoneNumber())
                .deliveryMethod(DeliveryMethod.COURIER)
                .status(OrderStatus.CREATED)
                .build();

        orderItemToCreate1 = OrderItem.builder()
                .product(cartItem1.getProduct())
                .quantity(cartItem1.getQuantity())
                .priceAtPurchase(cartItem1.getProduct().getDiscountPrice())
                .build();

        orderItemToCreate2 = OrderItem.builder()
                .product(cartItem2.getProduct())
                .quantity(cartItem2.getQuantity())
                .priceAtPurchase(cartItem2.getProduct().getDiscountPrice())
                .build();

        orderToCreate.setItems(new ArrayList<>(List.of(orderItemToCreate1, orderItemToCreate2)));
        orderToCreate.setTotalAmount(orderItemToCreate1.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItemToCreate1.getQuantity()))
                .add(orderItemToCreate2.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItemToCreate2.getQuantity()))));

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
                .totalAmount(order1.getTotalAmount())
                .deliveryMethod(order1.getDeliveryMethod().name())
                .build();

        orderShortResponseDto2 = OrderShortResponseDto.builder()
                .orderId(order2.getOrderId())
                .status(order2.getStatus().name())
                .deliveryAddress(order2.getDeliveryAddress())
                .contactPhone(order2.getContactPhone())
                .totalAmount(order2.getTotalAmount())
                .deliveryMethod(order2.getDeliveryMethod().name())
                .build();

        orderShortResponseDto3 = OrderShortResponseDto.builder()
                .orderId(order3.getOrderId())
                .status(order3.getStatus().name())
                .deliveryAddress(order3.getDeliveryAddress())
                .contactPhone(order3.getContactPhone())
                .totalAmount(order3.getTotalAmount())
                .deliveryMethod(order3.getDeliveryMethod().name())
                .build();

        orderShortResponseDto4 = OrderShortResponseDto.builder()
                .orderId(order4.getOrderId())
                .status(order4.getStatus().name())
                .deliveryAddress(order4.getDeliveryAddress())
                .contactPhone(order4.getContactPhone())
                .totalAmount(order4.getTotalAmount())
                .deliveryMethod(order4.getDeliveryMethod().name())
                .build();

        orderResponseDto1 = OrderResponseDto.builder()
                .orderId(order1.getOrderId())
                .userId(order1.getUser().getUserId())
                .status(order1.getStatus().name())
                .deliveryAddress(order1.getDeliveryAddress())
                .contactPhone(order1.getContactPhone())
                .deliveryMethod(order1.getDeliveryMethod().name())
                .totalAmount(order1.getTotalAmount())
                .createdAt(order1.getCreatedAt())
                .updatedAt(order1.getUpdatedAt())
                .build();

        orderItemResponseDto1 = OrderItemResponseDto.builder()
                .orderItemId(orderItem1.getOrderItemId())
                .product(productShortResponseDto1)
                .quantity(orderItem1.getQuantity())
                .priceAtPurchase(orderItem1.getPriceAtPurchase())
                .build();

        orderItemResponseDto2 = OrderItemResponseDto.builder()
                .orderItemId(orderItem2.getOrderItemId())
                .product(productShortResponseDto3)
                .quantity(orderItem2.getQuantity())
                .priceAtPurchase(orderItem2.getPriceAtPurchase())
                .build();

        orderResponseDto1.setItems(List.of(orderItemResponseDto1, orderItemResponseDto2));

        orderResponseDto2 = OrderResponseDto.builder()
                .orderId(order2.getOrderId())
                .userId(order2.getUser().getUserId())
                .status(order2.getStatus().name())
                .deliveryAddress(order2.getDeliveryAddress())
                .contactPhone(order2.getContactPhone())
                .deliveryMethod(order2.getDeliveryMethod().name())
                .totalAmount(order2.getTotalAmount())
                .createdAt(order2.getCreatedAt())
                .updatedAt(order2.getUpdatedAt())
                .build();

        orderItemResponseDto3 = OrderItemResponseDto.builder()
                .orderItemId(orderItem3.getOrderItemId())
                .product(productShortResponseDto3)
                .quantity(orderItem3.getQuantity())
                .priceAtPurchase(orderItem3.getPriceAtPurchase())
                .build();

        orderResponseDto2.setItems(List.of(orderItemResponseDto3));

        orderResponseDto3 = OrderResponseDto.builder()
                .orderId(order3.getOrderId())
                .userId(order3.getUser().getUserId())
                .status(order3.getStatus().name())
                .deliveryAddress(order3.getDeliveryAddress())
                .contactPhone(order3.getContactPhone())
                .deliveryMethod(order3.getDeliveryMethod().name())
                .totalAmount(order3.getTotalAmount())
                .createdAt(order3.getCreatedAt())
                .updatedAt(order3.getUpdatedAt())
                .build();

        orderItemResponseDto4 = OrderItemResponseDto.builder()
                .orderItemId(orderItem4.getOrderItemId())
                .product(productShortResponseDto3)
                .quantity(orderItem4.getQuantity())
                .priceAtPurchase(orderItem4.getPriceAtPurchase())
                .build();

        orderResponseDto3.setItems(List.of(orderItemResponseDto4));

        orderCreateRequestDto = OrderCreateRequestDto.builder()
                .deliveryAddress(orderToCreate.getDeliveryAddress())
                .contactPhone(orderToCreate.getContactPhone())
                .deliveryMethod(orderToCreate.getDeliveryMethod())
                .build();

        orderItemCreateRequestDto1 = OrderItemCreateRequestDto.builder()
                .productId(orderItemToCreate1.getProduct().getProductId())
                .quantity(orderItemToCreate1.getQuantity())
                .build();

        orderItemCreateRequestDto2 = OrderItemCreateRequestDto.builder()
                .productId(orderItemToCreate2.getProduct().getProductId())
                .quantity(orderItemToCreate2.getQuantity())
                .build();

        orderCreateRequestDto.setItems(List.of(orderItemCreateRequestDto1, orderItemCreateRequestDto2));

        orderResponseCreatedDto = OrderResponseDto.builder()
                .orderId(3L)
                .userId(user1.getUserId())
                .status(OrderStatus.CREATED.name())
                .deliveryAddress(orderToCreate.getDeliveryAddress())
                .contactPhone(orderToCreate.getContactPhone())
                .deliveryMethod(orderToCreate.getDeliveryMethod().name())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .totalAmount(orderToCreate.getTotalAmount())
                .build();

        orderItemResponseDtoCreated1 = OrderItemResponseDto.builder()
                .product(productShortResponseDto1)
                .quantity(orderItemCreateRequestDto1.getQuantity())
                .priceAtPurchase(orderItemToCreate1.getPriceAtPurchase())
                .build();

        orderItemResponseDtoCreated2 = OrderItemResponseDto.builder()
                .product(productShortResponseDto2)
                .quantity(orderItemCreateRequestDto2.getQuantity())
                .priceAtPurchase(orderItemToCreate2.getPriceAtPurchase())
                .build();

        orderResponseCreatedDto.setItems(List.of(orderItemResponseDtoCreated1, orderItemResponseDtoCreated2));

    }

    private void initProductDtos() {
        productShortResponseDto1 = ProductShortResponseDto.builder()
                .productId(product1.getProductId())
                .name(product1.getName())
                .price(product1.getPrice())
                .discountPrice(product1.getDiscountPrice())
                .categoryId(product1.getCategory().getCategoryId())
                .description(product1.getDescription())
                .imageUrl(product1.getImageUrl())
                .build();

        productShortResponseDto2 = ProductShortResponseDto.builder()
                .productId(product2.getProductId())
                .name(product2.getName())
                .price(product2.getPrice())
                .discountPrice(product2.getDiscountPrice())
                .categoryId(product2.getCategory().getCategoryId())
                .description(product2.getDescription())
                .imageUrl(product2.getImageUrl())
                .build();

        productShortResponseDto3 = ProductShortResponseDto.builder()
                .productId(product3.getProductId())
                .name(product3.getName())
                .price(product3.getPrice())
                .discountPrice(product3.getDiscountPrice())
                .categoryId(product3.getCategory().getCategoryId())
                .description(product3.getDescription())
                .imageUrl(product3.getImageUrl())
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

        productResponseDto2 = ProductResponseDto.builder()
                .productId(product2.getProductId())
                .name(product2.getName())
                .price(product2.getPrice())
                .discountPrice(product2.getDiscountPrice())
                .categoryId(product2.getCategory().getCategoryId())
                .description(product2.getDescription())
                .imageUrl(product2.getImageUrl())
                .build();

        productResponseDto3 = ProductResponseDto.builder()
                .productId(product3.getProductId())
                .name(product3.getName())
                .price(product3.getPrice())
                .discountPrice(product3.getDiscountPrice())
                .categoryId(product3.getCategory().getCategoryId())
                .description(product3.getDescription())
                .imageUrl(product3.getImageUrl())
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
                .roles(user1.getRoles().stream().map(Enum::name).toList())
                .build();

        userShortResponseDto2 = UserShortResponseDto.builder()
                .userId(user2.getUserId())
                .name(user2.getName())
                .email(user2.getEmail())
                .phoneNumber(user2.getPhoneNumber())
                .roles(user2.getRoles().stream().map(Enum::name).toList())
                .build();

        userResponseDto1 = UserResponseDto.builder()
                .userId(user1.getUserId())
                .name(user1.getName())
                .email(user1.getEmail())
                .phoneNumber(user1.getPhoneNumber())
                .favorites(List.of(favoriteResponseDto1, favoriteResponseDto2))
                .roles(user2.getRoles().stream().map(Enum::name).toList())
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
                .roles(userCreated.getRoles().stream().map(Enum::name).toList())
                .favorites(new ArrayList<>())
                .build();
    }

    private void initCartDtos() {
        cartResponseDto1 = CartResponseDto.builder()
                .cartId(cart1.getCartId())
                .userId(cart1.getUser().getUserId())
                .build();

        cartResponseDto2 = CartResponseDto.builder()
                .cartId(cart2.getCartId())
                .userId(cart2.getUser().getUserId())
                .build();

        cartItemResponseDto1 = CartItemResponseDto.builder()
                .cartItemId(cartItem1.getCartItemId())
                .product(productShortResponseDto1)
                .quantity(cartItem1.getQuantity())
                .build();

        cartItemResponseDto2 = CartItemResponseDto.builder()
                .cartItemId(cartItem2.getCartItemId())
                .product(productShortResponseDto2)
                .quantity(cartItem2.getQuantity())
                .build();

        cartResponseDto1.setItems(new ArrayList<>(List.of(cartItemResponseDto1, cartItemResponseDto2)));

        cartItemResponseDto3 = CartItemResponseDto.builder()
                .cartItemId(cartItem3.getCartItemId())
                .product(productShortResponseDto1)
                .quantity(cartItem3.getQuantity())
                .build();

        cartResponseDto2.setItems(new ArrayList<>(List.of(cartItemResponseDto3)));
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