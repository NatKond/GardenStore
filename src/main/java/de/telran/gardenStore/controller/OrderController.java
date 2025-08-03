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


@Tag(name = "Orders", description = "Controller responsible for managing user orders")
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
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
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
                                "orderId": 3,
                                "userId": 1,
                                "status": "CREATED",
                                "deliveryAddress": "123 Garden Street",
                                "deliveryMethod": "PICKUP",
                                "createdAt": "2025-07-25T11:01:53.093878",
                                "updatedAt": "2025-07-25T11:01:53.093913",
                                "items": [
                                    {
                                        "orderItemId": 4,
                                        "product": {
                                            "productId": 1
                                        },
                                        "quantity": 1,
                                        "priceAtPurchase": 11.99
                                    }
                                ],
                                "totalAmount": 11.99
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Order creation failed",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
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
                                "orderId": 3,
                                "userId": 1,
                                "status": "CREATED",
                                "deliveryAddress": "123 Garden Street",
                                "deliveryMethod": "PICKUP",
                                "createdAt": "2025-07-25T12:02:39.031111",
                                "updatedAt": "2025-07-25T12:02:39.031141",
                                "items": [
                                    {
                                        "orderItemId": 4,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99
                                        },
                                        "quantity": 1,
                                        "priceAtPurchase": 11.99
                                    },
                                    {
                                        "orderItemId": 5,
                                        "product": {
                                            "productId": 2,
                                            "name": "Organic Tomato Feed",
                                            "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                            "price": 13.99,
                                            "discountPrice": 10.49
                                        },
                                        "quantity": 1,
                                        "priceAtPurchase": 13.99
                                    }
                                ],
                                "totalAmount": 25.98
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Order is not modifiable",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "exception": "OrderModificationException",
                                "message": "Order cannot be modified in current status AWAITING_PAYMENT",
                                "status": 400,
                                "timestamp": "2025-07-25T11:45:02.93348"
                            }
                            """)))
    OrderResponseDto addItem(
            @Parameter(description = "Order ID", example = "3") @Positive Long orderId,
            @Parameter(description = "Product ID", example = "2") @Positive Long productId,
            @Parameter(description = "Quantity", example = "1") @Positive Integer quantity);

    @Operation(summary = "Update product quantity in order")
    @ApiResponse(responseCode = "200", description = "Order item quantity updated",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                                "orderId": 3,
                                "userId": 1,
                                "status": "CREATED",
                                "deliveryAddress": "123 Garden Street",
                                "deliveryMethod": "PICKUP",
                                "createdAt": "2025-07-25T11:39:57.88755",
                                "updatedAt": "2025-07-25T11:39:57.887592",
                                "items": [
                                    {
                                        "orderItemId": 4,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99
                                        },
                                        "quantity": 1,
                                        "priceAtPurchase": 11.99
                                    }
                                ],
                                "totalAmount": 11.99
                            }
                            """)))

    @ApiResponse(responseCode = "400", description = "Order is not modifiable",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "exception": "OrderModificationException",
                                "message": "Order cannot be modified in current status AWAITING_PAYMENT",
                                "status": 400,
                                "timestamp": "2025-07-25T11:45:02.93348"
                            }
                            """)))
    OrderResponseDto updateItem(
            @Parameter(description = "Order item ID", example = "4") @Positive Long orderItemId,
            @Parameter(description = "New quantity", example = "3") @Positive Integer quantity);

    @Operation(summary = "Remove item from order")
    @ApiResponse(responseCode = "200", description = "Item removed from order",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Order modification failed",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
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
            @Parameter(description = "Order item ID", example = "5") @Positive Long orderItemId);

    @Operation(summary = "Cancel order by ID")
    @ApiResponse(responseCode = "200", description = "Order cancelled",
            content = @Content(schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(value = """
                            {
                                "orderId": 1,
                                "status": "CANCELLED",
                                "deliveryAddress": "123 Garden Street",
                                "contactPhone": "+1234567890",
                                "deliveryMethod": "COURIER",
                                "createdAt": "2025-07-25T11:36:00.88543",
                                "updatedAt": "2025-07-25T11:43:01.906749",
                                "items": [
                                    {
                                        "orderItemId": 1,
                                        "product": {
                                            "productId": 1
                                        },
                                        "quantity": 2,
                                        "priceAtPurchase": 8.99
                                    }
                                ]
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
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
