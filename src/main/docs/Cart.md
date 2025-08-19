# Entity: Cart

## Description
Represents a user's shopping cart.

## Table
- Table name: `carts`

## Fields

| Field   | Type | DB Column | Description                         |
|---------|------|-----------|-------------------------------------|
| cartId  | Long | cart_id   | Primary key                         |
| userId  | Long | user_id   | Foreign key to `app_users(user_id)` |

## DTOs

### CartResponseDto

```json
{
  "cartId": 1,
  "userId": 2,
  "items": [
    {
      "quantity": 2,
      "product": {
        "productId": 5,
        "name": "string",
        "description": "string",
        "price": 9.49,
        "discountPrice": 6.99
      }
    },
    {
      "quantity": 1,
      "product": {
        "productId": 5,
        "name": "string",
        "description": "string",
        "price": 9.49,
        "discountPrice": 6.99
      }
    }
  ]
}
```

## Endpoints

| Method | URL                           | Role Required  | Description               |
|--------|-------------------------------|----------------|---------------------------|
| GET    | `/v1/cart`                    | USER           | Get cart for current user |
| POST   | `/v1/cart/items/{productId}`  | USER           | Add product to cart       |
| PUT    | `/v1/cart/items/{cartItemId}` | USER           | Update item in cart       |
| DELETE | `/v1/cart/items/{cartItemId}` | USER           | Remove item from cart     |


