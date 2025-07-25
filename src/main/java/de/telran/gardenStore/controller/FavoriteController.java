package de.telran.gardenStore.controller;
import de.telran.gardenStore.dto.FavoriteResponseDto;
import de.telran.gardenStore.dto.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Tag(name = "Favorites", description = "Favorite Controller")
public interface FavoriteController {

    @Operation(summary = "Get all favorites for current user")
    @ApiResponse(responseCode = "200", description = "List of user's favorite products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FavoriteResponseDto.class),
                    examples = @ExampleObject(name = "Favorites list", value = """
                            [
                                {
                                    "favoriteId": 1,
                                    "product": {
                                        "productId": 5,
                                        "name": "Tulip Bulb Mix (10 pcs)",
                                        "description": "Colorful tulip bulbs perfect for spring blooms",
                                        "price": 9.49,
                                        "discountPrice": 6.99,
                                        "categoryId": 3
                                    }
                                },
                                {
                                    "favoriteId": 2,
                                    "product": {
                                        "productId": 10,
                                        "name": "Hanging Planter Basket",
                                        "description": "Woven hanging basket with metal chain",
                                        "price": 12.50,
                                        "discountPrice": 9.25,
                                        "categoryId": 5
                                    }
                                }
                            ]
                            """)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "User not found", value = """
                            {
                                "exception": "UserNotFoundException",
                                "message": "User with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T23:34:45.605807"
                            }
                            """)))
    List<FavoriteResponseDto> getAllForCurrentUser();

    @Operation(summary = "Add product to favorites for current user")
    @ApiResponse(responseCode = "201", description = "Favorite created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FavoriteResponseDto.class),
                    examples = @ExampleObject(name = "Created favorite", value = """
                            {
                                "favoriteId": 8,
                                "product": {
                                    "productId": 10,
                                    "name": "Hanging Planter Basket",
                                    "description": "Woven hanging basket with metal chain",
                                    "price": 12.50,
                                    "discountPrice": 9.25
                                }
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "User or product not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Product not found", value = """
                                    {
                                        "exception": "ProductNotFoundException",
                                        "message": "Product with id 12 not found",
                                        "status": 404,
                                        "timestamp": "2025-07-08T23:36:11.847041"
                                    }
                                    """)))
    @ApiResponse(responseCode = "409", description = "Favorite already exists",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Conflict", value = """
                            {
                                "exception": "FavoriteAlreadyExistsException",
                                "message": "Favorite with userId 4 and productId 10 already exists",
                                "status": 409,
                                "timestamp": "2025-07-08T23:35:45.857475"
                            }
                            """)))
    FavoriteResponseDto create(
            @Parameter(description = "Add favorite for current user", example = "4") @Positive Long productId);

    @Operation(summary = "Delete favorite by ID")
    @ApiResponse(responseCode = "204", description = "Favorite deleted")
    @ApiResponse(responseCode = "404", description = "Favorite not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Favorite not found", value = """
                            {
                                "exception": "FavoriteNotFoundException",
                                "message": "Favorite with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T23:38:06.466938"
                            }
                            """)))
    void delete(@Parameter(description = "ID of favorite to delete", example = "1") @Positive Long favoriteId);
}
