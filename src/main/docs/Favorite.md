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

### FavoriteResponseDto

```json
{
  "favoriteId": 1,
  "product": {
    "productId": 5,
    "name": "string",
    "description": "string",
    "price": 9.49,
    "discountPrice": 6.99
  }
}
```

## Endpoints

| Method | URL                          | Role Required  | Description                               |
|--------|------------------------------|----------------|-------------------------------------------|
| GET    | `/v1/favorites`              | USER           | Get favorites for current user            |
| POST   | `/v1/favorites/{productId}`  | USER           | Add product to favorites for current user |
| DELETE | `/v1/favorites/{favoriteId}` | USER           | Remove from favorites                     |