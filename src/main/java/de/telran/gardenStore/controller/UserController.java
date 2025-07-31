package de.telran.gardenStore.controller;

import de.telran.gardenStore.dto.ApiErrorResponse;
import de.telran.gardenStore.dto.UserCreateRequestDto;
import de.telran.gardenStore.dto.UserResponseDto;
import de.telran.gardenStore.dto.UserShortResponseDto;
import de.telran.gardenStore.dto.security.LoginRequest;
import de.telran.gardenStore.dto.security.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Tag(name = "Users", description = "User Controller")
public interface UserController {

    @Operation(summary = "Get all users (only for role ADMIN)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserShortResponseDto.class),
                    examples = @ExampleObject(name = "List of users", value = """
                            [
                                {
                                    "userId": 1,
                                    "name": "Alice Johnson",
                                    "email": "alice.johnson@example.com",
                                    "phoneNumber": "+1234567890",
                                    "role": "ROLE_USER"
                                },
                                {
                                    "userId": 2,
                                    "name": "Bob Smith",
                                    "email": "bob.smith@example.com",
                                    "phoneNumber": "+1987654321",
                                    "role": "ROLE_USER"
                                }
                            ]
                            """
                    )))
    List<UserShortResponseDto> getAll();

    @Operation(summary = "Get user by ID (only for role ADMIN)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(name = "Alice Johnson", value = """
                            {
                                "userId": 1,
                                "name": "Alice Johnson",
                                "email": "alice.johnson@example.com",
                                "phoneNumber": "+1234567890",
                                "role": "ROLE_USER",
                                "favorites": [
                                    {
                                        "favoriteId": 1,
                                        "product": {
                                            "productId": 5,
                                            "name": "Tulip Bulb Mix (10 pcs)",
                                            "description": "Colorful tulip bulbs perfect for spring blooms",
                                            "price": 9.49,
                                            "discountPrice": 6.99
                                        }
                                    }
                                ]
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "User not found", value = """
                            {
                                "exception": "UserNotFoundException",
                                "message": "User with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T12:33:30.849474"
                            }
                            """
                    )))
    UserResponseDto getById(
            @Parameter(description = "ID of the user", example = "1")
            @Positive Long userId);

    @Operation(summary = "Get current user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(name = "Alice Johnson", value = """
                            {
                                "userId": 1,
                                "name": "Alice Johnson",
                                "email": "alice.johnson@example.com",
                                "phoneNumber": "+1234567890",
                                "role": "ROLE_USER",
                                "favorites": [
                                    {
                                        "favoriteId": 1,
                                        "product": {
                                            "productId": 5,
                                            "name": "Tulip Bulb Mix (10 pcs)",
                                            "description": "Colorful tulip bulbs perfect for spring blooms",
                                            "price": 9.49,
                                            "discountPrice": 6.99
                                        }
                                    }
                                ]
                            }
                            """)))
    UserResponseDto getCurrent();

    @Operation(summary = "Login for existing user")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated user",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class)))
    LoginResponse login(
            @RequestBody(description = "User to login",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = {
                                    @ExampleObject(name = "Alice Johnson(ADMIN, USER)", value = """
                                    {
                                      "email": "alice.johnson@example.com",
                                      "password": "12345"
                                    }
                                    """),
                                    @ExampleObject(name = "Bob Smith(USER)", value = """
                                    {
                                      "email": "bob.smith@example.com",
                                      "password": "12345"
                                    }
                                    """),
                                    @ExampleObject(name = "Carol Lee(USER)", value = """
                                    {
                                      "email": "carol.lee@example.com",
                                      "password": "12345"
                                    }
                                    """),
                                    @ExampleObject(name = "David Brown(USER)", value = """
                                    {
                                      "email": "david.brown@example.com",
                                      "password": "12345"
                                    }
                                    """)
                            }
                    ))
            @Valid LoginRequest loginRequest);

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(name = "Alice Johnson", value = """
                            {
                                "userId": 6,
                                "name": "Frank Green",
                                "email": "frank.green@example.com",
                                "phoneNumber": "+1444555666",
                                "role": "ROLE_USER",
                                "favorites": []
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Validation error", value = """
                            {
                                "exception": "MethodArgumentNotValidException",
                                "messages": {
                                    "password": "Password should have at least 5 characters",
                                    "phoneNumber": "Phone number should have at least 10 characters",
                                    "email": "Should be a well-formed email address"
                                },
                                "status": 400,
                                "timestamp": "2025-07-08T12:37:38.600215"
                            }
                            """
                    )))
    @ApiResponse(responseCode = "409", description = "User with this email already exists",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "User with email already exists", value = """
                            {
                                "exception": "UserWithEmailAlreadyExistsException",
                                "message": "User with email bob@example.com already exists",
                                "status": 409,
                                "timestamp": "2025-07-08T12:38:35.709413"
                            }
                            """
                    )))
    UserResponseDto create(
            @RequestBody(
                    description = "User to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateRequestDto.class),
                            examples = {
                                    @ExampleObject(name = "Eve White", value = """
                                            {
                                                "name": "Eve White",
                                                "email": "eve.white@example.com",
                                                "phoneNumber": "+1333444555",
                                                "password": "12345"
                                            }
                                            """),
                                    @ExampleObject(name = "Frank Green", value = """
                                            {
                                                "name": "Frank Green",
                                                "email": "frank.green@example.com",
                                                "phoneNumber": "+1444555666",
                                                "password": "12345"
                                            }
                                            """),
                                    @ExampleObject(name = "David Black", value = """
                                            {
                                                "name": "David Black",
                                                "email": "david.Black@example.com",
                                                "phoneNumber": "+1222333555",
                                                "password": "123456"
                                            }
                                            """)
                            }
                    )
            )
            @Valid UserCreateRequestDto userRequest);

    @Operation(summary = "Update an existing user")
    @ApiResponse(responseCode = "202", description = "User successfully updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class),
                    examples = @ExampleObject(name = "Alice Johnson", value = """
                            {
                                "userId": 1,
                                "name": "Alice Johnson",
                                "email": "alice.johnson@example.com",
                                "phoneNumber": "+1234567890",
                                "role": "ROLE_USER",
                                "favorites": []
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "Validation error", value = """
                            {
                                "exception": "MethodArgumentNotValidException",
                                "messages": {
                                    "password": "Password should have at least 5 characters",
                                    "phoneNumber": "Phone number should have at least 10 characters",
                                    "email": "Should be a well-formed email address"
                                },
                                "status": 400,
                                "timestamp": "2025-07-08T12:37:38.600215"
                            }
                            """
                    )))
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "User not found", value = """
                            {
                                "exception": "UserNotFoundException",
                                "message": "User with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T12:33:30.849474"
                            }
                            """
                    )))
    @ApiResponse(responseCode = "409", description = "User with this email already exists",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "User with email already exists", value = """
                            {
                                "exception": "UserWithEmailAlreadyExistsException",
                                "message": "User with email bob@example.com already exists",
                                "status": 409,
                                "timestamp": "2025-07-08T12:38:35.709413"
                            }
                            """
                    )))
    UserResponseDto update(
            @Parameter(description = "ID of the user to update", example = "4")
            @RequestBody(
                    description = "User to update",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateRequestDto.class),
                            examples = {
                                    @ExampleObject(name = "David Green", value = """
                                            {
                                                "name": "Alice Johnson",
                                                "email": "alice.johnson@example.com",
                                                "phoneNumber": "+1234567890",
                                                "password": "123455632"
                                            }
                                            """),
                                    @ExampleObject(name = "Frank Green", value = """
                                            {
                                                "name": "Bob Smith",
                                                "email": "bob.smith@example.com",
                                                "phoneNumber": "+1987654321",
                                                "password": "123455632"
                                            }
                                            """)
                            }
                    )
            )
            @Valid UserCreateRequestDto userRequest);

    @Operation(summary = "Delete user by ID")
    @ApiResponse(responseCode = "204", description = "User successfully deleted")
    @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(name = "User not found", value = """
                            {
                                "exception": "UserNotFoundException",
                                "message": "User with id 12 not found",
                                "status": 404,
                                "timestamp": "2025-07-08T12:33:30.849474"
                            }
                            """
                    )))
    void delete();
}
