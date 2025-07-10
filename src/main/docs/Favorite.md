# Entity: Favorite

## Description
Represents a user's favorite product.

## Table
- Table name: `favorite`

## Fields

| Field      | Type | DB Column     | Description                           |
|------------|------|---------------|---------------------------------------|
| favoriteId | Long | favorite_id   | Primary Key                           |
| userId     | Long | user_id       | Foreign key to `users(user_id)`       |
| productId  | Long | product_id    | Foreign key to `products(product_id)` |

## DTOs

### FavoriteCreateRequestDto

```json
{
  "userId": 3,
  "productId": 1
}
```

### FavoriteResponseDto

```json
{
  "favoriteId": 1,
  "userId": 3,
  "productId": 1
}
```

## Endpoints

| Method | URL                          | Role Required  | Description                     |
|--------|------------------------------|----------------|---------------------------------|
| GET    | `/v1/favorites/{userId}`     | USER           | Get favorites product by userId |
| POST   | `/v1/favorites`              | USER           | Add to favorites                |
| DELETE | `/v1/favorites/{favoriteId}` | USER           | Remove from favorites           |