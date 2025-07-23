# Entity: User

## Description
Represents a system user.

## Table
- Table name: `app_users`

## Fields

| Field       | Type   | DB Column     | Description                    |
|-------------|--------|---------------|--------------------------------|
| userId      | Long   | user_id       | Primary Key                    |
| name        | String | name          | Full name                      |
| email       | String | email         | Unique, required               |
| phoneNumber | String | phone_number  | Required                       |
| password    | String | password_hash | Hashed password                |
| role        | Enum   | role          | ROLE_USER / ROLE_ADMINISTRATOR |

## DTOs

### UserCreateRequestDto

```json
{
  "name": "Frank Green",
  "email": "frank.green@example.com",
  "phoneNumber": "+1444555666",
  "password": "12345"
}
```

### UserResponseDto

```json
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
```

### UserShortResponseDto

```json
{
  "userId": 1,
  "name": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "phoneNumber": "+1234567890",
  "role": "ROLE_USER"
}
```

### LoginRequest
```json
{
  "email": "string",
  "password": "string"
}
```

## Endpoints

| Method | URL                  | Role Required | Description                 |
|--------|----------------------|---------------|-----------------------------|
| POST   | `/v1/users/register` | —             | Register user               |
| POST   | `/v1/users/login`    | —             | Authenticate user           |
| GET    | `/v1/users`          | ADMIN         | Get all users               |
| GET    | `/v1/users/me`       | USER / ADMIN  | Get current user            |
| GET    | `/v1/users/{userId}` | USER / ADMIN  | Get user by Id              |
| PUT    | `/v1/users/{userId}` | USER / ADMIN  | Update current user profile |
| DELETE | `/v1/users/{userId}` | USER / ADMIN  | Delete current user         |