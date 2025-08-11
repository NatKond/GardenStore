package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;


@Tag(name = "6. Orders", description = "Order Controller")
public interface OrderController {

    @Operation(summary = "Get all orders for current user")
    @ApiResponse(responseCode = "200", description = "List of user's orders",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderShortResponseDto.class),
                    examples = @ExampleObject(name = "Order list", value = """
                            [
                                {
                                    "orderId": 1,
                                    "userId": 1,
                                    "status": "AWAITING_PAYMENT",
                                    "deliveryAddress": "123 Garden Street",
                                    "contactPhone": "+1234567890",
                                    "deliveryMethod": "COURIER"
                                }
                            ]
                            """)))
    List<OrderShortResponseDto> getAllForCurrentUser();

    @Operation(summary = "Get all delivered orders for current user")
    @ApiResponse(responseCode = "200", description = "List of delivered orders for current user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(name = "Delivered orders for current user", value = """
                        [
                            {
                                "orderId": 1,
                                "userId": 1,
                                "status": "DELIVERED",
                                "deliveryAddress": "789 Oak Street",
                                "contactPhone": "+1234509876",
                                "deliveryMethod": "COURIER",
                                "createdAt": "2025-01-15T10:00:00",
                                "updatedAt": "2025-01-17T15:00:00",
                                "items": [
                                    {
                                        "orderItemId": 1,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99,
                                            "imageUrl": "/product_img/fertilizer_all_purpose.jpg"
                                        },
                                        "quantity": 2,
                                        "priceAtPurchase": 8.99
                                    },
                                    {
                                        "orderItemId": 2,
                                        "product": {
                                            "productId": 5,
                                            "name": "Espoma Organic Perlite",
                                            "description": "Porous material to aid soil aeration. Allows water, air, and nutrients to reach roots. Great for propagation.",
                                            "price": 13.95,
                                            "discountPrice": 12.65,
                                            "imageUrl": "/product_img/organic_perlite.jpeg"
                                        },
                                        "quantity": 1,
                                        "priceAtPurchase": 12.65
                                    }
                                ],
                                "totalAmount": 30.63
                            },
                            {
                                "orderId": 7,
                                "userId": 1,
                                "status": "DELIVERED",
                                "deliveryAddress": "789 Oak Street",
                                "contactPhone": "+1234509876",
                                "deliveryMethod": "COURIER",
                                "createdAt": "2025-05-03T17:00:00",
                                "updatedAt": "2025-05-04T10:00:00",
                                "items": [
                                    {
                                        "orderItemId": 14,
                                        "product": {
                                            "productId": 4,
                                            "name": "Espoma Organic Orchid Mix",
                                            "description": "Espoma's Organic Orchid Mix gives your orchid the ideal environment for growth and flowering.",
                                            "price": 6.95,
                                            "imageUrl": "/product_img/organic_orchid_mix.jpeg"
                                        },
                                        "quantity": 3,
                                        "priceAtPurchase": 6.95
                                    },
                                    {
                                        "orderItemId": 15,
                                        "product": {
                                            "productId": 11,
                                            "name": "Seed Pack - Carrots (Heirloom)",
                                            "description": "Non-GMO heirloom carrot seeds for rich harvests",
                                            "price": 3.99,
                                            "discountPrice": 2.95,
                                            "imageUrl": "/product_img/carrot_seeds.jpg"
                                        },
                                        "quantity": 2,
                                        "priceAtPurchase": 2.95
                                    }
                                ],
                                "totalAmount": 26.75
                            }
                        ]
                        """)))
    List<OrderResponseDto> getAllDeliveredForCurrentUser();

    @Operation(summary = "Get all orders (only for role ADMIN)")
    @ApiResponse(responseCode = "200", description = "List of all orders",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderShortResponseDto.class),
                    examples = @ExampleObject(name = "All orders", value = """
                        [
                            {
                                "orderId": 1,
                                "userId": 1,
                                "status": "DELIVERED",
                                "deliveryAddress": "789 Oak Street",
                                "totalAmount": 30.63,
                                "contactPhone": "+1234509876",
                                "deliveryMethod": "COURIER"
                            },
                            {
                                "orderId": 2,
                                "userId": 2,
                                "status": "DELIVERED",
                                "deliveryAddress": "456 Elm Avenue",
                                "totalAmount": 20.1,
                                "contactPhone": "+1987654322",
                                "deliveryMethod": "PICKUP"
                            },
                            {
                                "orderId": 3,
                                "userId": 3,
                                "status": "DELIVERED",
                                "deliveryAddress": "321 Maple Road",
                                "totalAmount": 41.98,
                                "contactPhone": "+1234560000",
                                "deliveryMethod": "COURIER"
                            },
                            {
                                "orderId": 4,
                                "userId": 3,
                                "status": "CANCELLED",
                                "deliveryAddress": "321 Maple Road",
                                "totalAmount": 28.48,
                                "contactPhone": "+1234560000",
                                "deliveryMethod": "COURIER"
                            }
                        ]
                        """)))
    List<OrderShortResponseDto> getAll();

    @Operation(summary = "Get order by ID")
    @ApiResponse(responseCode = "200", description = "Full order details",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(name = "Order details", value = """
                            {
                                "orderId": 1,
                                "userId": 1,
                                "status": "AWAITING_PAYMENT",
                                "deliveryAddress": "123 Garden Street",
                                "contactPhone": "+1234567890",
                                "deliveryMethod": "COURIER",
                                "createdAt": "2025-07-25T11:36:00.88543",
                                "updatedAt": "2025-07-25T11:36:00.88543",
                                "items": [
                                    {
                                        "orderItemId": 1,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99
                                        },
                                        "quantity": 2,
                                        "priceAtPurchase": 8.99
                                    }
                                ],
                                "totalAmount": 28.47
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "exception": "OrderNotFoundException",
                                "message": "Order with id 99 not found",
                                "status": 404,
                                "timestamp": "2025-07-25T11:38:02.747984"
                            }
                            """)))
    OrderResponseDto getById(
            @Parameter(description = "ID of the order", example = "1") @Positive Long orderId);

    @Operation(summary = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created successfully",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                                "orderId": 21,
                                "userId": 1,
                                "status": "CREATED",
                                "deliveryAddress": "123 Garden Street",
                                "contactPhone": "+1234509876",
                                "deliveryMethod": "PICKUP",
                                "createdAt": "2025-08-03T23:27:01.689652",
                                "updatedAt": "2025-08-03T23:27:01.689692",
                                "items": [
                                    {
                                        "orderItemId": 46,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99,
                                            "imageUrl": "/product_img/fertilizer_all_purpose.jpg"
                                        },
                                        "quantity": 1,
                                        "priceAtPurchase": 8.99
                                    }
                                ],
                                "totalAmount": 8.99
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Order creation failed",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "exception": "EmptyOrderException",
                                "message": "Order is empty.",
                                "status": 400,
                                "timestamp": "2025-07-25T11:38:52.887838"
                            }
                            """)))
    OrderResponseDto create(
            @RequestBody(description = "Order creation request", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderCreateRequestDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "deliveryAddress": "123 Garden Street",
                                      "deliveryMethod": "PICKUP",
                                      "items": [
                                        {
                                          "productId": 1,
                                          "quantity": 1
                                        },
                                        {
                                          "productId": 3,
                                          "quantity": 1
                                        }
                                      ]
                                    }
                                    """)))
            @Valid OrderCreateRequestDto orderCreateRequestDto);

    @Operation(summary = "Add product to order")
    @ApiResponse(responseCode = "200", description = "Product added to order",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                                 "orderId": 21,
                                 "userId": 1,
                                 "status": "CREATED",
                                 "deliveryAddress": "123 Garden Street",
                                 "contactPhone": "+1234509876",
                                 "deliveryMethod": "PICKUP",
                                 "createdAt": "2025-08-03T23:27:01.689652",
                                 "updatedAt": "2025-08-03T23:27:01.689692",
                                 "items": [
                                     {
                                         "orderItemId": 46,
                                         "product": {
                                             "productId": 1,
                                             "name": "All-Purpose Plant Fertilizer",
                                             "description": "Balanced NPK formula for all types of plants",
                                             "price": 11.99,
                                             "discountPrice": 8.99,
                                             "imageUrl": "/product_img/fertilizer_all_purpose.jpg"
                                         },
                                         "quantity": 1,
                                         "priceAtPurchase": 8.99
                                     },
                                     {
                                         "orderItemId": 47,
                                         "product": {
                                             "productId": 2,
                                             "name": "Organic Tomato Feed",
                                             "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                             "price": 13.99,
                                             "discountPrice": 10.49,
                                             "imageUrl": "/product_img/fertilizer_tomato_feed.jpg"
                                         },
                                         "quantity": 1,
                                         "priceAtPurchase": 10.49
                                     }
                                 ],
                                 "totalAmount": 19.48
                             }
                            """)))
    @ApiResponse(responseCode = "400", description = "Order is not modifiable",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "exception": "OrderModificationException",
                                "message": "Order cannot be modified in current status AWAITING_PAYMENT",
                                "status": 400,
                                "timestamp": "2025-07-25T11:45:02.93348"
                            }
                            """)))
    OrderResponseDto addItem(
            @Parameter(description = "Order ID", example = "21") @Positive Long orderId,
            @Parameter(description = "Product ID", example = "2") @Positive Long productId,
            @Parameter(description = "Quantity", example = "1") @Positive Integer quantity);

    @Operation(summary = "Update product quantity in order")
    @ApiResponse(responseCode = "200", description = "Order item quantity updated",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                                 "orderId": 21,
                                 "userId": 1,
                                 "status": "CREATED",
                                 "deliveryAddress": "123 Garden Street",
                                 "contactPhone": "+1234509876",
                                 "deliveryMethod": "PICKUP",
                                 "createdAt": "2025-08-03T23:29:02.252787",
                                 "updatedAt": "2025-08-03T23:29:02.252826",
                                 "items": [
                                     {
                                         "orderItemId": 46,
                                         "product": {
                                             "productId": 1,
                                             "name": "All-Purpose Plant Fertilizer",
                                             "description": "Balanced NPK formula for all types of plants",
                                             "price": 11.99,
                                             "discountPrice": 8.99,
                                             "imageUrl": "/product_img/fertilizer_all_purpose.jpg"
                                         },
                                         "quantity": 1,
                                         "priceAtPurchase": 8.99
                                     },
                                     {
                                         "orderItemId": 47,
                                         "product": {
                                             "productId": 2,
                                             "name": "Organic Tomato Feed",
                                             "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                             "price": 13.99,
                                             "discountPrice": 10.49,
                                             "imageUrl": "/product_img/fertilizer_tomato_feed.jpg"
                                         },
                                         "quantity": 3,
                                         "priceAtPurchase": 10.49
                                     }
                                 ],
                                 "totalAmount": 40.46
                             }
                            """)))

    @ApiResponse(responseCode = "400", description = "Order is not modifiable",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "exception": "OrderModificationException",
                                "message": "Order cannot be modified in current status AWAITING_PAYMENT",
                                "status": 400,
                                "timestamp": "2025-07-25T11:45:02.93348"
                            }
                            """)))
    OrderResponseDto updateItem(
            @Parameter(description = "Order item ID", example = "47") @Positive Long orderItemId,
            @Parameter(description = "New quantity", example = "3") @Positive Integer quantity);

    @Operation(summary = "Remove item from order")
    @ApiResponse(responseCode = "200", description = "Item removed from order",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                                  "orderId": 21,
                                  "userId": 1,
                                  "status": "CREATED",
                                  "deliveryAddress": "123 Garden Street",
                                  "contactPhone": "+1234509876",
                                  "deliveryMethod": "PICKUP",
                                  "createdAt": "2025-08-03T23:31:36.569785",
                                  "updatedAt": "2025-08-03T23:31:36.569818",
                                  "items": [
                                      {
                                          "orderItemId": 46,
                                          "product": {
                                              "productId": 1,
                                              "name": "All-Purpose Plant Fertilizer",
                                              "description": "Balanced NPK formula for all types of plants",
                                              "price": 11.99,
                                              "discountPrice": 8.99,
                                              "imageUrl": "/product_img/fertilizer_all_purpose.jpg"
                                          },
                                          "quantity": 1,
                                          "priceAtPurchase": 8.99
                                      }
                                  ],
                                  "totalAmount": 8.99
                              }
                            """))
    )
    @ApiResponse(responseCode = "400", description = "Order modification failed",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = {
                            @ExampleObject(name = "Empty Order Exception", value = """
                                    {
                                        "exception": "EmptyOrderException",
                                        "message": "Order is empty.",
                                        "status": 400,
                                        "timestamp": "2025-07-25T11:38:52.887838"
                                    }
                                    """),
                            @ExampleObject(name = "Order is not modifiable", value = """
                                    {
                                        "exception": "OrderModificationException",
                                        "message": "Order cannot be modified in current status AWAITING_PAYMENT",
                                        "status": 400,
                                        "timestamp": "2025-07-25T11:45:02.93348"
                                    }
                                    """),
                    }))
    OrderResponseDto removeItem(
            @Parameter(description = "Order item ID", example = "47") @Positive Long orderItemId);

    @Operation(summary = "Cancel order by ID")
    @ApiResponse(responseCode = "200", description = "Order cancelled",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                                "orderId": 21,
                                "userId": 1,
                                "status": "CANCELLED",
                                "deliveryAddress": "123 Garden Street",
                                "contactPhone": "+1234509876",
                                "deliveryMethod": "PICKUP",
                                "createdAt": "2025-08-03T23:31:36.569785",
                                "updatedAt": "2025-08-03T23:37:18.510762",
                                "items": [
                                    {
                                        "orderItemId": 46,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99,
                                            "imageUrl": "/product_img/fertilizer_all_purpose.jpg"
                                        },
                                        "quantity": 1,
                                        "priceAtPurchase": 8.99
                                    }
                                ]
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "exception": "OrderNotFoundException",
                                "message": "Order with id 99 not found",
                                "status": 404,
                                "timestamp": "2025-07-25T11:43:19.050783"
                            }
                            """)))
    OrderResponseDto delete(
            @Parameter(description = "Order ID", example = "1") @Positive Long orderId);
}
