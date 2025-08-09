package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.report.ProductReport;
import de.telran.gardenStore.dto.report.ProfitReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Tag(name = "8. Reports", description = "Report Controller")
public interface ReportController {

    @Operation(summary = "Get top purchased products")
    @ApiResponse(responseCode = "200", description = "List of top purchased products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductReport.class),
                    examples = @ExampleObject(name = "Top purchased products", value = """
                            [
                                {
                                    "product": {
                                        "productId": 5,
                                        "name": "Espoma Organic Perlite",
                                        "description": "Porous material to aid soil aeration. Allows water, air, and nutrients to reach roots. Great for propagation.",
                                        "price": 13.95,
                                        "discountPrice": 12.65,
                                        "categoryId": 1,
                                        "imageUrl": "/product_img/organic_perlite.jpeg"
                                    },
                                    "quantity": 4
                                },
                                {
                                    "product": {
                                        "productId": 15,
                                        "name": "Hanging Planter Basket",
                                        "description": "Woven hanging basket with metal chain",
                                        "price": 12.50,
                                        "discountPrice": 9.25,
                                        "categoryId": 5,
                                        "imageUrl": "/product_img/hanging_planter.jpg"
                                    },
                                    "quantity": 2
                                }
                            ]
                            """)))
    List<ProductReport> getTopPurchasedProducts(
            @Parameter(description = "Limit number of results", example = "5") @Positive Integer limit);

    @Operation(summary = "Get top canceled products")
    @ApiResponse(responseCode = "200", description = "List of top canceled products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductReport.class),
                    examples = @ExampleObject(name = "Top canceled products", value = """
                            [
                                {
                                    "product": {
                                        "productId": 2,
                                        "name": "Organic Tomato Feed",
                                        "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                        "price": 13.99,
                                        "discountPrice": 10.49,
                                        "categoryId": 1,
                                        "imageUrl": "/product_img/fertilizer_tomato_feed.jpg"
                                    },
                                    "quantity": 2
                                },
                                {
                                    "product": {
                                        "productId": 4,
                                        "name": "Espoma Organic Orchid Mix",
                                        "description": "Espoma's Organic Orchid Mix gives your orchid the ideal environment for growth and flowering.",
                                        "price": 6.95,
                                        "categoryId": 1,
                                        "imageUrl": "/product_img/organic_orchid_mix.jpeg"
                                    },
                                    "quantity": 1
                                }
                            ]
                            """)))
    List<ProductReport> getTopCanceledProducts(
            @Parameter(description = "Limit number of results", example = "5") @Positive Integer limit);

    @Operation(summary = "Get products awaiting payment for specified number of days")
    @ApiResponse(responseCode = "200", description = "List of products awaiting payment",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductReport.class),
                    examples = @ExampleObject(name = "Products awaiting payment", value = """
                        [
                            {
                                "product": {
                                    "productId": 4,
                                    "name": "Espoma Organic Orchid Mix",
                                    "description": "Espoma's Organic Orchid Mix gives your orchid the ideal environment for growth and flowering.",
                                    "price": 6.95,
                                    "categoryId": 1,
                                    "imageUrl": "/product_img/organic_orchid_mix.jpeg"
                                },
                                "quantity": 1
                            },
                            {
                                "product": {
                                    "productId": 2,
                                    "name": "Organic Tomato Feed",
                                    "description": "Organic liquid fertilizer ideal for tomatoes and vegetables",
                                    "price": 13.99,
                                    "discountPrice": 10.49,
                                    "categoryId": 1,
                                    "imageUrl": "/product_img/fertilizer_tomato_feed.jpg"
                                },
                                "quantity": 1
                            },
                            {
                                "product": {
                                    "productId": 1,
                                    "name": "All-Purpose Plant Fertilizer",
                                    "description": "Balanced NPK formula for all types of plants",
                                    "price": 11.99,
                                    "discountPrice": 8.99,
                                    "categoryId": 1,
                                    "imageUrl": "/product_img/fertilizer_all_purpose.jpg"
                                },
                                "quantity": 1
                            }
                        ]
                        """)))
    List<ProductReport> getProductsAwaitingPaymentForDays(
            @Parameter(description = "Number of days the payment is pending", example = "16") @Positive Integer days,
            @Parameter(description = "Limit number of results", example = "5") @Positive Integer limit);

    @Operation(summary = "Get profit over a specified period")
    @ApiResponse(responseCode = "200", description = "Profit amount per grouped period",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfitReport.class),
                    examples = @ExampleObject(name = "Profit over period", value = """
                        [
                            {
                                "period": "2025-05",
                                "amountPerPeriod": 22.20
                            },
                            {
                                "period": "2025-06",
                                "amountPerPeriod": 90.55
                            },
                            {
                                "period": "2025-07",
                                "amountPerPeriod": 120.25
                            },
                            {
                                "period": "2025-08",
                                "amountPerPeriod": 17.40
                            }
                        ]
                        """)))
    List<ProfitReport> getProfitOverPeriod(@Parameter(description = "Time unit for the period (e.g., days, weeks, months)", example = "months") @Pattern(regexp = "days|months|years") String timeUnit,
                                           @Parameter(description = "Number of time units to include in the report", example = "3") @Positive Integer timeAmount,
                                           @Parameter(description = "Grouping unit for report results (e.g., day, week, month)", example = "month") @Pattern(regexp = "hour|day|week|month") String groupBy);
}
