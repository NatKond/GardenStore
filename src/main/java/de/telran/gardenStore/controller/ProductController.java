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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "3. Products", description = "Product Controller")
public interface ProductController {

    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductShortResponseDto.class),
                    examples = @ExampleObject(name = "List of products", value = """
                            [
                              {
                                "productId": 2,
                                "name": "Organic Tomato Feed",
                                "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                "price": 13.99,
                                "discountPrice": 10.49,
                                "categoryId": 1,
                                "imageUrl": "/product_img/fertilizer_tomato_feed.jpg"
                              },
                              {
                                "productId": 5,
                                "name": "Espoma Organic Perlite",
                                "description": "Porous material to aid soil aeration. Allows water, air, and nutrients to reach roots. Great for propagation.",
                                "price": 13.95,
                                "discountPrice": 12.65,
                                "categoryId": 1,
                                "imageUrl": "/product_img/organic_perlite.jpeg"
                              }
                            ]
                            """)))
    List<ProductShortResponseDto> getAll(
            @Parameter(description = "Filter by category ID", example = "1") @Positive Long categoryId,
            @Parameter(description = "Filter by discount availability", example = "true") Boolean discount,
            @Parameter(description = "Minimum product price", example = "5") @Positive BigDecimal minPrice,
            @Parameter(description = "Maximum product price", example = "15") @Positive BigDecimal maxPrice,
            @Parameter(description = "Field to sort by (productId|name|price|category|discountPrice|createdAt|updatedAt)", example = "price") @Pattern(regexp = "productId|name|price|category|discountPrice|createdAt|updatedAt") String sortBy,
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
                    schema = @Schema(implementation = ApiResponse.class),
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
                    schema = @Schema(implementation = ApiResponse.class),
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
                                                "imageUrl": "https://example.com/product_img/garden_trowel.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Pot", value = """
                                            {
                                                "name": "Terracotta Flower Pot",
                                                "description": "Handcrafted terracotta pot ideal for indoor and outdoor use",
                                                "price": 7.75,
                                                "discountPrice": 6.50,
                                                "categoryId": 5,
                                                "imageUrl": "https://example.com/product_img/terracotta_pot.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Seeds Pack", value = """
                                            {
                                                "name": "Lavender Seeds Pack",
                                                "description": "Fragrant lavender seeds for sunny gardens",
                                                "price": 4.99,
                                                "discountPrice": 3.49,
                                                "categoryId": 3,
                                                "imageUrl": "https://example.com/product_img/lavender_seeds.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Sprayer", value = """
                                            {
                                                "name": "Battery Powered Garden Sprayer",
                                                "description": "Lightweight sprayer ideal for pesticide and fertilizer application",
                                                "price": 39.95,
                                                "discountPrice": 34.99,
                                                "categoryId": 4,
                                                "imageUrl": "https://example.com/product_img/garden_sprayer.jpg"
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
                                "imageUrl": "https://example.com/product_img/fertilizer_all_purpose.jpg",
                                "createdAt": "2025-07-08T20:42:12.980366",
                                "updatedAt": "2025-07-08T20:50:30.157989"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
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
                    schema = @Schema(implementation = ApiResponse.class),
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
                                                  "price": 11.99,
                                                  "categoryId": 1,
                                                  "imageUrl": "https://example.com/product_img/fertilizer_all_purpose.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Tomato Feed", value = """
                                            {
                                                "name": "Organic Tomato Feed",
                                                "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                                "price": 15.99,
                                                "categoryId": 1,
                                                "imageUrl": "https://example.com/product_img/fertilizer_tomato_feed.jpg"
                                            }
                                            """),
                                    @ExampleObject(name = "Organic Potting Mix", value = """
                                            {
                                              "name": "Espoma Organic Potting Mix23",
                                              "description": "Organic mix feeds your plant with nutrients and gives roots the space to breathe and grow. See your plants thrive!",
                                              "price": 6.95,
                                              "categoryId": 1,
                                              "imageUrl": "https://example.com/product_img/organic_potting_mix.jpeg"
                                            }
                                            """),
                                    @ExampleObject(name = "Espoma Organic Perlite", value = """
                                            {
                                              "name": "Espoma Organic Perlite",
                                              "description": "Porous material to aid soil aeration. Allows water, air, and nutrients to reach roots. Great for propagation.",
                                              "price": 13.95,
                                              "categoryId": 1,
                                              "imageUrl": "https://example.com/product_img/organic_perlite.jpeg"
                                            }
                                            """),
                                    @ExampleObject(name = "Espoma Organic Perlite", value = """
                                            {
                                              "name": "Espoma Organic Perlite",
                                              "description": "Porous material to aid soil aeration. Allows water, air, and nutrients to reach roots. Great for propagation.",
                                              "price": 13.95,
                                              "categoryId": 1,
                                              "imageUrl": "https://example.com/product_img/organic_perlite.jpeg"
                                            }
                                            """)
                            }))
            @Valid ProductCreateRequestDto productRequest);

    @Operation(summary = "Delete product by ID (only for role ADMIN)")
    @ApiResponse(responseCode = "204", description = "Product successfully deleted")
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
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

    @Operation(summary = "Set discount for a product (only for role ADMIN)")
    @ApiResponse(responseCode = "200", description = "Discount successfully applied",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class),
                    examples = @ExampleObject(name = "Product with discount applied", value = """
                            {
                                "productId": 9,
                                "name": "Savannah Summer Annual Collection",
                                "description": "We love this fusion of colorful blossoms, created by combining some of the most floriferous and high performance annuals we know in our Savannah Summer Collection.",
                                "price": 53.00,
                                "discountPrice": 37.10,
                                "categoryId": 3,
                                "imageUrl": "https://example.com/product_img/summer_annual_collection.jpeg",
                                "createdAt": "2025-08-09T11:32:52.345883",
                                "updatedAt": "2025-08-09T11:48:40.907027"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "Product not found", value = """
                            {
                                "exception": "ProductNotFoundException",
                                "message": "Product with id 99 not found",
                                "status": 404,
                                "timestamp": "2025-08-09T11:55:22.784321"
                            }
                            """)))
    ProductResponseDto setDiscount(
            @Parameter(description = "ID of the product to apply discount", example = "9") @Positive Long productId,
            @Parameter(description = "Discount percentage to apply", example = "30")
            @Min(value = 1, message = "Discount must be at least 1%")
            @Max(value = 99, message = "Discount cannot exceed 99%")
            BigDecimal discountPercentage
    );

    @Operation(summary = "Get product of the day")
    @ApiResponse(responseCode = "200", description = "Product of the day retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class),
                    examples = @ExampleObject(name = "Product of the day", value = """
                            {
                                "productId": 15,
                                "name": "Hanging Planter Basket",
                                "description": "Woven hanging basket with metal chain",
                                "price": 12.50,
                                "discountPrice": 9.25,
                                "categoryId": 5,
                                "imageUrl": "https://example.com/product_img/hanging_planter.jpg",
                                "createdAt": "2025-08-09T11:50:27.305166",
                                "updatedAt": "2025-08-09T11:50:27.305166"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Product of the day not set",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "No product of the day", value = """
                            {
                                "exception": "ProductOfTheDayNotFoundException",
                                "message": "Product of the day is not set",
                                "status": 404,
                                "timestamp": "2025-08-09T12:05:14.112874"
                            }
                            """)))
    ProductResponseDto getProductOfTheDay();
}