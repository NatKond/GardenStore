# Entity: Cart

## Description
Represents a user's shopping cart.

## Table
- Table name: `carts`

## Fields

| Field   | Type | DB Column | Description                         |
|---------|------|-----------|-------------------------------------|
| cartId  | Long | cart_id   | Primary key                         |
| userId  | Long | user_id   | Foreign key to `users(user_id)`     |

## DTOs

### CartCreateRequestDto

```json
{
  "userId": 2
}
```

### CartResponseDto

```json
{
  "cartId": 1,
  "userId": 2
}
```

## Endpoints

| Method | URL                       | Role Required  | Description           |
|--------|---------------------------|----------------|-----------------------|
| GET    | `/v1/cart/{userId}`       | USER           | Get cart by userId    |
| POST   | `/v1/cart/items`          | USER           | Add item to cart      |
| PUT    | `/v1/cart/items/{itemId}` | USER           | Update item in cart   |
| DELETE | `/v1/cart/items/{itemId}` | USER           | Remove item from cart |


