package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ApiErrorResponse;
import de.telran.gardenStore.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Tag(name = "Payment", description = "Payment Controller")
public interface PaymentController {

    @Operation(summary = "Process payment for order",
            description = "Processes the payment for a given order ID and payment amount. Returns updated order details.")
    @ApiResponse(responseCode = "200", description = "Payment processed successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponseDto.class),
                    examples = @ExampleObject(name = "Successful Payment", value = """
                            {
                                 "orderId": 17,
                                 "userId": 1,
                                 "status": "PAID",
                                 "deliveryAddress": "789 Oak Street",
                                 "contactPhone": "+1234509876",
                                 "deliveryMethod": "COURIER",
                                 "createdAt": "2025-07-28T14:20:00",
                                 "updatedAt": "2025-08-03T23:38:16.373632",
                                 "items": [
                                     {
                                         "orderItemId": 36,
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
                                     },
                                     {
                                         "orderItemId": 37,
                                         "product": {
                                             "productId": 6,
                                             "name": "Slug & Snail Barrier Pellets",
                                             "description": "Pet-safe barrier pellets to protect plants from slugs",
                                             "price": 7.50,
                                             "discountPrice": 5.75,
                                             "imageUrl": "/product_img/protection_slug_pellets.jpg"
                                         },
                                         "quantity": 2,
                                         "priceAtPurchase": 5.75
                                     },
                                     {
                                         "orderItemId": 38,
                                         "product": {
                                             "productId": 8,
                                             "name": "Bonide Diatomaceous Earth",
                                             "description": "All-natural insect killer that targets pests without harming beneficial critters. Ideal for crops and livestock areas.",
                                             "price": 27.95,
                                             "discountPrice": 27.05,
                                             "imageUrl": "/product_img/diatomaceous_earth.jpeg"
                                         },
                                         "quantity": 1,
                                         "priceAtPurchase": 27.05
                                     }
                                 ]
                             }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation failed or invalid order state",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples =
                    @ExampleObject(name = "Invalid Order Status", value = """
                            {
                                "exception": "OrderPaymentRejectedException",
                                "message": "Order cannot be paid in current status: PAID",
                                "status": 400,
                                "timestamp": "2025-07-25T10:43:28.118055"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Order not found", value = """
                            {
                                "exception": "OrderNotFoundException",
                                "message": "Order with id 50 not found",
                                "status": 404,
                                "timestamp": "2025-07-25T10:42:09.664574"
                            }
                            """)))
    OrderResponseDto processPayment(
            @Parameter(description = "ID of the order to pay for", example = "17") @Positive Long orderId,
            @Parameter(description = "Payment amount for the order", example = "49.04") @Positive BigDecimal paymentAmount);
}
