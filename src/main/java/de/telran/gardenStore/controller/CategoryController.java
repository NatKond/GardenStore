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

@Tag(name = "2. Categories", description = "Category Controller")
public interface CategoryController {

    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "List of all categories",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryShortResponseDto.class),
                    examples = @ExampleObject(name = "List of categories", value = """
                            [
                                {
                                    "categoryId": 1,
                                    "name": "Fertilizer"
                                },
                                {
                                    "categoryId": 2,
                                    "name": "Protective products and septic tanks"
                                },
                                {
                                    "categoryId": 3,
                                    "name": "Planting material"
                                }
                            ]
                            """)))
    List<CategoryShortResponseDto> getAll();

    @Operation(summary = "Get category by ID")
    @ApiResponse(responseCode = "200", description = "Category found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryResponseDto.class),
                    examples = @ExampleObject(name = "Category with products", value = """
                            {
                                "categoryId": 1,
                                "name": "Fertilizer",
                                "products": [
                                    {
                                        "productId": 1,
                                        "name": "All-Purpose Plant Fertilizer",
                                        "description": "Balanced NPK formula for all types of plants",
                                        "price": 11.99,
                                        "discountPrice": 8.99,
                                        "categoryId": 1
                                    }
                                ]
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "Category not found", value = """
                            {
                                "exception": "CategoryNotFoundException",
                                "message": "Category with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T22:06:22.675691"
                            }
                            """)))
    CategoryResponseDto getById(@Parameter(description = "ID of the category", example = "1")
                                        @Positive Long categoryId);

    @Operation(summary = "Create a new category (only for role ADMIN)")
    @ApiResponse(responseCode = "201", description = "Category successfully created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryResponseDto.class),
                    examples = @ExampleObject(name = "Created category", value = """
                            {
                                "categoryId": 6,
                                "name": "Outdoor furniture",
                                "products": []
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "Validation error", value = """
                            {
                                "exception": "MethodArgumentNotValidException",
                                "messages": {
                                    "name": "Category name should be from 2 to 20 characters"
                                },
                                "status": 400,
                                "timestamp": "2025-07-08T22:07:50.398558"
                            }
                            """)))
    @ApiResponse(responseCode = "409", description = "Category name conflict",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "Conflict error", value = """
                            {
                                "exception": "CategoryWithNameAlreadyExistsException",
                                "message": "Category with name Outdoor furniture already exists.",
                                "status": 409,
                                "timestamp": "2025-07-08T22:07:08.754019"
                            }
                            """)))
    CategoryResponseDto create(
            @RequestBody(
                    description = "Category to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryCreateRequestDto.class),
                            examples = {
                                    @ExampleObject(name = "Outdoor furniture", value = """
                                            {
                                                "name": "Outdoor furniture"
                                            }
                                            """),
                                    @ExampleObject(name = "Vertical planters", value = """
                                            {
                                                "name": "Vertical planters"
                                            }
                                            """),
                                    @ExampleObject(name = "Herb garden kits", value = """
                                            {
                                                "name": "Herb garden kits"
                                            }
                                            """)
                            }
                    )
            )
            @Valid CategoryCreateRequestDto dto);

    @Operation(summary = "Update existing category (only for role ADMIN)")
    @ApiResponse(responseCode = "202", description = "Category successfully updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryResponseDto.class),
                    examples = @ExampleObject(name = "Updated category", value = """
                            {
                                "categoryId": 1,
                                "name": "Compost and soil amendments",
                                "products": [
                                    {
                                        "productId": 1,
                                        "name": "All-Purpose Plant Fertilizer",
                                        "description": "Balanced NPK formula for all types of plants",
                                        "price": 11.99,
                                        "discountPrice": 8.99,
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
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "Validation error", value = """
                            {
                                "exception": "MethodArgumentNotValidException",
                                "messages": {
                                    "name": "Category name should be from 2 to 30 characters"
                                },
                                "status": 400,
                                "timestamp": "2025-07-08T22:10:32.856346"
                            }
                            """)))
    @ApiResponse(responseCode = "409", description = "Category name conflict",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "Conflict error", value = """
                            {
                                "exception": "CategoryWithNameAlreadyExistsException",
                                "message": "Category with name Compost and soil amendments already exists.",
                                "status": 409,
                                "timestamp": "2025-07-08T22:10:18.67931"
                            }
                            """)))
    CategoryResponseDto update(@Parameter(description = "ID of the category to update", example = "1")
                                       @Positive Long categoryId,
                                       @RequestBody(
                                               description = "Category to update",
                                               required = true,
                                               content = @Content(
                                                       mediaType = "application/json",
                                                       schema = @Schema(implementation = CategoryCreateRequestDto.class),
                                                       examples = {
                                                               @ExampleObject(name = "Compost and soil amendments", value = """
                                                                       {
                                                                           "name": "Compost and soil amendments"
                                                                       }
                                                                       """),
                                                               @ExampleObject(name = "Garden decor", value = """
                                                                       {
                                                                           "name": "Garden decor"
                                                                       }
                                                                       """),
                                                               @ExampleObject(name = "Raised beds", value = """
                                                                       {
                                                                           "name": "Raised beds"
                                                                       }
                                                                       """)
                                                       }
                                               )
                                       )
                                       @Valid CategoryCreateRequestDto dto);


    @Operation(summary = "Delete category by ID (only for role ADMIN)")
    @ApiResponse(responseCode = "200", description = "Category successfully deleted")
    @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "Category not found", value = """
                            {
                                "exception": "CategoryNotFoundException",
                                "message": "Category with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T22:06:22.675691"
                            }
                            """)))
    void delete(@Parameter(description = "ID of the category to delete", example = "1") @Positive Long categoryId);
}