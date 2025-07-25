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
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Products", description = "Product Controller")
public interface ProductController {

    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductShortResponseDto.class),
                    examples = @ExampleObject(name = "List of products", value = """
                            [
                                 {
                                     "productId": 1,
                                     "name": "All-Purpose Plant Fertilizer",
                                     "description": "Balanced NPK formula for all types of plants",
                                     "price": 15.99,
                                     "discountPrice": 10.99,
                                     "categoryId": 1
                                 },
                                 {
                                     "productId": 2,
                                     "name": "Organic Tomato Feed",
                                     "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                     "price": 13.99,
                                     "discountPrice": 10.49,
                                     "categoryId": 1
                                 }
                            ]
                            """)))
    List<ProductShortResponseDto> getAll(
            @Parameter(description = "Filter by category ID", example = "1") @Positive Long categoryId,
            @Parameter(description = "Filter by discount availability", example = "true") Boolean discount,
            @Parameter(description = "Minimum product price", example = "5") @Positive BigDecimal minPrice,
            @Parameter(description = "Maximum product price", example = "15") @Positive BigDecimal maxPrice,
            @Parameter(description = "Field to sort by", example = "price") @Pattern(regexp = "productId|name|price|category|discountPrice|createdAt|updatedAt") String sortBy,
            @Parameter(description = "Sort direction: true for ASC, false for DESC", example = "true") Boolean sortDirection);

    @Operation(summary = "Get product by ID")
    @ApiResponse(responseCode = "200", description = "Product found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class),
                    examples = @ExampleObject(name = "Product example", value = """
                            {
                                "productId": 1,
                                "name": "All-Purpose Plant Fertilizer",
                                "description": "Balanced NPK formula for all types of plants",
                                "price": 11.99,
                                "discountPrice": 8.99,
                                "categoryId": 1,
                                "imageUrl": "https://example.com/images/fertilizer_all_purpose.jpg",
                                "createdAt": "2025-07-08T20:42:12.980366",
                                "updatedAt": "2025-07-08T20:42:12.980366"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Product not found", value = """
                            {
                                "exception": "ProductNotFoundException",
                                "message": "Product with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T20:43:40.833577"
                            }
                            """)))
    ProductResponseDto getById(
            @Parameter(description = "ID of the product to retrieve", example = "1") @Positive Long productId);

    @Operation(summary = "Create a new product (only for role ADMIN)")
    @ApiResponse(responseCode = "201", description = "Product created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class),
                    examples = @ExampleObject(name = "Created product", value = """
                            {
                                "productId": 11,
                                "name": "Ergonomic Garden Trowel",
                                "description": "Durable stainless steel blade with ergonomic rubber grip",
                                "price": 12.50,
                                "discountPrice": 9.99,
                                "categoryId": 2,
                                "imageUrl": "https://example.com/images/garden_trowel.jpg",
                                "createdAt": "2025-07-08T21:14:12.679652",
                                "updatedAt": "2025-07-08T21:14:12.679688"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Validation error", value = """
                            {
                                "exception": "MethodArgumentNotValidException",
                                "messages": {
                                    "price": "Price is required",
                                    "imageUrl": "Image URL must be valid",
                                    "name": "Name must not be blank"
                                },
                                "status": 400,
                                "timestamp": "2025-07-08T20:48:30.631028"
                            }
                            """)))
    ProductResponseDto create(
            @RequestBody(description = "Product to create", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductCreateRequestDto.class),
                            examples = {
                                    @ExampleObject(name = "Trowel", value = """
                                            {
                                                "name": "Ergonomic Garden Trowel",
                                                "description": "Durable stainless steel blade with ergonomic rubber grip",
                                                "price": 12.50,
                                                "discountPrice": 9.99,
                                                "categoryId": 4,
                                                "imageUrl": "https://example.com/images/garden_trowel.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Pot", value = """
                                            {
                                                "name": "Terracotta Flower Pot",
                                                "description": "Handcrafted terracotta pot ideal for indoor and outdoor use",
                                                "price": 7.75,
                                                "discountPrice": 6.50,
                                                "categoryId": 5,
                                                "imageUrl": "https://example.com/images/terracotta_pot.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Seeds Pack", value = """
                                            {
                                                "name": "Lavender Seeds Pack",
                                                "description": "Fragrant lavender seeds for sunny gardens",
                                                "price": 4.99,
                                                "discountPrice": 3.49,
                                                "categoryId": 3,
                                                "imageUrl": "https://example.com/images/lavender_seeds.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Sprayer", value = """
                                            {
                                                "name": "Battery Powered Garden Sprayer",
                                                "description": "Lightweight sprayer ideal for pesticide and fertilizer application",
                                                "price": 39.95,
                                                "discountPrice": 34.99,
                                                "categoryId": 4,
                                                "imageUrl": "https://example.com/images/garden_sprayer.jpg"
                                            }
                                            """)
                            }))
            @Valid ProductCreateRequestDto productRequest);

    @Operation(summary = "Update existing product (only for role ADMIN)")
    @ApiResponse(responseCode = "202", description = "Product updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class),
                    examples = @ExampleObject(name = "Plant Fertilizer", value = """
                            {
                                "productId": 1,
                                "name": "All-Purpose Plant Fertilizer",
                                "description": "Balanced NPK formula for all types of plants",
                                "price": 15.99,
                                "discountPrice": 10.99,
                                "categoryId": 1,
                                "imageUrl": "https://example.com/images/fertilizer_all_purpose.jpg",
                                "createdAt": "2025-07-08T20:42:12.980366",
                                "updatedAt": "2025-07-08T20:50:30.157989"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Validation error", value = """
                            {
                                "exception": "MethodArgumentNotValidException",
                                "messages": {
                                    "price": "Price is required",
                                    "imageUrl": "Image URL must be valid",
                                    "name": "Name must not be blank"
                                },
                                "status": 400,
                                "timestamp": "2025-07-08T20:48:30.631028"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Product not found", value = """
                            {
                                "exception": "ProductNotFoundException",
                                "message": "Product with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T20:43:40.833577"
                            }
                            """)))
    ProductResponseDto update(
            @Parameter(description = "ID of the product to update", example = "1") @Positive Long productId,
            @RequestBody(description = "Product to update", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductCreateRequestDto.class),
                            examples = {
                                    @ExampleObject(name = "Plant Fertilizer", value = """
                                            {
                                                "name": "All-Purpose Plant Fertilizer",
                                                "description": "Balanced NPK formula for all types of plants",
                                                "price": 15.99,
                                                "discountPrice": 10.99,
                                                "categoryId": 1,
                                                "imageUrl": "https://example.com/images/fertilizer_all_purpose.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Tomato Feed", value = """
                                            {
                                                "name": "Organic Tomato Feed",
                                                "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                                "price": 15.99,
                                                "categoryId": 1,
                                                "imageUrl": "https://example.com/images/fertilizer_tomato_feed.jpg"
                                            }
                                            """)
                            }))
            @Valid ProductCreateRequestDto productRequest);

    @Operation(summary = "Delete product by ID (only for role ADMIN)")
    @ApiResponse(responseCode = "204", description = "Product successfully deleted")
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Product not found", value = """
                            {
                                "exception": "ProductNotFoundException",
                                "message": "Product with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T20:43:40.833577"
                            }
                            """)))
    void delete(
            @Parameter(description = "ID of the product to delete", example = "1") @Positive Long productId);
}