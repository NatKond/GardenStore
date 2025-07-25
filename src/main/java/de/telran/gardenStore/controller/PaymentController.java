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
                                "orderId": 1,
                                "userId": 1,
                                "status": "PAID",
                                "deliveryAddress": "123 Garden Street",
                                "contactPhone": "+1234567890",
                                "deliveryMethod": "COURIER",
                                "createdAt": "2025-07-25T10:30:09.988527",
                                "updatedAt": "2025-07-25T10:30:42.770027",
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
                                    },
                                    {
                                        "orderItemId": 2,
                                        "product": {
                                            "productId": 2,
                                            "name": "Organic Tomato Feed",
                                            "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                            "price": 13.99,
                                            "discountPrice": 10.49
                                        },
                                        "quantity": 1,
                                        "priceAtPurchase": 10.49
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
            @Parameter(description = "ID of the order to pay for", example = "1") @Positive Long orderId,
            @Parameter(description = "Payment amount for the order", example = "28.47") @Positive BigDecimal paymentAmount);
}
