package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ApiErrorResponse;
import de.telran.gardenStore.dto.CartResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;


@Tag(name = "Cart", description = "Controller responsible for managing the user's shopping cart")
public interface CartController {

    @Operation(summary = "Get current user's cart",
            description = "Retrieves the shopping cart for the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CartResponseDto.class),
                    examples = @ExampleObject(name = "Cart Response", value = """
                            {
                                "cartId": 1,
                                "userId": 1,
                                "items": [
                                    {
                                        "cartItemId": 1,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99
                                        },
                                        "quantity": 2
                                    },
                                    {
                                        "cartItemId": 2,
                                        "product": {
                                            "productId": 2,
                                            "name": "Organic Tomato Feed",
                                            "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                            "price": 13.99,
                                            "discountPrice": 10.49
                                        },
                                        "quantity": 1
                                    }
                                ]
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Cart not found for user",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Cart not found", value = """
                            {
                                "exception": "CartNotFoundException",
                                "message": "Cart for user 5 not found",
                                "status": 404,
                                "timestamp": "2025-07-25T11:02:54.107778"
                            }
                            """)))
    CartResponseDto getForCurrentUser();

    @Operation(summary = "Add product to cart",
            description = "Adds a product to the current user's shopping cart.")
    @ApiResponse(responseCode = "200", description = "Product added to cart",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CartResponseDto.class),
                    examples = @ExampleObject(name = "Product Added", value = """
                            {
                                "cartId": 1,
                                "userId": 1,
                                "items": [
                                    {
                                        "cartItemId": 1,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99
                                        },
                                        "quantity": 2
                                    },
                                    {
                                        "cartItemId": 2,
                                        "product": {
                                            "productId": 2,
                                            "name": "Organic Tomato Feed",
                                            "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                            "price": 13.99,
                                            "discountPrice": 10.49
                                        },
                                        "quantity": 1
                                    },
                                    {
                                        "cartItemId": 6,
                                        "product": {
                                            "productId": 3,
                                            "name": "Slug & Snail Barrier Pellets",
                                            "description": "Pet-safe barrier pellets to protect plants from slugs",
                                            "price": 7.50,
                                            "discountPrice": 5.75
                                        },
                                        "quantity": 1
                                    }
                                ]
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Product not found", value = """
                            {
                                "exception": "ProductNotFoundException",
                                "message": "Product with id 99 not found",
                                "status": 404,
                                "timestamp": "2025-07-25T10:50:12.123456"
                            }
                            """)))
    CartResponseDto addItem(
            @Parameter(description = "ID of the product to add", example = "3") @Positive Long productId);

    @Operation(summary = "Update cart item quantity",
            description = "Updates the quantity of a specific item in the user's cart.")
    @ApiResponse(responseCode = "200", description = "Cart item updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CartResponseDto.class),
                    examples = @ExampleObject(name = "Item Updated", value = """
                            {
                                "cartId": 1,
                                "userId": 1,
                                "items": [
                                    {
                                        "cartItemId": 1,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99,
                                            "imageUrl": "/product_img/fertilizer_all_purpose.jpg"
                                        },
                                        "quantity": 2
                                    },
                                    {
                                        "cartItemId": 2,
                                        "product": {
                                            "productId": 2,
                                            "name": "Organic Tomato Feed",
                                            "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                            "price": 13.99,
                                            "discountPrice": 10.49,
                                            "imageUrl": "/product_img/fertilizer_tomato_feed.jpg"
                                        },
                                        "quantity": 1
                                    },
                                    {
                                        "cartItemId": 6,
                                        "product": {
                                            "productId": 3,
                                            "name": "Espoma Organic Potting Mix23",
                                            "description": "Organic mix feeds your plant with nutrients and gives roots the space to breathe and grow. See your plants thrive!",
                                            "price": 6.95,
                                            "discountPrice": 6.7,
                                            "imageUrl": "/product_img/organic_potting_mix.jpeg"
                                        },
                                        "quantity": 3
                                    }
                                ]
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Cart item not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Item not found", value = """
                            {
                                "exception": "CartItemNotFoundException",
                                "message": "Cart item with id 10 not found",
                                "status": 404,
                                "timestamp": "2025-07-25T10:51:47.789012"
                            }
                            """)))
    CartResponseDto updateItem(
            @Parameter(description = "ID of the cart item", example = "6") @Positive Long cartItemId,
            @Parameter(description = "New quantity", example = "3") @Positive Integer quantity);

    @Operation(summary = "Delete cart item",
            description = "Removes an item from the user's cart.")
    @ApiResponse(responseCode = "200", description = "Item deleted from cart",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CartResponseDto.class),
                    examples = @ExampleObject(name = "Item Deleted", value = """
                            {
                                "cartId": 1,
                                "userId": 1,
                                "items": [
                                    {
                                        "cartItemId": 1,
                                        "product": {
                                            "productId": 1,
                                            "name": "All-Purpose Plant Fertilizer",
                                            "description": "Balanced NPK formula for all types of plants",
                                            "price": 11.99,
                                            "discountPrice": 8.99
                                        },
                                        "quantity": 2
                                    },
                                    {
                                        "cartItemId": 2,
                                        "product": {
                                            "productId": 2,
                                            "name": "Organic Tomato Feed",
                                            "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                            "price": 13.99,
                                            "discountPrice": 10.49
                                        },
                                        "quantity": 1
                                    }
                                ]
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Cart item not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Item not found", value = """
                            {
                                "exception": "CartItemNotFoundException",
                                "message": "Cart item with id 6 not found",
                                "status": 404,
                                "timestamp": "2025-07-25T10:53:01.123456"
                            }
                            """)))
    CartResponseDto deleteItem(
            @Parameter(description = "ID of the cart item to delete", example = "6") @Positive Long cartItemId);
}